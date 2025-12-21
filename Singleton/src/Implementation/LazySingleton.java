package Implementation;

/**
 *
 * This implementation is not thread-safe.
 * If multiple threads call getInstance() simultaneously when instance is null
 * it's possible to create multiple instances.
 *
 */

class LazySingleton {
    // The single instance, initially null
    private static LazySingleton instance;

    // Private constructor to prevent instantiation
    private LazySingleton() {}

    // Public method to get the instance
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}