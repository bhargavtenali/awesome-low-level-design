package snakeandladdergame;

import snakeandladdergame.models.Board;
import snakeandladdergame.models.Dice;
import snakeandladdergame.models.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private int currentPlayerIndex;
    private boolean gameOver;
    private Player winner;

    public Game(Board board, List<String> playerNames, Dice dice) {
        if (board == null || dice == null) {
            throw new IllegalArgumentException("Board and dice are required.");
        }

        if (playerNames == null || playerNames.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required.");
        }

        this.board = board;
        this.dice = dice;
        this.players = new ArrayList<>();

        for (String playerName : playerNames) {
            if (playerName == null || playerName.isBlank()) {
                throw new IllegalArgumentException("Player name cannot be empty.");
            }

            players.add(new Player(playerName));
        }
    }

    public synchronized void play() {
        System.out.println("Game started!");
        while (!gameOver) {
            Player currentPlayer = players.get(currentPlayerIndex);
            boolean extraTurn = takeTurn(currentPlayer);
            if (!gameOver && !extraTurn) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
        System.out.println("Game Finished!");
        System.out.printf("The winner is %s!%n", winner.getName());
    }

    private boolean takeTurn(Player player) {
        int roll = dice.roll();
        System.out.printf("%n%s's turn. Rolled a %d.%n", player.getName(), roll);
        int currentPosition = player.getPosition();
        int finalPosition = board.getFinalPosition(currentPosition + roll);
        if (finalPosition > board.getSize()) {
            System.out.printf("Oops, %s needs to land exactly on %d. Turn skipped.%n", player.getName(), board.getSize());
            return false;
        }
        player.setPosition(finalPosition);
        if (finalPosition == board.getSize()) {
            winner = player;
            gameOver = true;
            System.out.printf("Hooray! %s reached the final square %d and won!%n", player.getName(), board.getSize());
            return false;
        }
        if (roll == 6) {
            System.out.printf("%s rolled a 6 and gets another turn!%n", player.getName());
            return true;
        }
        return false;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}