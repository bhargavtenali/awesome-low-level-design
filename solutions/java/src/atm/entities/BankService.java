package atm.entities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final Map<Card, Account> cardAccountMap = new ConcurrentHashMap<>();

    public Account createAccount(String accountNumber, double initialBalance) {
        Account account = new Account(accountNumber, initialBalance);
        Account existing = accounts.putIfAbsent(accountNumber, account);
        if (existing != null) {
            throw new IllegalArgumentException("Account already exists: " + accountNumber);
        }
        return account;
    }

    public Card createCard(String cardNumber, String pin) {
        Card card = new Card(cardNumber, pin);
        Card existing = cards.putIfAbsent(cardNumber, card);
        if (existing != null) {
            throw new IllegalArgumentException("Card already exists: " + cardNumber);
        }
        return card;
    }

    public void linkCardToAccount(Card card, Account account) {
        if (card == null || account == null) {
            throw new IllegalArgumentException("Card and account are required.");
        }
        cardAccountMap.put(card, account);
    }

    public boolean authenticate(Card card, String pin) {
        return card != null && card.getPin().equals(pin) && cardAccountMap.containsKey(card);
    }

    public Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }

    public double getBalance(Card card) {
        Account account = getAccount(card);
        return account.getBalance();
    }

    public boolean withdrawMoney(Card card, double amount) {
        Account account = getAccount(card);
        return account.withdraw(amount);
    }

    public void depositMoney(Card card, double amount) {
        Account account = getAccount(card);
        account.deposit(amount);
    }

    private Account getAccount(Card card) {
        Account account = cardAccountMap.get(card);
        if (account == null) {
            throw new IllegalStateException("Card is not linked to a bank account.");
        }
        return account;
    }
}