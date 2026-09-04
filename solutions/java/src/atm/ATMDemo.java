package atm;

import atm.entities.Account;
import atm.entities.BankService;
import atm.entities.Card;
import atm.enums.OperationType;

public class ATMDemo {

    public static void main(String[] args) {
        ATMSystem atmSystem = ATMSystem.getInstance();

        BankService bankService = atmSystem.getBankService();

        Account account = bankService.createAccount("1234567890", 1000.0);
        Card card = bankService.createCard("1234-5678-9012-3456", "1234");
        bankService.linkCardToAccount(card, account);

        // Check balance
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("1234");
        atmSystem.selectOperation(OperationType.CHECK_BALANCE);

        // Withdraw cash
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("1234");
        atmSystem.selectOperation(OperationType.WITHDRAW_CASH, 570);

        // Deposit cash
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("1234");
        atmSystem.selectOperation(OperationType.DEPOSIT_CASH, 200);

        // Check balance again
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("1234");
        atmSystem.selectOperation(OperationType.CHECK_BALANCE);

        // Insufficient balance
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("1234");
        atmSystem.selectOperation(OperationType.WITHDRAW_CASH, 700);

        // Incorrect PIN
        atmSystem.insertCard("1234-5678-9012-3456");
        atmSystem.enterPin("3425");
    }
}