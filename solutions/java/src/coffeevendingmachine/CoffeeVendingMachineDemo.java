package coffeevendingmachine;

import coffeevendingmachine.enums.CoffeeType;
import coffeevendingmachine.enums.Ingredient;
import coffeevendingmachine.enums.ToppingType;

import java.util.List;

public class CoffeeVendingMachineDemo {
    public static void main(String[] args) {
        CoffeeVendingMachine machine = CoffeeVendingMachine.getInstance();
        Inventory inventory = machine.getInventory();

        System.out.println("=== Initializing Vending Machine ===");

        inventory.addStock(Ingredient.COFFEE_BEANS, 50);
        inventory.addStock(Ingredient.WATER, 500);
        inventory.addStock(Ingredient.MILK, 200);
        inventory.addStock(Ingredient.SUGAR, 100);
        inventory.addStock(Ingredient.CARAMEL_SYRUP, 50);

        inventory.printInventory();

        System.out.println("\n--- SCENARIO 1: Buy Latte ---");

        machine.selectCoffee(CoffeeType.LATTE, List.of());
        machine.insertMoney(200);
        machine.insertMoney(50);
        machine.dispenseCoffee();

        inventory.printInventory();

        System.out.println("\n--- SCENARIO 2: Insufficient Funds ---");

        machine.selectCoffee(CoffeeType.ESPRESSO, List.of());
        machine.insertMoney(100);
        machine.dispenseCoffee();
        machine.cancel();

        System.out.println("\n--- SCENARIO 3: Coffee With Toppings ---");

        machine.selectCoffee(
                CoffeeType.LATTE,
                List.of(ToppingType.EXTRA_SUGAR, ToppingType.CARAMEL_SYRUP));

        machine.insertMoney(300);
        machine.dispenseCoffee();

        inventory.printInventory();

        System.out.println("\n--- SCENARIO 4: Out Of Ingredients ---");

        machine.selectCoffee(CoffeeType.CAPPUCCINO, List.of());
        machine.insertMoney(300);
        machine.dispenseCoffee();

        inventory.printInventory();

        System.out.println("\n--- REFILLING ---");

        inventory.addStock(Ingredient.MILK, 200);
        inventory.printInventory();

        machine.selectCoffee(CoffeeType.CAPPUCCINO, List.of());
        machine.insertMoney(300);
        machine.dispenseCoffee();

        inventory.printInventory();
    }
}