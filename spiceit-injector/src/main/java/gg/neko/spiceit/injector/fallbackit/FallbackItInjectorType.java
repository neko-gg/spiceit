package gg.neko.spiceit.injector.fallbackit;

import gg.neko.spiceit.annotation.FallbackIt;

/**
 * Enumeration of all available @{@link FallbackIt} injectors.
 */
public enum FallbackItInjectorType {

    /**
     * {@link FallbackItInjector} that uses try-catch blocks.
     */
    TRY_CATCH {
        /**
         * Factory method that returns a new instance of {@link TryCatchFallbackItInjector}.
         *
         * @return a new instance of {@link TryCatchFallbackItInjector}
         */
        @Override
        public FallbackItInjector getFallbackItInjector() {
            return new TryCatchFallbackItInjector();
        }
    };

    /**
     * Factory method to get a new instance of a @{@link FallbackIt} injector.
     *
     * @return a new instance of a @{@link FallbackIt} injector
     */
    public abstract FallbackItInjector getFallbackItInjector();

}
