package billy.tictactoeweb2.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tictactoelib.game.ComputerGame;
import tictactoelib.game.Game;
import tictactoelib.game.GameException;
import tictactoelib.game.PlayerGame;

@Getter
@Service
public class GameService {
    private Game game;

    public GameService(@Qualifier("player") Game game) {
        this.game = game;
    }

    /**
     * Restarts the game
     * @param toggle switch modes
     */
    public void resetGame(boolean toggle) {
        game = toggle ? game instanceof PlayerGame ? new ComputerGame() : new PlayerGame() : game instanceof PlayerGame ? new PlayerGame() : new ComputerGame();
    }
}
