package logger;

public class ConsoleSink implements Sink {
    @Override
    public void write(String formatted) {
        System.out.println(formatted);
    }
}

