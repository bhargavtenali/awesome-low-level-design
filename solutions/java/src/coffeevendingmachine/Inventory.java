package coffeevendingmachine;

import coffeevendingmachine.enums.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Ingredient, Integer> stock = new HashMap<>();

    public synchronized void addStock(Ingredient ingredient, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        stock.merge(ingredient, quantity, Integer::sum);
    }

    public synchronized boolean tryDeductIngredients(Map<Ingredient, Integer> recipe) {
        if (!hasIngredients(recipe)) {
            return false;
        }
        recipe.forEach((ingredient, quantity) ->
                stock.put(ingredient, stock.get(ingredient) - quantity));
        return true;
    }

    public synchronized boolean hasIngredients(Map<Ingredient, Integer> recipe) {
        return recipe.entrySet()
                .stream()
                .allMatch(entry -> stock.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    public synchronized int getStock(Ingredient ingredient) {
        return stock.getOrDefault(ingredient, 0);
    }

    public synchronized void printInventory() {
        System.out.println("--- Current Inventory ---");
        stock.forEach((ingredient, quantity) -> System.out.println(ingredient + ": " + quantity));
        System.out.println("-------------------------");
    }
}