package librarymanagementsystem.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Member {

    private final String id;
    private final String name;
    private final List<Loan> loans = new CopyOnWriteArrayList<>();

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Loan> getLoans() {
        return List.copyOf(loans);
    }
}