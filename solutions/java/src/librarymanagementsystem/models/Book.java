package librarymanagementsystem.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Book {

    private final String id;
    private final String title;
    private final String author;
    private final String isbn;
    private final List<BookCopy> copies = new CopyOnWriteArrayList<>();

    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public void addCopy(BookCopy copy) {
        copies.add(copy);
    }

    public List<BookCopy> getCopies() {
        return List.copyOf(copies);
    }

    public long getAvailableCopyCount() {
        return copies.stream()
                .filter(BookCopy::isAvailable)
                .count();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}