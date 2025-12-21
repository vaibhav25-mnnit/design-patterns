package Implementation;

/**
 * While it is inherently thread-safe,
 * it could potentially waste resources
 * if the singleton instance is never used by the client application.
 */
class EagerSingleton {
    // The single instance, created immediately
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return instance;
    }
}
