package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;

public class AuthenticatedState implements ATMState {
    @Override
    public void insertCard(ATMSystem atmSystem, String cardNumber) {
        System.out.println("Error: A card is already inserted and a session is active.");
    }

    @Override
    public void enterPin(ATMSystem atmSystem, String pin) {
        System.out.println("Error: PIN has already been entered and authenticated.");
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operation, int... args) {
        switch (operation) {
            case CHECK_BALANCE:
                atmSystem.checkBalance();
                break;
            case WITHDRAW_CASH:
                if (args.length == 0 || args[0] <= 0) {
                    throw new IllegalArgumentException("Withdrawal amount must be positive.");
                }
                System.out.println("Processing withdrawal for $" + args[0]);
                atmSystem.withdrawCash(args[0]);
                break;
            case DEPOSIT_CASH:
                if (args.length == 0 || args[0] <= 0) {
                    throw new IllegalArgumentException("Deposit amount must be positive.");
                }
                System.out.println("Processing deposit for $" + args[0]);
                atmSystem.depositCash(args[0]);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation.");
        }
        System.out.println("Transaction complete.");
        ejectCard(atmSystem);
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        System.out.println("Card has been ejected. Thank you for using our ATM.");
        atmSystem.setCurrentCard(null);
        atmSystem.changeState(new IdleState());
    }
}