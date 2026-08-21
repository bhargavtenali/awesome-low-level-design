package chessgame;

import chessgame.pieces.Piece;

import java.util.Scanner;

public class ChessGame {
    private final Board board;
    private Player whitePlayer, blackPlayer;
    private Player currentPlayer;

    public ChessGame() {
        board = new Board();
    }

    public void setPlayers(String playerWhiteName, String playerBlackName) {
        this.whitePlayer = new Player(playerWhiteName, Color.WHITE);
        this.blackPlayer = new Player(playerBlackName, Color.BLACK);
        this.currentPlayer = whitePlayer;
    }

    public void start() {
        // Game loop
        while (!isGameOver()) {
            Player player = currentPlayer;
            System.out.println(player.getName() + "'s turn.");
            // Get move from the player
            Move move = getPlayerMove(player);
            // Make the move on the board
            try {
                board.makeMove(move);
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
                System.out.println("Try again!");
                continue;
            }
            // Switch to the next player
            switchTurn();
        }
        // Display game result
        displayResult();
    }

    private void switchTurn() {
        currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
    }

    private boolean isGameOver() {
        return board.isCheckmate(whitePlayer.getColor()) || board.isCheckmate(blackPlayer.getColor()) ||
                board.isStalemate(whitePlayer.getColor()) || board.isStalemate(blackPlayer.getColor());
    }

    private Move getPlayerMove(Player player) {
        // For simplicity, let's assume the player enters the move via console input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();
        System.out.print("Enter start column: ");
        int startCol = scanner.nextInt();
        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();
        System.out.print("Enter end column: ");
        int endCol = scanner.nextInt();

        Piece piece = board.getPiece(startRow, startCol);
        if (piece == null || piece.getColor() != player.getColor()) {
            throw new IllegalArgumentException("Invalid piece selection!");
        }

        return new Move(board.getCell(startRow, startCol), board.getCell(endRow, endCol));
    }

    private void displayResult() {
        if (board.isCheckmate(Color.WHITE)) {
            System.out.println("Black wins by checkmate!");
        } else if (board.isCheckmate(Color.BLACK)) {
            System.out.println("White wins by checkmate!");
        } else if (board.isStalemate(Color.WHITE) || board.isStalemate(Color.BLACK)) {
            System.out.println("The game ends in a stalemate!");
        }
    }
}
