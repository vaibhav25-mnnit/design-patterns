public class InfoLogger extends Logger {

    public InfoLogger(LogLevel level)
    {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[INFO] "+message);
    }


}
