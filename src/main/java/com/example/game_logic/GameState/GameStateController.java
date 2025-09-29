package com.example.game_logic.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/gameState")
public class GameStateController {
    private final GameStateService gameStateService;
    @Autowired
    public GameStateController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @PostMapping("/setState")
    public ResponseEntity<GameState> setState(@RequestParam Long gameId) {
        GameState saved = gameStateService.saveGameState(new GameState(gameId));
        return ResponseEntity.ok(saved);
    }

    // GET endpoint to fetch GameState by ID
    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGameState(@PathVariable Long gameId) {
        Optional<GameState> gameStateOpt = gameStateService.getGameStateById(gameId);
        return gameStateOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
