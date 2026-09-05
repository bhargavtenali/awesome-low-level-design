package coffeevendingmachine.coffee;

import coffeevendingmachine.enums.Ingredient;

import java.util.Map;

public abstract class Coffee {
    private final String coffeeType;

    protected Coffee(String coffeeType) {
        this.coffeeType = coffeeType;
    }

    public final void prepare() {
        System.out.println("\nPreparing your " + coffeeType + "...");
        grindBeans();
        brew();
        addCondiments();
        pourIntoCup();
        System.out.println(coffeeType + " is ready!");
    }

    private void grindBeans() {
        System.out.println("- Grinding fresh coffee beans.");
    }

    private void brew() {
        System.out.println("- Brewing coffee with hot water.");
    }

    private void pourIntoCup() {
        System.out.println("- Pouring into a cup.");
    }

    public abstract void addCondiments();

    public abstract int getPrice();

    public abstract Map<Ingredient, Integer> getRecipe();

    public String getCoffeeType() {
        return coffeeType;
    }
}