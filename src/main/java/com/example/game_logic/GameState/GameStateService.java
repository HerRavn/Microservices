package com.example.game_logic.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameStateService {
    private final GameStateRepo gameStateRepo;
    @Autowired
    public GameStateService(GameStateRepo gameStateRepo) {
        this.gameStateRepo = gameStateRepo;
    }
    public GameState saveGameState(GameState gameState) {
        return gameStateRepo.save(gameState);
    }

    public Optional<GameState> getGameStateById(Long gameId) {
        return gameStateRepo.findById(gameId);
    }

}
