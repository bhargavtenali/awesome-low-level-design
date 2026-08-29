package librarymanagementsystem.models;

import librarymanagementsystem.enums.BookCopyStatus;

public class BookCopy {

    private final String id;
    private final Book book;

    private BookCopyStatus status;

    private final Object lock = new Object();

    public BookCopy(String id, Book book) {
        this.id = id;
        this.book = book;
        this.status = BookCopyStatus.AVAILABLE;

        book.addCopy(this);
    }

    public synchronized boolean tryCheckout() {
        if (status != BookCopyStatus.AVAILABLE) {
            return false;
        }

        status = BookCopyStatus.CHECKED_OUT;
        return true;
    }

    public synchronized boolean tryReturn() {
        if (status != BookCopyStatus.CHECKED_OUT) {
            return false;
        }

        status = BookCopyStatus.AVAILABLE;
        return true;
    }

    public synchronized boolean isAvailable() {
        return status == BookCopyStatus.AVAILABLE;
    }

    public synchronized BookCopyStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Object getLock() { return lock; }
}