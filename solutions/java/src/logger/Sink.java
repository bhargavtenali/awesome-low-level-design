package logger;

public interface Sink {
    void write(String formatted) throws Exception;
}
