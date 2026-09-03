package snakeandladdergame.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int size;
    private final Map<Integer, Integer> snakesAndLadders;

    public Board(int size, List<BoardEntity> entities) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be at least 2.");
        }
        this.size = size;
        Map<Integer, Integer> jumps = new HashMap<>();
        if (entities != null) {
            for (BoardEntity entity : entities) {
                validateEntity(entity);
                if (jumps.put(entity.getStart(), entity.getEnd()) != null) {
                    throw new IllegalArgumentException("Multiple entities cannot start at the same position: " + entity.getStart());
                }
            }
        }
        this.snakesAndLadders = Map.copyOf(jumps);
    }

    private void validateEntity(BoardEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Board entity cannot be null.");
        }
        if (entity.getStart() < 1 || entity.getStart() >= size) {
            throw new IllegalArgumentException("Entity start must be between 1 and " + (size - 1));
        }
        if (entity.getEnd() < 1 || entity.getEnd() > size) {
            throw new IllegalArgumentException("Entity end must be between 1 and " + size);
        }
    }

    public int getSize() {
        return size;
    }

    public int getFinalPosition(int position) {
        return snakesAndLadders.getOrDefault(position, position);
    }
}