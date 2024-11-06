package billy.tictactoeweb2.modal;

import lombok.Getter;
import tictactoelib.Player;

/**
 * Represents successful move
 */
@Getter
public class MoveResponse {
    private final int id;
    private final Player player;
    private final Player nextPlayer;
    private final boolean statusChange;

    public MoveResponse(int id, Player player, Player nextPlayer, boolean statusChange) {
        this.id = id;
        this.player = player;
        this.nextPlayer = nextPlayer;
        this.statusChange = statusChange;
    }

}
