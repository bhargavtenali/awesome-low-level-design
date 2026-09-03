package snakeandladdergame;

import snakeandladdergame.models.Board;
import snakeandladdergame.models.BoardEntity;
import snakeandladdergame.models.Dice;
import snakeandladdergame.models.Ladder;
import snakeandladdergame.models.Snake;

import java.util.List;

public class SnakeAndLadderDemo {
    public static void main(String[] args) {
        List<BoardEntity> boardEntities = List.of(
                new Snake(17, 7),
                new Snake(54, 34),
                new Snake(62, 19),
                new Snake(98, 79),
                new Ladder(3, 38),
                new Ladder(24, 33),
                new Ladder(42, 93),
                new Ladder(72, 84)
        );
        Board board = new Board(100, boardEntities);
        List<String> players = List.of("Alice", "Bob", "Charlie");
        Dice dice = new Dice(1, 6);
        Game game = new Game(board, players, dice);
        game.play();
    }
}