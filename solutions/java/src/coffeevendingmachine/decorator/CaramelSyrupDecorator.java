package coffeevendingmachine.decorator;

import coffeevendingmachine.coffee.Coffee;
import coffeevendingmachine.enums.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class CaramelSyrupDecorator extends Coffee {
    private static final int COST = 20;
    private final Coffee coffee;

    public CaramelSyrupDecorator(Coffee coffee) {
        super(coffee.getCoffeeType() + " + Caramel Syrup");
        this.coffee = coffee;
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + COST;
    }

    @Override
    public Map<Ingredient, Integer> getRecipe() {
        Map<Ingredient, Integer> recipe = new HashMap<>(coffee.getRecipe());
        recipe.merge(Ingredient.CARAMEL_SYRUP, 1, Integer::sum);
        return recipe;
    }

    @Override
    public void addCondiments() {
        coffee.addCondiments();
        System.out.println("- Adding caramel syrup.");
    }
}