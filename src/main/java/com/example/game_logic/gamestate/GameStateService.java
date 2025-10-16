package com.example.game_logic.gamestate;

import com.example.game_logic.card.Card;
import com.example.game_logic.card.CardService;
import com.example.game_logic.decks.Deck;
import com.example.game_logic.decks.DeckService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameStateService {

    private final CardService cardService;
    private final DeckService deckService;
    private final GameStateRepo gameStateRepo;
    private final Random random = new Random();

    public GameStateService(CardService cardService, DeckService deckService, GameStateRepo gameStateRepo) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.gameStateRepo = gameStateRepo;
    }

    /**
     * Initialize a new game with shuffled deck and dealt hands
     */
    public GameState initializeGame() {
        // Create card IDs for a full deck
        List<Long> cardIds = new ArrayList<>();
        for (long i = 1; i <= 52; i++) {
            cardIds.add(i);
        }

        // Create and shuffle the main deck
        Deck mainDeck = deckService.createDeck("mainDeck", cardIds);
        deckService.shuffleDeck(mainDeck.getDeckId());

        // Create hands - drawCards already removes from deck
        List<Long> playerCardIds = deckService.drawCards(mainDeck.getDeckId(), 4);
        Deck playerHand = deckService.createDeck("playerHand", playerCardIds);

        List<Long> computerCardIds = deckService.drawCards(mainDeck.getDeckId(), 4);
        Deck computerHand = deckService.createDeck("computerHand", computerCardIds);

        // Create empty open table deck
        Deck openTableDeck = deckService.createDeck("openTableDeck", new ArrayList<>());

        // Create and save game state
        GameState gameState = new GameState();
        gameState.setMainDeck(mainDeck);
        gameState.setPlayerHand(playerHand);
        gameState.setComputerHand(computerHand);
        gameState.setOpenTableDeck(openTableDeck);
        gameState.setPlayerScore(0);
        gameState.setComputerScore(0);
        gameState.setRoundNumber(1);
        gameState.setGameOver(false);

        return gameStateRepo.save(gameState);
    }

    /**
     * Execute a complete turn: player draws, decides to swap, computer takes turn
     */
    public GameStateResponse executePlayerTurn(Long gameId, boolean playerSwaps, Integer cardIndexToSwap) {
        GameState gameState = gameStateRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        if (gameState.isGameOver()) {
            return buildResponse(gameState, null, "Game is already over!");
        }

        // Check if main deck has cards
        Deck mainDeck = deckService.getDeck(gameState.getMainDeck().getDeckId());
        if (mainDeck.getCardIds().isEmpty()) {
            gameState.setGameOver(true);
            gameStateRepo.save(gameState);
            return buildResponse(gameState, null, "Game over - no more cards in deck!");
        }

        // Player draws a card
        List<Long> drawnCardIds = deckService.drawCards(mainDeck.getDeckId(), 1);
        if (drawnCardIds.isEmpty()) {
            gameState.setGameOver(true);
            gameStateRepo.save(gameState);
            return buildResponse(gameState, null, "Game over - no more cards in deck!");
        }

        Card drawnCard = cardService.getCardById(drawnCardIds.get(0));
        Deck playerHand = deckService.getDeck(gameState.getPlayerHand().getDeckId());
        Deck openTableDeck = deckService.getDeck(gameState.getOpenTableDeck().getDeckId());

        String message = "Player drew " + drawnCard.getValue() + " of " + drawnCard.getSuite() + ". ";

        // Handle player's swap decision
        if (playerSwaps && cardIndexToSwap != null) {
            if (cardIndexToSwap >= 0 && cardIndexToSwap < playerHand.getCards().size()) {
                Card swappedCard = deckService.getCardFromDeck(playerHand.getDeckId(), cardIndexToSwap);
                message += "Swapped out: " + swappedCard.getValue() + " of " + swappedCard.getSuite() + ". ";

                // Move swapped card to open table
                deckService.removeDeckCards(playerHand.getDeckId(), swappedCard.getId());
                deckService.addCardToDeck(openTableDeck.getDeckId(), swappedCard);

                // Add drawn card to player hand
                deckService.addCardToDeck(playerHand.getDeckId(), drawnCard);
            } else {
                message += "Invalid swap index! Card discarded. ";
                deckService.addCardToDeck(openTableDeck.getDeckId(), drawnCard);
            }
        } else {
            message += "Card discarded to open table. ";
            deckService.addCardToDeck(openTableDeck.getDeckId(), drawnCard);
        }

        // Computer's turn
        message += executeComputerTurn(gameState);

        // Increment round number
        gameState.setRoundNumber(gameState.getRoundNumber() + 1);
        gameStateRepo.save(gameState);

        // Refresh all decks for response
        gameState = gameStateRepo.findById(gameId).orElseThrow();

        return buildResponse(gameState, drawnCard, message);
    }

    /**
     * Computer AI logic: draws a card and swaps if drawn card is lower
     */
    private String executeComputerTurn(GameState gameState) {
        Deck mainDeck = deckService.getDeck(gameState.getMainDeck().getDeckId());

        if (mainDeck.getCardIds().isEmpty()) {
            return "Computer cannot draw - deck empty.";
        }

        List<Long> computerDrawnIds = deckService.drawCards(mainDeck.getDeckId(), 1);
        if (computerDrawnIds.isEmpty()) {
            return "Computer cannot draw - deck empty.";
        }

        Card computersCard = cardService.getCardById(computerDrawnIds.get(0));
        Deck computerHand = deckService.getDeck(gameState.getComputerHand().getDeckId());
        Deck openTableDeck = deckService.getDeck(gameState.getOpenTableDeck().getDeckId());

        String computerMessage = "Computer drew " + computersCard.getValue() + " of " + computersCard.getSuite() + ". ";

        // Computer logic: swap if drawn card is lower than any card in hand
        boolean swapped = false;
        for (Card card : computerHand.getCards()) {
            if (computersCard.getValue() < card.getValue()) {
                deckService.removeDeckCards(computerHand.getDeckId(), card.getId());
                deckService.addCardToDeck(openTableDeck.getDeckId(), card);
                deckService.addCardToDeck(computerHand.getDeckId(), computersCard);
                computerMessage += "Computer swapped out " + card.getValue() + " of " + card.getSuite() + ".";
                swapped = true;
                break;
            }
        }

        if (!swapped) {
            deckService.addCardToDeck(openTableDeck.getDeckId(), computersCard);
            computerMessage += "Computer discarded the card.";
        }

        return computerMessage;
    }

    /**
     * Get current game state without making any moves
     */
    public GameStateResponse getGameStateResponse(Long gameId) {
        GameState gameState = gameStateRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
        return buildResponse(gameState, null, "Current game state");
    }

    /**
     * End the game and clean up
     */
    public void endGame(Long gameId) {
        gameStateRepo.deleteById(gameId);
    }

    /**
     * Build the response DTO
     */
    private GameStateResponse buildResponse(GameState gameState, Card drawnCard, String message) {
        Deck playerHand = deckService.getDeck(gameState.getPlayerHand().getDeckId());
        Deck computerHand = deckService.getDeck(gameState.getComputerHand().getDeckId());
        Deck mainDeck = deckService.getDeck(gameState.getMainDeck().getDeckId());
        Deck openTableDeck = deckService.getDeck(gameState.getOpenTableDeck().getDeckId());

        GameStateResponse response = new GameStateResponse();
        response.setGameId(gameState.getGameId());
        response.setPlayerHand(playerHand.getCards());
        response.setComputerHandSize(computerHand.getCards().size()); // Hide computer's actual cards
        response.setDrawnCard(drawnCard);
        response.setMainDeckSize(mainDeck.getCardIds().size());
        response.setOpenTableSize(openTableDeck.getCardIds().size());
        response.setRoundNumber(gameState.getRoundNumber());
        response.setGameOver(gameState.isGameOver());
        response.setMessage(message);

        return response;
    }
}
