public abstract class Logger {
    LogLevel level;

    protected Logger nexLogger;


    public Logger(LogLevel level)
    {
        this.level = level;
    }

    public void setNexLogger(Logger nexLogger)
    {
        this.nexLogger = nexLogger;
    }

    public void logMessage(LogLevel level, String message)
    {
        if(this.level == level)
        {
            writeLog(message);
        }

        if(this.nexLogger != null)
            this.nexLogger.logMessage(level,message);
    }

    protected abstract void writeLog(String message);

}
