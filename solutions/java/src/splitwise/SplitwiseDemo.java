package splitwise;

import splitwise.entities.Expense;
import splitwise.entities.Group;
import splitwise.entities.Transaction;
import splitwise.entities.User;
import splitwise.strategy.EqualSplitStrategy;
import splitwise.strategy.ExactSplitStrategy;
import splitwise.strategy.PercentageSplitStrategy;

import java.util.List;

public class SplitwiseDemo {
    public static void main(String[] args) {
        SplitwiseService service = SplitwiseService.getInstance();
        User alice = service.addUser("Alice", "alice@a.com");
        User bob = service.addUser("Bob", "bob@b.com");
        User charlie = service.addUser("Charlie", "charlie@c.com");
        User david = service.addUser("David", "david@d.com");

        Group friendsGroup = service.addGroup("Friends Trip", List.of(alice, bob, charlie, david));
        System.out.println("--- System Setup Complete ---\n");
        System.out.println("--- Use Case 1: Equal Split ---");

        Expense dinner = service.createExpense(
                "Dinner",
                1000,
                alice,
                List.of(alice, bob, charlie, david),
                new EqualSplitStrategy(),
                List.of());

        friendsGroup.addExpense(dinner);
        service.showBalanceSheet(alice.getId());
        service.showBalanceSheet(bob.getId());

        System.out.println("\n--- Use Case 2: Exact Split ---");

        Expense movieTickets = service.createExpense(
                "Movie Tickets",
                370,
                alice,
                List.of(bob, charlie),
                new ExactSplitStrategy(),
                List.of(120.0, 250.0));

        friendsGroup.addExpense(movieTickets);

        service.showBalanceSheet(alice.getId());
        service.showBalanceSheet(bob.getId());

        System.out.println("\n--- Use Case 3: Percentage Split ---");

        Expense groceries = service.createExpense(
                "Groceries",
                500,
                david,
                List.of(alice, bob, charlie),
                new PercentageSplitStrategy(),
                List.of(40.0, 30.0, 30.0));

        friendsGroup.addExpense(groceries);

        System.out.println("\n--- Balances After All Expenses ---");

        service.showBalanceSheet(alice.getId());
        service.showBalanceSheet(bob.getId());
        service.showBalanceSheet(charlie.getId());
        service.showBalanceSheet(david.getId());

        System.out.println("\n--- Simplified Group Debts ---");

        List<Transaction> transactions = service.simplifyGroupDebts(friendsGroup.getId());

        if (transactions.isEmpty()) {
            System.out.println("All debts are settled within the group!");
        } else {
            transactions.forEach(System.out::println);
        }

        System.out.println("\n--- Partial Settlement ---");

        service.settleUp(bob.getId(), alice.getId(), 100);

        service.showBalanceSheet(alice.getId());
        service.showBalanceSheet(bob.getId());
    }
}