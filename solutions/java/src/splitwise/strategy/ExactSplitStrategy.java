package splitwise.strategy;

import splitwise.entities.Split;
import splitwise.entities.User;

import java.util.ArrayList;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, User paidBy, List<User> participants, List<Double> splitValues) {
        if (participants.size() != splitValues.size()) {
            throw new IllegalArgumentException("Number of participants and split values must match.");
        }

        double sum = splitValues.stream().mapToDouble(Double::doubleValue).sum();

        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new IllegalArgumentException("Sum of exact amounts must equal the total expense amount.");
        }

        List<Split> splits = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            double amount = splitValues.get(i);

            if (amount < 0) {
                throw new IllegalArgumentException("Split amount cannot be negative.");
            }

            splits.add(new Split(participants.get(i), amount));
        }

        return splits;
    }
}