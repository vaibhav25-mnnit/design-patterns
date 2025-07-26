public class ErrorLogger extends Logger {

    public  ErrorLogger(LogLevel level)
    {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[ERROR] "+message);
    }
}
