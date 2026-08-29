package librarymanagementsystem;

import librarymanagementsystem.models.BookCopy;
import librarymanagementsystem.models.Loan;
import librarymanagementsystem.models.Member;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionService {

    private static final TransactionService INSTANCE =
            new TransactionService();

    private final Map<String, Loan> activeLoans =
            new ConcurrentHashMap<>();

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        return INSTANCE;
    }

    public void checkout(BookCopy copy, Member member) {

        synchronized (copy) {

            if (!copy.isAvailable()) {
                throw new IllegalStateException(
                        "Book copy is not available: " + copy.getId());
            }

            Loan loan = new Loan(copy, member);

            Loan existing =
                    activeLoans.putIfAbsent(
                            copy.getId(),
                            loan);

            if (existing != null) {
                throw new IllegalStateException(
                        "Book copy is already on loan: " + copy.getId());
            }

            if (!copy.tryCheckout()) {
                activeLoans.remove(copy.getId(), loan);

                throw new IllegalStateException(
                        "Book copy is not available: " + copy.getId());
            }

            member.addLoan(loan);
        }
    }

    public void returnBook(
            BookCopy copy,
            Member member) {

        synchronized (copy) {

            Loan loan = activeLoans.get(copy.getId());

            if (loan == null) {
                throw new IllegalStateException(
                        "Book copy is not currently on loan: "
                                + copy.getId());
            }

            if (!loan.getMember()
                    .getId()
                    .equals(member.getId())) {

                throw new IllegalStateException(
                        "Book copy was not borrowed by this member.");
            }

            if (!activeLoans.remove(copy.getId(), loan)) {
                throw new IllegalStateException(
                        "Unable to return book copy.");
            }

            if (!copy.tryReturn()) {
                activeLoans.putIfAbsent(copy.getId(), loan);

                throw new IllegalStateException(
                        "Book copy is not checked out.");
            }

            loan.markReturned();
            member.removeLoan(loan);
        }
    }
}