package librarymanagementsystem;

import librarymanagementsystem.models.Book;
import librarymanagementsystem.models.BookCopy;
import librarymanagementsystem.models.Member;

import java.util.List;

public class LibraryManagementDemo {

    public static void main(String[] args) {

        LibraryManagementSystem library =
                LibraryManagementSystem.getInstance();

        System.out.println("=== Setting up the Library ===");

        List<BookCopy> hobbitCopies =
                library.addBook(
                        "B001",
                        "The Hobbit",
                        "J.R.R. Tolkien",
                        "978-0547928227",
                        2);

        List<BookCopy> duneCopies =
                library.addBook(
                        "B002",
                        "Dune",
                        "Frank Herbert",
                        "978-0441172719",
                        1);

        Member alice =
                library.addMember(
                        "MEM01",
                        "Alice");

        Member bob =
                library.addMember(
                        "MEM02",
                        "Bob");

        Member charlie =
                library.addMember(
                        "MEM03",
                        "Charlie");

        library.printCatalog();

        // ---------------- Search ----------------

        System.out.println(
                "=== Search by Title ===");

        library.searchByTitle("Dune")
                .forEach(book ->
                        System.out.println(
                                "Found: "
                                        + book.getTitle()));

        System.out.println(
                "\n=== Search by Author ===");

        library.searchByAuthor("Tolkien")
                .forEach(book ->
                        System.out.println(
                                "Found: "
                                        + book.getTitle()));

        System.out.println(
                "\n=== Search by ISBN ===");

        library.searchByIsbn("978-0441172719")
                .forEach(book ->
                        System.out.println(
                                "Found: "
                                        + book.getTitle()));

        // ---------------- Checkout ----------------

        System.out.println(
                "\n=== Checkout ===");

        library.checkout(
                alice.getId(),
                hobbitCopies.get(0).getId());

        library.checkout(
                bob.getId(),
                duneCopies.get(0).getId());

        library.printCatalog();

        // ---------------- Invalid checkout ----------------

        System.out.println(
                "Trying to checkout an already checked-out copy:");

        try {
            library.checkout(
                    charlie.getId(),
                    hobbitCopies.get(0).getId());
        } catch (IllegalStateException e) {
            System.out.println(
                    "Checkout failed: "
                            + e.getMessage());
        }

        // ---------------- Return ----------------

        System.out.println(
                "\n=== Return ===");

        library.returnBook(
                alice.getId(),
                hobbitCopies.get(0).getId());

        library.printCatalog();

        // ---------------- Member loans ----------------

        System.out.println(
                "Alice active loans: "
                        + library
                        .getLoans(alice.getId())
                        .size());

        System.out.println(
                "Bob active loans: "
                        + library
                        .getLoans(bob.getId())
                        .size());
    }
}