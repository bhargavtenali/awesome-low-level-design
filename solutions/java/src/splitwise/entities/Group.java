package splitwise.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {
    private final String id;
    private final String name;
    private final List<User> members;
    private final List<Expense> expenses;

    public Group(String name, List<User> members) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name is required.");
        }
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Group must have at least one member.");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.members = new ArrayList<>(members);
        this.expenses = new ArrayList<>();
    }

    public synchronized void addMember(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public synchronized void removeMember(User user) {
        members.remove(user);
    }

    public synchronized void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized List<User> getMembers() {
        return List.copyOf(members);
    }

    public synchronized List<Expense> getExpenses() {
        return List.copyOf(expenses);
    }
}