package librarymanagementsystem;

import librarymanagementsystem.models.Book;
import librarymanagementsystem.models.BookCopy;
import librarymanagementsystem.models.Loan;
import librarymanagementsystem.models.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryManagementSystem {

    private static final LibraryManagementSystem INSTANCE =
            new LibraryManagementSystem();

    private final Catalog catalog;
    private final Map<String, Member> members;
    private final Map<String, BookCopy> copies;
    private final TransactionService transactionService;

    private LibraryManagementSystem() {
        this.catalog = new Catalog();
        this.members = new ConcurrentHashMap<>();
        this.copies = new ConcurrentHashMap<>();
        this.transactionService = TransactionService.getInstance();
    }

    public static LibraryManagementSystem getInstance() {
        return INSTANCE;
    }

    public List<BookCopy> addBook(
            String id,
            String title,
            String author,
            String isbn,
            int numCopies) {

        if (numCopies <= 0) {
            throw new IllegalArgumentException(
                    "Number of copies must be greater than zero.");
        }

        Book book = new Book(id, title, author, isbn);
        catalog.addBook(book);

        List<BookCopy> bookCopies = new ArrayList<>();

        for (int i = 1; i <= numCopies; i++) {
            String copyId = id + "-c" + i;
            BookCopy copy = new BookCopy(copyId, book);

            copies.put(copyId, copy);
            bookCopies.add(copy);
        }

        return List.copyOf(bookCopies);
    }

    public void removeBook(String bookId) {
        Book book = catalog.getBook(bookId);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found: " + bookId);
        }

        boolean hasActiveLoan = book.getCopies()
                .stream()
                .anyMatch(copy -> !copy.isAvailable());

        if (hasActiveLoan) {
            throw new IllegalStateException(
                    "Cannot remove book while a copy is checked out.");
        }

        for (BookCopy copy : book.getCopies()) {
            copies.remove(copy.getId());
        }

        catalog.removeBook(bookId);
    }

    public Member addMember(String id, String name) {
        Member member = new Member(id, name);

        Member existing = members.putIfAbsent(id, member);

        if (existing != null) {
            throw new IllegalArgumentException(
                    "Member already exists: " + id);
        }

        return member;
    }

    public void checkout(String memberId, String copyId) {
        Member member = members.get(memberId);
        BookCopy copy = copies.get(copyId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found: " + memberId);
        }

        if (copy == null) {
            throw new IllegalArgumentException(
                    "Book copy not found: " + copyId);
        }

        transactionService.checkout(copy, member);
    }

    public void returnBook(String memberId, String copyId) {
        Member member = members.get(memberId);
        BookCopy copy = copies.get(copyId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found: " + memberId);
        }

        if (copy == null) {
            throw new IllegalArgumentException(
                    "Book copy not found: " + copyId);
        }

        transactionService.returnBook(copy, member);
    }

    public List<Book> searchByTitle(String title) {
        return catalog.searchByTitle(title);
    }

    public List<Book> searchByAuthor(String author) {
        return catalog.searchByAuthor(author);
    }

    public List<Book> searchByIsbn(String isbn) {
        return catalog.searchByIsbn(isbn);
    }

    public List<BookCopy> getCopies(String bookId) {
        Book book = catalog.getBook(bookId);

        if (book == null) {
            throw new IllegalArgumentException(
                    "Book not found: " + bookId);
        }

        return book.getCopies();
    }

    public List<Loan> getLoans(String memberId) {
        Member member = members.get(memberId);

        if (member == null) {
            throw new IllegalArgumentException(
                    "Member not found: " + memberId);
        }

        return member.getLoans();
    }

    public void printCatalog() {
        System.out.println("\n--- Library Catalog ---");

        for (Book book : catalog.getAllBooks()) {
            System.out.printf(
                    "ID: %s, Title: %s, Author: %s, ISBN: %s, Available: %d/%d%n",
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getAvailableCopyCount(),
                    book.getCopies().size());
        }

        System.out.println("-----------------------\n");
    }
}