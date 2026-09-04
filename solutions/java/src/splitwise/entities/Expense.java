package splitwise.entities;

import splitwise.strategy.SplitStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class Expense {

    private final String id;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<Split> splits;
    private final LocalDateTime timestamp;

    public Expense(String description, double amount, User paidBy, List<User> participants, SplitStrategy splitStrategy,
                   List<Double> splitValues) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive.");
        }
        if (paidBy == null) {
            throw new IllegalArgumentException("Paid-by user is required.");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("At least one participant is required.");
        }
        if (splitStrategy == null) {
            throw new IllegalArgumentException("Split strategy is required.");
        }

        this.id = java.util.UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.timestamp = LocalDateTime.now();

        this.splits = splitStrategy.calculateSplits(amount, paidBy, participants, splitValues);

        double totalSplit = this.splits.stream().mapToDouble(Split::getAmount).sum();

        if (Math.abs(totalSplit - amount) > 0.01) {
            throw new IllegalArgumentException("Split amounts must equal the expense amount.");
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}