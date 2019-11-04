package gg.neko.spiceit.injector.logit;

import gg.neko.spiceit.annotation.LogIt;

/**
 * Enumeration of all available @{@link LogIt} injectors.
 */
public enum LogItInjectorType {

    /**
     * {@link LogItInjector} that uses {@link org.slf4j.Logger}.
     */
    SLF4J {
        /**
         * Factory method that returns a new instance of {@link Slf4jLogItInjector}.
         *
         * @return a new instance of {@link Slf4jLogItInjector}
         */
        @Override
        public LogItInjector getLogItInjector() {
            return new Slf4jLogItInjector();
        }
    };

    /**
     * Factory method to get a new instance of a @{@link LogIt} injector.
     *
     * @return a new instance of a @{@link LogIt} injector
     */
    public abstract LogItInjector getLogItInjector();

}
