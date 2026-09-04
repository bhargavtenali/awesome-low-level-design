package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;

public class HasCardState implements ATMState {

    @Override
    public void insertCard(ATMSystem atmSystem, String cardNumber) {
        System.out.println("Error: A card is already inserted.");
    }

    @Override
    public void enterPin(ATMSystem atmSystem, String pin) {
        System.out.println("Authenticating PIN...");
        if (atmSystem.authenticate(pin)) {
            System.out.println("Authentication successful.");
            atmSystem.changeState(new AuthenticatedState());
        } else {
            System.out.println("Authentication failed: Incorrect PIN.");
            ejectCard(atmSystem);
        }
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operation, int... args) {
        System.out.println("Error: Please enter your PIN first.");
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        System.out.println("Card has been ejected. Thank you for using our ATM.");
        atmSystem.setCurrentCard(null);
        atmSystem.changeState(new IdleState());
    }
}