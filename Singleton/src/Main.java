import Implementation.EnumSingleton;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!This is Singleton  pattern");

        EnumSingleton enumSingleton =  EnumSingleton.INSTANCE;

        System.out.println("Host "+enumSingleton.getHost()+" Port "+enumSingleton.getPort());
    }
}