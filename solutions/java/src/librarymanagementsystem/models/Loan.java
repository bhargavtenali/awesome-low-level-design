package librarymanagementsystem.models;

import java.time.LocalDate;

public class Loan {

    private final BookCopy copy;
    private final Member member;
    private final LocalDate checkoutDate;

    private LocalDate returnDate;

    public Loan(BookCopy copy, Member member) {
        this.copy = copy;
        this.member = member;
        this.checkoutDate = LocalDate.now();
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
    }

    public BookCopy getCopy() {
        return copy;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}