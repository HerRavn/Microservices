package com.example.game_logic.gamestate;

import com.example.game_logic.card.Card;
import com.example.game_logic.card.CardService;
import com.example.game_logic.decks.Deck;
import com.example.game_logic.decks.DeckService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameStateService {

    private final CardService cardService;
    private final DeckService deckService;
    private final GameStateRepo gameStateRepo;

    public GameStateService(CardService cardService, DeckService deckService, GameStateRepo gameStateRepo) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.gameStateRepo = gameStateRepo;
    }

    public GameState initializeGame(int numPlayers, int cardsPerPlayer) {
        // 1. Make sure 52 cards exist
        cardService.initCards();

        // 2. Fetch all card IDs and shuffle
        List<Long> allCardIds = cardService.getAllCards().stream()
                .map(Card::getId)
                .toList();
        Collections.shuffle(allCardIds);

        // 3. Create main deck (start deck)
        Deck mainDeck = deckService.createDeck("Main Deck", new ArrayList<>(allCardIds));

        // 4. Create open/discard deck (empty)
        Deck openDeck = deckService.createDeck("Open Deck", new ArrayList<>());

        // 5. Create player decks and hands
        Deck playerDeck = deckService.createDeck("Player Deck", new ArrayList<>());
        Deck computerDeck = deckService.createDeck("Computer Deck", new ArrayList<>());

        Deck playerHand = deckService.createDeck("Player Hand", new ArrayList<>());
        Deck computerHand = deckService.createDeck("Computer Hand", new ArrayList<>());

        // 6. Deal cards to players (remove from main deck)
        for (int i = 0; i < cardsPerPlayer; i++) {
            if (!mainDeck.getCardIds().isEmpty()) {
                Long cardForPlayer = mainDeck.getCardIds().remove(0);
                playerHand.getCardIds().add(cardForPlayer);
            }
            if (!mainDeck.getCardIds().isEmpty()) {
                Long cardForComputer = mainDeck.getCardIds().remove(0);
                computerHand.getCardIds().add(cardForComputer);
            }
        }

        // Save updated decks after removing cards
        deckService.saveDeck(mainDeck);
        deckService.saveDeck(playerHand);
        deckService.saveDeck(computerHand);

        // 7. Create and save GameState
        GameState gameState = new GameState();
        gameState.setMainDeck(mainDeck);
        gameState.setOpenTableDeck(openDeck);
        gameState.setPlayerDeck(playerDeck);
        gameState.setComputerDeck(computerDeck);
        gameState.setPlayerHand(playerHand);
        gameState.setComputerHand(computerHand);
        gameState.setPlayerScore(0);
        gameState.setComputerScore(0);
        gameState.setRoundNumber(1);
        gameState.setGameOver(false);

        return gameStateRepo.save(gameState);
    }
}
