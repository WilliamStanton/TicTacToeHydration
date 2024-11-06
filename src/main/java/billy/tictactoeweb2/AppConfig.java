package billy.tictactoeweb2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.SessionScope;
import tictactoelib.Player;
import tictactoelib.game.ComputerGame;
import tictactoelib.game.Game;
import tictactoelib.game.PlayerGame;

@Configuration
public class AppConfig {

    @Bean
    @SessionScope
    @Qualifier("player")
    public PlayerGame getPlayerGame() {
        return new PlayerGame();
    }

    @Bean
    @SessionScope
    @Qualifier("computer")
    public ComputerGame getComputerGame() {
        return new ComputerGame();
    }
}
