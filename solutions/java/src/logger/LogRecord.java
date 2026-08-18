package logger;

import java.time.Instant;

public class LogRecord {
    private final Instant timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;

    public LogRecord(Instant timestamp, LogLevel level, String message, String threadName) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.threadName = threadName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getThreadName() {
        return threadName;
    }
}
