public class Main {
    public static void main(String[] args) {
        System.out.println("Hi there this is logger designed using chain of responsibility design pattern");

       Logger logger =  LoggerChain.createLoggerChain();

        logger.logMessage(LogLevel.INFO,"This is a info message");
        logger.logMessage(LogLevel.DEBUG,"This is a debug message");
        logger.logMessage(LogLevel.ERROR,"This is a error message");

    }
}