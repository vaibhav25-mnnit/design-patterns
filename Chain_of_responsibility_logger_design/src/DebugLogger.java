public class DebugLogger extends Logger {

    public DebugLogger(LogLevel level)
    {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[DEBUG] "+message);
    }
}
