package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;

public interface ATMState {
    void insertCard(ATMSystem atmSystem, String cardNumber);

    void enterPin(ATMSystem atmSystem, String pin);

    void selectOperation(ATMSystem atmSystem, OperationType operation, int... args);

    void ejectCard(ATMSystem atmSystem);
}