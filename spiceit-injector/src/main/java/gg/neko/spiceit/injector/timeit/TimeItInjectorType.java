package gg.neko.spiceit.injector.timeit;

public enum TimeItInjectorType {

    SYSTEM_MILLIS {
        @Override
        public TimeItInjector getTimeItInjector() {
            return new SystemMillisTimeItInjector();
        }
    };

    public abstract TimeItInjector getTimeItInjector();

}
