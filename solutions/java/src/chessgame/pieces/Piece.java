package chessgame.pieces;

import chessgame.Board;
import chessgame.Cell;
import chessgame.Color;

public abstract class Piece {
    protected final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public abstract boolean canMove(Board board, Cell start, Cell end);

    public Color getColor() {
        return color;
    }
}
