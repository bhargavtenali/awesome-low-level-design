package coffeevendingmachine.state;

import coffeevendingmachine.CoffeeVendingMachine;
import coffeevendingmachine.Inventory;
import coffeevendingmachine.decorator.Coffee;

public class PaidState implements VendingMachineState {
    @Override
    public void selectCoffee(CoffeeVendingMachine machine, Coffee coffee) {
        System.out.println("Cannot select another coffee now.");
    }

    @Override
    public void insertMoney(CoffeeVendingMachine machine, int amount) {
        System.out.println("Already paid. Please wait for your coffee.");
    }

    @Override
    public void dispenseCoffee(CoffeeVendingMachine machine) {
        Inventory inventory = machine.getInventory();
        Coffee coffee = machine.getSelectedCoffee();
        if (!inventory.tryDeductIngredients(coffee.getRecipe())) {
            System.out.println("Sorry, out of ingredients for " + coffee.getCoffeeType());
            System.out.println("Refunding " + machine.getMoneyInserted());
            machine.reset();
            machine.setState(new ReadyState());
            return;
        }
        coffee.prepare();
        int change = machine.getMoneyInserted() - coffee.getPrice();
        if (change > 0) {
            System.out.println("Returning change: " + change);
        }
        machine.reset();
        machine.setState(new ReadyState());
    }

    @Override
    public void cancel(CoffeeVendingMachine machine) {
        System.out.println("Transaction cancelled. Refunding " + machine.getMoneyInserted());
        machine.reset();
        machine.setState(new ReadyState());
    }
}