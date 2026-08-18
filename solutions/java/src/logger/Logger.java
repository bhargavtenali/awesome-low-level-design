package logger;

import java.time.Instant;
import java.util.List;

public class Logger {
    private final List<Destination> destinations;

    public Logger(List<Destination> destinations) {
        this.destinations = List.copyOf(destinations);
    }

    public void log(LogLevel level, String message) {
        LogRecord record = new LogRecord(
                Instant.now(),
                level,
                message,
                Thread.currentThread().getName()
        );

        for (Destination destination : destinations) {
            destination.write(record);
        }
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }
}
