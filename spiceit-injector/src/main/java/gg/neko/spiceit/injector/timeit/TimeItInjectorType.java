package gg.neko.spiceit.injector.timeit;

import gg.neko.spiceit.annotation.TimeIt;

/**
 * Enumeration of all available @{@link TimeIt} injectors.
 */
public enum TimeItInjectorType {

    /**
     * {@link TimeItInjector} that uses {@link System#currentTimeMillis()}.
     */
    SYSTEM_MILLIS {
        /**
         * Factory method that returns a new instance of {@link SystemMillisTimeItInjector}.
         *
         * @return a new instance of {@link SystemMillisTimeItInjector}
         */
        @Override
        public TimeItInjector getTimeItInjector() {
            return new SystemMillisTimeItInjector();
        }
    };

    /**
     * Factory method to get a new instance of a @{@link TimeIt} injector.
     *
     * @return a new instance of a @{@link TimeIt} injector
     */
    public abstract TimeItInjector getTimeItInjector();

}
