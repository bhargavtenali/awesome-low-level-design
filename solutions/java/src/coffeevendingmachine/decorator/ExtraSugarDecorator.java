package coffeevendingmachine.decorator;

import coffeevendingmachine.enums.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class ExtraSugarDecorator extends Coffee {

    private final Coffee coffee;

    public ExtraSugarDecorator(Coffee coffee) {
        super(coffee.getCoffeeType() + " + Extra Sugar");
        this.coffee = coffee;
    }

    @Override
    public int getPrice() {
        return coffee.getPrice() + 10;
    }

    @Override
    public Map<Ingredient, Integer> getRecipe() {
        Map<Ingredient, Integer> recipe =
                new HashMap<>(coffee.getRecipe());

        recipe.merge(Ingredient.SUGAR, 1, Integer::sum);

        return recipe;
    }

    @Override
    protected void addCondiments() {
        coffee.addCondiments();
        System.out.println("- Adding extra sugar.");
    }
}
