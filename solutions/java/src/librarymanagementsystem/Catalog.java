package librarymanagementsystem;

import librarymanagementsystem.models.Book;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Catalog {

    private final Map<String, Book> books = new ConcurrentHashMap<>();

    public void addBook(Book book) {
        Book existing = books.putIfAbsent(book.getId(), book);
        if (existing != null) {
            throw new IllegalArgumentException("Book already exists: " + book.getId());
        }
    }

    public void removeBook(String bookId) {
        books.remove(bookId);
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public List<Book> getAllBooks() {
        return List.copyOf(books.values());
    }

    public List<Book> searchByTitle(String title) {
        String query = title.toLowerCase();

        return books.values()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        String query = author.toLowerCase();

        return books.values()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Book> searchByIsbn(String isbn) {
        return books.values()
                .stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }
}