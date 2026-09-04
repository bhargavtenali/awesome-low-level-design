package atm.entities;

import atm.chainofresponsibility.DispenseChain;

public class CashDispenser {

    private final DispenseChain chain;

    public CashDispenser(DispenseChain chain) {
        this.chain = chain;
    }

    public synchronized boolean canDispenseCash(int amount) {
        return amount > 0 && chain.canDispense(amount);
    }

    public synchronized void dispenseCash(int amount) {
        chain.dispense(amount);
    }
}