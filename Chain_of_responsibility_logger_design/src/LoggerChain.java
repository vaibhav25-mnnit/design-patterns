public class LoggerChain {

    public static Logger createLoggerChain()
    {
        Logger info =  new InfoLogger(LogLevel.INFO);
        Logger debug = new DebugLogger(LogLevel.DEBUG);
        Logger error = new ErrorLogger(LogLevel.ERROR);

        info.setNexLogger(debug);
        debug.setNexLogger(error);

        return info;
    }
}
