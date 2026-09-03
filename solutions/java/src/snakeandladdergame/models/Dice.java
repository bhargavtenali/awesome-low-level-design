package snakeandladdergame.models;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private final int minValue;
    private final int maxValue;

    public Dice(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("Minimum value cannot exceed maximum value.");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int roll() {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
    }
}