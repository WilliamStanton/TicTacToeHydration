package billy.tictactoeweb2.modal;

import lombok.Getter;
import tictactoelib.Player;
import tictactoelib.board.Board;
import tictactoelib.game.GameStatus;

/**
 * Represents successful move
 */
@Getter
public class MoveResponse {
    private final int id;
    private final Player nextPlayer;
    private final Board board;
    private final GameStatus gameStatus;

    public MoveResponse(int id, Player nextPlayer, Board board, GameStatus gameStatus) {
        this.id = id;
        this.nextPlayer = nextPlayer;
        this.board = board;
        this.gameStatus = gameStatus;
    }
}
