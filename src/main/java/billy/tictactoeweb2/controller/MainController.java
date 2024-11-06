package billy.tictactoeweb2.controller;

import billy.tictactoeweb2.modal.MoveResponse;
import billy.tictactoeweb2.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tictactoelib.game.ComputerGame;
import tictactoelib.game.GameException;

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

    @PostMapping("/next")
    public ResponseEntity<?> next(@RequestParam int id) throws GameException {
        var game = gameService.getGame();
        var status = game.getStatus();
        try {
            game.next(id);
        } catch (GameException e) {
            // print error
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(new MoveResponse(id, gameService.getGame().getBoardSpot(id).getPlayer(), gameService.getGame().getNextPlayer(), status != game.getStatus() || game instanceof ComputerGame));
    }

    /**
     * Restart game
     * @return new game
     */
    @PostMapping("/restart")
    public String restartGame() {
        gameService.restartGame(false);
        return "redirect:/";
    }

    @PostMapping("/settings")
    public String settings() {
        gameService.restartGame(true);
        return "redirect:/";
    }
}
