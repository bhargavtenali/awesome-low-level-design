package coffeevendingmachine.decorator;

import coffeevendingmachine.coffee.Coffee;
import coffeevendingmachine.enums.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class ExtraSugarDecorator extends Coffee {
    private static final int COST = 10;
    private final Coffee coffee;

    public ExtraSugarDecorator(Coffee coffee) {
        super(coffee.getCoffeeType() + " + Extra Sugar");
        this.coffee = coffee;
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + COST;
    }

    @Override
    public Map<Ingredient, Integer> getRecipe() {
        Map<Ingredient, Integer> recipe = new HashMap<>(coffee.getRecipe());
        recipe.merge(Ingredient.SUGAR, 1, Integer::sum);
        return recipe;
    }

    @Override
    public void addCondiments() {
        coffee.addCondiments();
        System.out.println("- Adding extra sugar.");
    }
}