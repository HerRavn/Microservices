package com.example.game_logic.GameState;

import com.example.game_logic.Card;
import com.example.game_logic.GameState.GameState;
import com.example.game_logic.GameState.GameStateRepo;
import com.example.game_logic.Suite;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class TestData {

    @Bean
    CommandLineRunner initDatabase(GameStateRepo gameStateRepo) {
        return args -> {
            // Create some cards
            Card card1 = new Card(null, 1, Suite.HEARTS, "hearts_1.png");
            Card card2 = new Card(null, 13, Suite.SPADES, "spades_king.png");
            Card card3 = new Card(null, 7, Suite.DIAMONDS, "diamonds_7.png");
            Card card4 = new Card(null, 10, Suite.CLUBS, "clubs_10.png");

            // Create a game state with cards
            GameState gameState = new GameState(1L);
            gameState.setTableDeck(Arrays.asList(card1, card2));
            gameState.setOpenTableDeck(Arrays.asList(card3));
            gameState.setPlayer1Hand(Arrays.asList(card4));
            gameState.setPlayer2Hand(Arrays.asList());

            // Save to DB
            gameStateRepo.save(gameState);

            System.out.println("✅ Saved GameState with ID " + gameState.getGameId());
        };
    }
}

