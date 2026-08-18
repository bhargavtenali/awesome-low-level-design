package logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSink implements Sink, AutoCloseable {
    private final BufferedWriter writer;

    public FileSink(String filePath) throws IOException {
        this.writer = Files.newBufferedWriter(
                Path.of(filePath),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    @Override
    public void write(String formatted) throws IOException {
        writer.write(formatted);
        writer.newLine();
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
