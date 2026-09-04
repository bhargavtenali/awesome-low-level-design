package atm.chainofresponsibility;

public class NoteDispenser implements DispenseChain {
    private final int noteValue;
    private int numNotes;
    private DispenseChain nextChain;

    public NoteDispenser(int noteValue, int numNotes) {
        if (noteValue <= 0 || numNotes < 0) {
            throw new IllegalArgumentException("Invalid note configuration.");
        }
        this.noteValue = noteValue;
        this.numNotes = numNotes;
    }

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public synchronized boolean canDispense(int amount) {
        if (amount < 0) {
            return false;
        }
        if (amount == 0) {
            return true;
        }
        int maxNotes = Math.min(amount / noteValue, numNotes);
        for (int count = maxNotes; count >= 0; count--) {
            int remaining = amount - count * noteValue;
            if (remaining == 0) {
                return true;
            }
            if (nextChain != null && nextChain.canDispense(remaining)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void dispense(int amount) {
        int maxNotes = Math.min(amount / noteValue, numNotes);
        for (int count = maxNotes; count >= 0; count--) {
            int remaining = amount - count * noteValue;
            if (remaining == 0 || (nextChain != null && nextChain.canDispense(remaining))) {
                if (count > 0) {
                    numNotes -= count;
                    System.out.println("Dispensing " + count + " x $" + noteValue + " note(s)");
                }
                if (remaining > 0) {
                    nextChain.dispense(remaining);
                }
                return;
            }
        }
        throw new IllegalStateException("Cannot dispense exact amount: " + amount);
    }
}