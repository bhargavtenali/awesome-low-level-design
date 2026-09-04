package atm;

import atm.chainofresponsibility.DispenseChain;
import atm.chainofresponsibility.NoteDispenser;
import atm.entities.BankService;
import atm.entities.Card;
import atm.entities.CashDispenser;
import atm.enums.OperationType;
import atm.state.ATMState;
import atm.state.IdleState;

public class ATMSystem {
    private static final ATMSystem INSTANCE = new ATMSystem();
    private final BankService bankService;
    private final CashDispenser cashDispenser;
    private ATMState currentState;
    private Card currentCard;

    private ATMSystem() {
        this.currentState = new IdleState();
        this.bankService = new BankService();
        DispenseChain c100 = new NoteDispenser(100, 10);
        DispenseChain c50 = new NoteDispenser(50, 20);
        DispenseChain c20 = new NoteDispenser(20, 30);
        c100.setNextChain(c50);
        c50.setNextChain(c20);
        this.cashDispenser = new CashDispenser(c100);
    }

    public static ATMSystem getInstance() {
        return INSTANCE;
    }

    public synchronized void changeState(ATMState newState) {
        this.currentState = newState;
    }

    public synchronized void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public synchronized void insertCard(String cardNumber) {
        currentState.insertCard(this, cardNumber);
    }

    public synchronized void enterPin(String pin) {
        currentState.enterPin(this, pin);
    }

    public synchronized void selectOperation(OperationType operation, int... args) {
        currentState.selectOperation(this, operation, args);
    }

    public Card getCard(String cardNumber) {
        return bankService.getCard(cardNumber);
    }

    public boolean authenticate(String pin) {
        return currentCard != null && bankService.authenticate(currentCard, pin);
    }

    public synchronized void checkBalance() {
        double balance = bankService.getBalance(currentCard);
        System.out.printf("Your current account balance is: $%.2f%n", balance);
    }

    public synchronized void withdrawCash(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (!cashDispenser.canDispenseCash(amount)) {
            throw new IllegalStateException("ATM cannot dispense the requested amount.");
        }
        if (!bankService.withdrawMoney(currentCard, amount)) {
            throw new IllegalStateException("Insufficient account balance.");
        }
        try {
            cashDispenser.dispenseCash(amount);
        } catch (RuntimeException e) {
            bankService.depositMoney(currentCard, amount);
            throw new IllegalStateException("Unable to dispense cash. Transaction rolled back.", e);
        }
        System.out.println("Please collect your cash: $" + amount);
    }

    public synchronized void depositCash(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        bankService.depositMoney(currentCard, amount);
        System.out.println("Cash deposited successfully: $" + amount);
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public BankService getBankService() {
        return bankService;
    }
}