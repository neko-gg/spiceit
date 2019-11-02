package gg.neko.spiceit.injector.logit;

public enum LogItInjectorType {

    SLF4J {
        @Override
        public LogItInjector getLogItInjector() {
            return new Slf4jLogItInjector();
        }
    };

    public abstract LogItInjector getLogItInjector();

}
