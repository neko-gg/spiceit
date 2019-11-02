package gg.neko.spiceit.injector;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class PatternSolverTest {

    @Test
    void shouldSolveSimplePattern() throws NotFoundException, CannotCompileException {
        CtMethod ctMethod = getCtMethod();
        String solvedPattern = PatternSolver.solve("${method.class.name}#${method.class.simpleName}#${method.name}#${method.signature}#${method.longName}#${method.return}#${method.exception.message}#${method.args}#${method.time}",
                                                   ctMethod);

        Assertions.assertNotNull(solvedPattern);
        Assertions.assertEquals("\"$TEST_PACKAGE.$TEST_CLASS#$TEST_CLASS#$TEST_METHOD#$TEST_METHOD(int, long)#$TEST_PACKAGE.$TEST_CLASS.$TEST_METHOD(int,long)#\" + java.lang.String.valueOf(($w)$_) + \"#\" + $e.getMessage() + \"#\" + java.util.Arrays.toString(java.util.Arrays.copyOfRange($args, 0, ($args).length)) + \"#\" + \"TIMING_UNAVAILABLE\" + \"\"",
                                solvedPattern);
    }

    @Test
    void shouldSolvePatternWithIndexedArgs() throws NotFoundException, CannotCompileException {
        CtMethod ctMethod = getCtMethod();
        String solvedPattern = PatternSolver.solve("${method.args[1]}#${method.args[2]}", ctMethod);

        Assertions.assertNotNull(solvedPattern);
        Assertions.assertEquals("\"\" + java.lang.String.valueOf(($w)$1) + \"#\" + java.lang.String.valueOf(($w)$2) + \"\"", solvedPattern);
    }

    @Test
    void shouldSolvePatternWithArgsOffset() throws NotFoundException, CannotCompileException {
        CtMethod ctMethod = getCtMethod();
        String solvedPattern = PatternSolver.solve("${method.args[1]}#${method.args[2]}", ctMethod, "", 1);

        Assertions.assertNotNull(solvedPattern);
        Assertions.assertEquals("\"\" + java.lang.String.valueOf(($w)$2) + \"#\" + java.lang.String.valueOf(($w)$3) + \"\"", solvedPattern);
    }

    @Test
    void shouldSolvePatternWithTimeReplacement() throws NotFoundException, CannotCompileException {
        CtMethod ctMethod = getCtMethod();
        String solvedPattern = PatternSolver.solve("${method.time}", ctMethod, "$TEST_TIME_REPLACEMENT", 0);

        Assertions.assertNotNull(solvedPattern);
        Assertions.assertEquals("\"\" + $TEST_TIME_REPLACEMENT + \"\"", solvedPattern);
    }

    @Test
    void constructorShouldNotBeAccessible() throws ReflectiveOperationException {
        Constructor<PatternSolver> declaredConstructor = PatternSolver.class.getDeclaredConstructor();
        Assertions.assertFalse(declaredConstructor.isAccessible());
    }

    @Test
    void shouldThrowExceptionWhenInstantiatedWithReflection() throws ReflectiveOperationException {
        Constructor<PatternSolver> declaredConstructor = PatternSolver.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, declaredConstructor::newInstance);

        try {
            declaredConstructor.newInstance();
        } catch (InvocationTargetException e) {
            Assertions.assertNotNull(e.getCause());
            Assertions.assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
        }
    }

    private CtMethod getCtMethod() throws CannotCompileException, NotFoundException {
        CtClass ctClass = ClassPool.getDefault().makeClass("$TEST_PACKAGE.$TEST_CLASS");
        return CtNewMethod.make(Modifier.FINAL,
                                CtClass.longType,
                                "$TEST_METHOD",
                                new CtClass[]{CtClass.intType, CtClass.longType},
                                new CtClass[]{ClassPool.getDefault().get(Exception.class.getName())},
                                "return 69;",
                                ctClass);
    }

}
