package chessgame.pieces;

import chessgame.Board;
import chessgame.Cell;
import chessgame.Color;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int rowDiff = end.getRow() - start.getRow();
        int colDiff = Math.abs(end.getCol() - start.getCol());

        if (color == Color.WHITE) {
            return (rowDiff == 1 && colDiff == 0) ||
                    (start.getRow() == 1 && rowDiff == 2 && colDiff == 0) ||
                    (rowDiff == 1 && colDiff == 1 && board.getPiece(end.getRow(), end.getCol()) != null);
        } else {
            return (rowDiff == -1 && colDiff == 0) ||
                    (start.getRow() == 6 && rowDiff == -2 && colDiff == 0) ||
                    (rowDiff == -1 && colDiff == 1 && board.getPiece(end.getRow(), end.getCol()) != null);
        }
    }
}
