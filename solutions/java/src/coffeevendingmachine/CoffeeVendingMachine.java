package coffeevendingmachine;

import coffeevendingmachine.decorator.CaramelSyrupDecorator;
import coffeevendingmachine.decorator.Coffee;
import coffeevendingmachine.decorator.ExtraSugarDecorator;
import coffeevendingmachine.enums.CoffeeType;
import coffeevendingmachine.enums.ToppingType;
import coffeevendingmachine.factory.CoffeeFactory;
import coffeevendingmachine.state.ReadyState;
import coffeevendingmachine.state.VendingMachineState;

import java.util.List;

public class CoffeeVendingMachine {
    private static final CoffeeVendingMachine INSTANCE = new CoffeeVendingMachine();
    private final Inventory inventory;
    private VendingMachineState state;
    private Coffee selectedCoffee;
    private int moneyInserted;

    private CoffeeVendingMachine() {
        this.inventory = new Inventory();
        this.state = new ReadyState();
    }

    public static CoffeeVendingMachine getInstance() {
        return INSTANCE;
    }

    public synchronized void selectCoffee(CoffeeType type, List<ToppingType> toppings) {
        Coffee coffee = CoffeeFactory.createCoffee(type);
        for (ToppingType topping : toppings) {
            switch (topping) {
                case EXTRA_SUGAR:
                    coffee = new ExtraSugarDecorator(coffee);
                    break;
                case CARAMEL_SYRUP:
                    coffee = new CaramelSyrupDecorator(coffee);
                    break;
            }
        }
        state.selectCoffee(this, coffee);
    }

    public synchronized void insertMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Money must be positive.");
        }
        state.insertMoney(this, amount);
    }

    public synchronized void dispenseCoffee() {
        state.dispenseCoffee(this);
    }

    public synchronized void cancel() {
        state.cancel(this);
    }

    public synchronized VendingMachineState getState() {
        return state;
    }

    public synchronized void setState(VendingMachineState state) {
        this.state = state;
    }

    public synchronized Coffee getSelectedCoffee() {
        return selectedCoffee;
    }

    public synchronized void setSelectedCoffee(Coffee selectedCoffee) {
        this.selectedCoffee = selectedCoffee;
    }

    public synchronized int getMoneyInserted() {
        return moneyInserted;
    }

    public synchronized void setMoneyInserted(int moneyInserted) {
        this.moneyInserted = moneyInserted;
    }

    public synchronized Inventory getInventory() {
        return inventory;
    }

    public synchronized void reset() {
        selectedCoffee = null;
        moneyInserted = 0;
    }
}