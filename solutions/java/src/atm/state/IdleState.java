package atm.state;

import atm.ATMSystem;
import atm.entities.Card;
import atm.enums.OperationType;

public class IdleState implements ATMState {
    @Override
    public void insertCard(ATMSystem atmSystem, String cardNumber) {
        Card card = atmSystem.getCard(cardNumber);
        if (card == null) {
            System.out.println("Error: Invalid card.");
            return;
        }
        System.out.println("\nCard has been inserted.");
        atmSystem.setCurrentCard(card);
        atmSystem.changeState(new HasCardState());
    }

    @Override
    public void enterPin(ATMSystem atmSystem, String pin) {
        System.out.println("Error: Please insert a card first.");
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operation, int... args) {
        System.out.println("Error: Please insert a card first.");
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        System.out.println("No card is inserted.");
    }
}