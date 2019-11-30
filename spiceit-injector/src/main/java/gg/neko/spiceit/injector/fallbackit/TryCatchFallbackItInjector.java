package gg.neko.spiceit.injector.fallbackit;

import gg.neko.spiceit.annotation.FallbackIt;
import gg.neko.spiceit.injector.InjectorUtils;
import gg.neko.spiceit.injector.exception.SpiceItInjectorException;
import javassist.CannotCompileException;
import javassist.CtMethod;

/**
 * A {@link FallbackItInjector} implementation that wraps the method in a try-catch block.
 */
public class TryCatchFallbackItInjector implements FallbackItInjector {

    /**
     * {@inheritDoc}
     */
    @Override
    public void inject(FallbackIt fallbackIt, CtMethod ctMethod) {
        fallbackOnException(fallbackIt, ctMethod);
        if (fallbackIt.fallbackOnNull()) { fallbackOnNull(fallbackIt, ctMethod); }
    }

    private static void fallbackOnException(FallbackIt fallbackIt, CtMethod ctMethod) {
        try {
            StringBuilder catchBlock = new StringBuilder("{ ");
            setUpExceptionLists(fallbackIt, catchBlock);
            setUpFallbackConditions(catchBlock);
            fallbackOrRethrow(fallbackIt, catchBlock);
            catchBlock.append("}");

            ctMethod.addCatch(catchBlock.toString(), InjectorUtils.getCatchExceptionTypeName());
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException("failed to insert call to fallback method " + fallbackIt.fallbackMethod() + " in catch block in method " + ctMethod.getLongName(), e);
        }
    }

    private static void fallbackOnNull(FallbackIt fallbackIt, CtMethod ctMethod) {
        try {
            ctMethod.insertAfter("if (null == ($w)$_) return " + fallbackIt.fallbackMethod() + "($$);");
        } catch (CannotCompileException e) {
            throw new SpiceItInjectorException("failed to insert call to fallback method " + fallbackIt.fallbackMethod() + " on null return in method " + ctMethod.getLongName(), e);
        }
    }

    private static void setUpExceptionLists(FallbackIt fallbackIt, StringBuilder catchBlock) {
        catchBlock.append("java.util.List $SPICEIT_FALLBACK_TRIGGERING_EXCEPTIONS = new java.util.ArrayList(); ");
        for (Class<? extends Throwable> triggeringException : fallbackIt.triggeringExceptions()) {
            catchBlock.append("$SPICEIT_FALLBACK_TRIGGERING_EXCEPTIONS.add(")
                      .append(triggeringException.getName())
                      .append(".class")
                      .append("); ");
        }

        catchBlock.append("java.util.List $SPICEIT_FALLBACK_IGNORED_EXCEPTIONS = new java.util.ArrayList(); ");
        for (Class<? extends Throwable> ignoredException : fallbackIt.ignoredExceptions()) {
            catchBlock.append("$SPICEIT_FALLBACK_IGNORED_EXCEPTIONS.add(")
                      .append(ignoredException.getName())
                      .append(".class")
                      .append("); ");
        }
    }

    private static void setUpFallbackConditions(StringBuilder catchBlock) {
        catchBlock.append("boolean $SPICEIT_SHOULD_FALLBACK = false; ")
                  .append("for (int $SPICEIT_FALLBACK_INDEX = 0; " + "$SPICEIT_FALLBACK_INDEX < ")
                  .append("$SPICEIT_FALLBACK_TRIGGERING_EXCEPTIONS")
                  .append(".size(); ")
                  .append("++$SPICEIT_FALLBACK_INDEX) { ")
                  .append("if (((java.lang.Class) ")
                  .append("$SPICEIT_FALLBACK_TRIGGERING_EXCEPTIONS")
                  .append(".get($SPICEIT_FALLBACK_INDEX)")
                  .append(").isInstance($e)) { ")
                  .append("$SPICEIT_SHOULD_FALLBACK = true; ")
                  .append("break; ")
                  .append("}} ")
                  .append("boolean $SPICEIT_SHOULD_NOT_FALLBACK = false; ")
                  .append("for (int $SPICEIT_FALLBACK_INDEX = 0; ")
                  .append("$SPICEIT_FALLBACK_INDEX < ")
                  .append("$SPICEIT_FALLBACK_IGNORED_EXCEPTIONS")
                  .append(".size(); ")
                  .append("++$SPICEIT_FALLBACK_INDEX) { ")
                  .append("if (((java.lang.Class) ")
                  .append("$SPICEIT_FALLBACK_IGNORED_EXCEPTIONS")
                  .append(".get($SPICEIT_FALLBACK_INDEX)")
                  .append(").isInstance($e)) { ")
                  .append("$SPICEIT_SHOULD_NOT_FALLBACK = true; ")
                  .append("break; ")
                  .append("}} ");
    }

    private static void fallbackOrRethrow(FallbackIt fallbackIt, StringBuilder catchBlock) {
        catchBlock.append("if ($SPICEIT_SHOULD_FALLBACK && !$SPICEIT_SHOULD_NOT_FALLBACK) {")
                  .append("return ")
                  .append(fallbackIt.fallbackMethod())
                  .append("($$); } ")
                  .append("throw $e; ");
    }

}
