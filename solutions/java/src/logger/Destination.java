package logger;

import java.util.concurrent.locks.ReentrantLock;

public class Destination {
    private final Formatter formatter;
    private final LogLevel minLevel;
    private final Sink sink;
    private final ReentrantLock lock;

    public Destination(Formatter formatter, LogLevel minLevel, Sink sink) {
        this.formatter = formatter;
        this.minLevel = minLevel;
        this.sink = sink;
        this.lock = new ReentrantLock();
    }

    public void write(LogRecord record) {
        if (!record.getLevel().isAtLeast(minLevel)) {
            return;
        }

        String formatted = formatter.format(record);

        lock.lock();
        try {
            sink.write(formatted);
        } catch (Exception e) {
            System.err.println("logger: sink write failed: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}

