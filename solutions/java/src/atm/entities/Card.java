package atm.entities;

public class Card {

    private final String cardNumber;
    private final String pin;

    public Card(String cardNumber, String pin) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number is required.");
        }
        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException("PIN is required.");
        }
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }
}