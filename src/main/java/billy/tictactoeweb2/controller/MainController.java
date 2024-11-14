package billy.tictactoeweb2.controller;

import billy.tictactoeweb2.modal.MoveResponse;
import billy.tictactoeweb2.service.GameService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tictactoelib.Player;
import tictactoelib.board.Board;
import tictactoelib.game.ComputerGame;
import tictactoelib.game.GameException;
import tictactoelib.game.GameStatus;

@Controller
public class MainController {
    private final GameService gameService;

    public MainController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Game Home
     */
    @GetMapping("")
    public String game(Model model){
        model.addAttribute("game", gameService.getGame());
        return "game";
    }

    /**
     * Gets next player
     * @return returns next player, otherwise 400 - game concluded
     */
    @GetMapping("/next")
    @ResponseBody
    public ResponseEntity<?> nextPlayer() throws GameException {
        if (gameService.getGame().getStatus() == GameStatus.INCOMPLETE) {
            System.out.println(gameService.getGame().getNextPlayer());
            return ResponseEntity.ok(gameService.getGame().getNextPlayer());
        }
        else
            return ResponseEntity.badRequest().body("Game concluded");
    }

    /**
     * Makes next move
     * @param id spot id
     * @return move response
     * @throws GameException move not completed
     */
    @PostMapping("/next")
    public ResponseEntity<?> next(@RequestParam int id) throws GameException {
        var game = gameService.getGame();
        try {
            game.next(id);
        } catch (GameException e) {
            // print error
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(new MoveResponse(id, game.getStatus() == GameStatus.INCOMPLETE ? game.getNextPlayer() : null, game.getBoard(), game.getStatus()));
    }

    /**
     * Restart game/switch mode
     * @return new game
     */
    @PatchMapping(path = {"/restart", "settings"})
    public ResponseEntity<?> restartGame(HttpServletRequest req) {
        gameService.resetGame(req.getRequestURI().contains("settings"));
        return ResponseEntity.ok().build();
    }

    /**
     * Gets game winner
     * @return game winner
     */
    @GetMapping("/winner")
    @ResponseBody
    public ResponseEntity<?> getWinner() {
        if (gameService.getGame().getStatus() == GameStatus.WON)
            return ResponseEntity.ok(gameService.getGame().getBoard().getWinningSpots());
        else
            return ResponseEntity.badRequest().body("No winner");
    }
}
