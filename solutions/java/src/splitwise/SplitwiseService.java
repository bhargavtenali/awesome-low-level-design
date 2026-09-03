package splitwise;

import splitwise.entities.Expense;
import splitwise.entities.Group;
import splitwise.entities.Split;
import splitwise.entities.Transaction;
import splitwise.entities.User;
import splitwise.strategy.SplitStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SplitwiseService {
    private static final SplitwiseService instance = new SplitwiseService();
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Group> groups = new ConcurrentHashMap<>();

    private SplitwiseService() {
    }

    public static synchronized SplitwiseService getInstance() {
        return instance;
    }

    public User addUser(String name, String email) {
        User user = new User(name, email);
        User existing = users.putIfAbsent(user.getId(), user);
        if (existing != null) {
            throw new IllegalArgumentException("User already exists: " + user.getId());
        }
        return user;
    }

    public Group addGroup(String name, List<User> members) {
        Group group = new Group(name, members);
        Group existing = groups.putIfAbsent(group.getId(), group);
        if (existing != null) {
            throw new IllegalArgumentException("Group already exists: " + group.getId());
        }
        return group;
    }

    public User getUser(String id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user;
    }

    public Group getGroup(String id) {
        Group group = groups.get(id);

        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + id);
        }

        return group;
    }

    public synchronized Expense createExpense(String description, double amount, User paidBy, List<User> participants,
                                              SplitStrategy splitStrategy, List<Double> splitValues) {
        Expense expense = new Expense(description, amount, paidBy, participants, splitStrategy, splitValues);
        for (Split split : expense.getSplits()) {
            User participant = split.getUser();
            double share = split.getAmount();
            if (!paidBy.equals(participant)) {
                paidBy.getBalanceSheet().adjustBalance(participant, share);
                participant.getBalanceSheet().adjustBalance(paidBy, -share);
            }
        }
        System.out.println("Expense '" + description + "' of amount " + amount + " created.");
        return expense;
    }

    public synchronized void settleUp(String payerId, String payeeId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Settlement amount must be positive.");
        }
        User payer = getUser(payerId);
        User payee = getUser(payeeId);
        if (payer.equals(payee)) {
            throw new IllegalArgumentException("Payer and payee cannot be the same.");
        }
        double currentOwed = -payer.getBalanceSheet().getBalance(payee);
        if (amount > currentOwed + 0.01) {
            throw new IllegalArgumentException("Settlement amount exceeds outstanding debt.");
        }
        payer.getBalanceSheet().adjustBalance(payee, amount);
        payee.getBalanceSheet().adjustBalance(payer, -amount);
        System.out.println(payer.getName() + " settled " + amount + " with " + payee.getName());
    }

    public void showBalanceSheet(String userId) {
        getUser(userId).getBalanceSheet().showBalances();
    }

    public synchronized List<Transaction> simplifyGroupDebts(String groupId) {
        Group group = getGroup(groupId);
        Map<User, Double> netBalances = new HashMap<>();
        for (User member : group.getMembers()) {
            double balance = 0;
            for (Map.Entry<User, Double> entry : member.getBalanceSheet().getBalances().entrySet()) {
                if (group.getMembers().contains(entry.getKey())) {
                    balance += entry.getValue();
                }
            }
            netBalances.put(member, balance);
        }
        List<Map.Entry<User, Double>> creditors = netBalances.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0.01)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        List<Map.Entry<User, Double>> debtors = netBalances.entrySet()
                .stream()
                .filter(entry -> entry.getValue() < -0.01)
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<Transaction> transactions = new ArrayList<>();
        int creditorIndex = 0;
        int debtorIndex = 0;
        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            Map.Entry<User, Double> creditor = creditors.get(creditorIndex);
            Map.Entry<User, Double> debtor = debtors.get(debtorIndex);

            double amount = Math.min(creditor.getValue(), -debtor.getValue());

            transactions.add(new Transaction(debtor.getKey(), creditor.getKey(), amount));

            creditor.setValue(creditor.getValue() - amount);
            debtor.setValue(debtor.getValue() + amount);

            if (Math.abs(creditor.getValue()) < 0.01) {
                creditorIndex++;
            }

            if (Math.abs(debtor.getValue()) < 0.01) {
                debtorIndex++;
            }
        }
        return transactions;
    }
}