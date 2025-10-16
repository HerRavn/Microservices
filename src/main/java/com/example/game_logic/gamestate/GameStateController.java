// GameStateController.java
package com.example.game_logic.gamestate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*") // Adjust for your frontend URL in production
public class GameStateController {

    private final GameStateService gameStateService;

    public GameStateController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    /**
     * Start a new game
     * POST /api/game/start
     */
    @PostMapping("/start")
    public ResponseEntity<GameStateResponse> startGame() {
        GameState gameState = gameStateService.initializeGame();
        GameStateResponse response = gameStateService.getGameStateResponse(gameState.getGameId());
        return ResponseEntity.ok(response);
    }

    /**
     * Execute a player turn (draw and optionally swap)
     * POST /api/game/{gameId}/turn
     * Body: { "swap": true, "cardIndexToSwap": 2 }
     */
    @PostMapping("/{gameId}/turn")
    public ResponseEntity<GameStateResponse> playerTurn(
            @PathVariable Long gameId,
            @RequestBody PlayerTurnRequest request) {

        GameStateResponse response = gameStateService.executePlayerTurn(
                gameId,
                request.isSwap(),
                request.getCardIndexToSwap()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get current game state without making a move
     * GET /api/game/{gameId}
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateResponse> getGameState(@PathVariable Long gameId) {
        GameStateResponse response = gameStateService.getGameStateResponse(gameId);
        return ResponseEntity.ok(response);
    }

    /**
     * End and delete a game
     * DELETE /api/game/{gameId}
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> endGame(@PathVariable Long gameId) {
        gameStateService.endGame(gameId);
        return ResponseEntity.noContent().build();
    }
}
