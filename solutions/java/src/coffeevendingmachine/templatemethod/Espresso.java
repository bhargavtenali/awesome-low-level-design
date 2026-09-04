package coffeevendingmachine.templatemethod;

import coffeevendingmachine.decorator.Coffee;
import coffeevendingmachine.enums.Ingredient;

import java.util.Map;

public class Espresso extends Coffee {

    public Espresso() {
        super("Espresso");
    }

    @Override
    protected void addCondiments() {
    }

    @Override
    public int getPrice() {
        return 150;
    }

    @Override
    public Map<Ingredient, Integer> getRecipe() {
        return Map.of(
                Ingredient.COFFEE_BEANS, 7,
                Ingredient.WATER, 30);
    }
}