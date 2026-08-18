package logger;

public enum LogLevel {
    DEBUG(10),
    INFO(20),
    WARN(30),
    ERROR(40),
    FATAL(50);

    private final int severity;

    LogLevel(int severity) {
        this.severity = severity;
    }

    public boolean isAtLeast(LogLevel minimum) {
        return severity >= minimum.severity;
    }
}

