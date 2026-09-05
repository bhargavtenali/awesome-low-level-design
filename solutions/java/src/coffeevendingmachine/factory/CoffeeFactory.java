package coffeevendingmachine.factory;

import coffeevendingmachine.coffee.Cappuccino;
import coffeevendingmachine.coffee.Coffee;
import coffeevendingmachine.coffee.Espresso;
import coffeevendingmachine.coffee.Latte;
import coffeevendingmachine.enums.CoffeeType;

public class CoffeeFactory {
    public static Coffee createCoffee(CoffeeType type) {
        switch (type) {
            case ESPRESSO:
                return new Espresso();
            case LATTE:
                return new Latte();
            case CAPPUCCINO:
                return new Cappuccino();
            default:
                throw new IllegalArgumentException("Unsupported coffee type: " + type);
        }
    }
}