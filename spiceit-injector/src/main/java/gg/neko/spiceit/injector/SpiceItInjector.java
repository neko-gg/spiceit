package gg.neko.spiceit.injector;

import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.annotation.TimeIt;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import gg.neko.spiceit.util.SpiceItUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Analyzes the bytecode in {@code .class} files to find {@code SpiceIt} APIs usage, and invokes {@code SpiceIt} injectors.
 */
public class SpiceItInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiceItInjector.class);

    private static final String ORDER_METHOD_NAME = "order";

    private SpiceItInjector() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    /**
     * Finds {@code SpiceIt} APIs usage in all {@code .class} files in {@code targetDirectory},
     * and injects the matching features into the bytecode,
     * overwriting all modified {@code .class} files.
     *
     * @param targetDirectory directory containing {@code .class} files to revise.
     * @param classPaths      {@code .jar} files, or directories containing {@code .class} files, to add to the classpath
     *                        to resolve dependencies.
     */
    public static void revise(File targetDirectory, File... classPaths) {
        if (!targetDirectory.isDirectory()) {
            throw new SpiceItInjectorException("targetDirectory must be a directory");
        }

        loadClassPathsInClassPool(classPaths);
        revise(targetDirectory);
    }

    /**
     * Finds {@code SpiceIt} APIs usage in a {@code .class} file bytecode,
     * injects the matching features,
     * and returns the modified bytecode.
     *
     * @param classBytecode {@code .class} file bytecode as a byte array
     * @return the modified bytecode
     */
    public static byte[] revise(byte[] classBytecode) {
        try {
            CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classBytecode), false);
            revise(ctClass);
            return ctClass.toBytecode();
        } catch (IOException | CannotCompileException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    /**
     * Walks {@code targetDirectory}, and revises any {@code .class} file found.
     *
     * @param targetDirectory directory containing {@code .class} files to revise.
     */
    private static void revise(File targetDirectory) {
        try {
            Files.walk(Paths.get(targetDirectory.toURI()))
                 .filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".class"))
                 .map(classFile -> makeCtClass(classFile.toFile()))
                 .filter(SpiceItInjector::revise)
                 .forEach(ctClass -> writeClassFile(ctClass, targetDirectory));
        } catch (IOException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    /**
     * Revises a {@link CtClass}, applying the matching injectors.
     *
     * @param ctClass the {@link CtClass} to revise
     * @return whether the {@link CtClass} was modified
     */
    private static boolean revise(CtClass ctClass) {
        if (!hasAnySpiceItAnnotation(ctClass)) { return false; }

        LOGGER.info("Spicing class {}", ctClass.getName());
        Arrays.stream(ctClass.getDeclaredMethods())
              .forEach(ctMethod -> Arrays.stream(getAnnotations(ctMethod))
                                         .map(annotation -> (Annotation) annotation)
                                         .filter(annotation -> isAnnotationOfType(SpiceItUtils.spiceItAnnotations(), annotation))
                                         .sorted(Comparator.comparingInt(SpiceItInjector::getInjectionOrder))
                                         .forEachOrdered(annotation -> applyAnnotation(annotation, ctMethod)));
        LOGGER.info("Spicing complete for class {}", ctClass.getName());

        return true;
    }

    private static boolean hasAnySpiceItAnnotation(CtClass ctClass) {
        return Arrays.stream(ctClass.getDeclaredMethods())
                     .flatMap(ctMethod -> Arrays.stream(getAnnotations(ctMethod)))
                     .map(annotation -> (Annotation) annotation)
                     .anyMatch(annotation -> isAnnotationOfType(SpiceItUtils.spiceItAnnotations(), annotation));
    }

    private static int getInjectionOrder(Annotation annotation) {
        try {
            Method orderMethod = annotation.annotationType().getMethod(ORDER_METHOD_NAME);
            return (int) orderMethod.invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new SpiceItInjectorException("failed to retrieve order for " + annotation.annotationType().getName(), e);
        }
    }

    private static void writeClassFile(CtClass ctClass, File targetDirectory) {
        try {
            LOGGER.info("Overwriting .class file for class {}", ctClass.getName());
            ctClass.writeFile(targetDirectory.getAbsolutePath());
            ctClass.defrost();
        } catch (IOException | CannotCompileException e) {
            throw new SpiceItInjectorException("failed to overwrite .class file for " + ctClass.getName(), e);
        }
    }

    private static void applyAnnotation(Annotation annotation, CtMethod ctMethod) {
        LOGGER.info("Injecting {} in method {}",
                    annotation.annotationType().getSimpleName(),
                    InjectorUtils.getMethodSignature(ctMethod));

        if (annotation instanceof LogIt) {
            LogItInjector.inject((LogIt) annotation, ctMethod);
        } else if (annotation instanceof TimeIt) {
            TimeItInjector.inject((TimeIt) annotation, ctMethod);
        }

        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        annotationsAttribute.removeAnnotation(annotation.annotationType().getName());
    }

    private static Object[] getAnnotations(CtMethod ctMethod) {
        try {
            return ctMethod.getAnnotations();
        } catch (ClassNotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static boolean isAnnotationOfType(List<Class<? extends Annotation>> annotationTypes, Annotation annotation) {
        return annotationTypes.contains(annotation.annotationType());
    }

    private static CtClass makeCtClass(File classFile) {
        try {
            return ClassPool.getDefault().makeClass(new FileInputStream(classFile), false);
        } catch (IOException e) {
            throw new SpiceItInjectorException(e);
        }
    }

    private static void loadClassPathsInClassPool(File... classPaths) {
        Arrays.stream(classPaths)
              .map(File::getAbsolutePath)
              .forEach(SpiceItInjector::appendClassPath);
    }

    private static void appendClassPath(String classPath) {
        try {
            LOGGER.debug("Adding to classpath: {}", classPath);
            ClassPool.getDefault().appendClassPath(classPath);
        } catch (NotFoundException e) {
            throw new SpiceItInjectorException(e);
        }
    }

}
