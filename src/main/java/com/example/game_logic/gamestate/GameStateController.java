package com.example.game_logic.gamestate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameStateController {

    private final GameStateService gameStateService;

    public GameStateController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @PostMapping("/start")
    public GameState startGame() {
        // Initialize game: 4 cards per player
        return gameStateService.initializeGame(1, 4);
    }
}

