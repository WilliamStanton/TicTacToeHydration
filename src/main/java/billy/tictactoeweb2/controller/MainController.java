package billy.tictactoeweb2.controller;

import billy.tictactoeweb2.modal.MoveResponse;
import billy.tictactoeweb2.service.GameService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("")
    public String game(Model model){
        model.addAttribute("game", gameService.getGame());
        return "game";
    }

    @GetMapping("/next")
    @ResponseBody
    public ResponseEntity<?> nextPlayer() throws GameException {
        if (gameService.getGame().getStatus() == GameStatus.INCOMPLETE)
            return ResponseEntity.ok(gameService.getGame().getNextPlayer());
        else
            return ResponseEntity.badRequest().body("Game concluded");
    }

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
    @PostMapping(path = {"/restart", "settings"})
    public String restartGame(HttpServletRequest req) {
        gameService.resetGame(req.getRequestURI().contains("settings"));
        return "redirect:/";
    }

    @PostMapping("/board")
    @ResponseBody
    public Board getBoard() {
        return gameService.getGame().getBoard();
    }

    @GetMapping("/winner")
    @ResponseBody
    public ResponseEntity<?> getWinner() throws GameException {
        if (gameService.getGame().getStatus() == GameStatus.WON)
            return ResponseEntity.ok(gameService.getGame().getWinner());
        else
            return ResponseEntity.badRequest().body("No winner");
    }
}
