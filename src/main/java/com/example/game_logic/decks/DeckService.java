package com.example.game_logic.decks;

import com.example.game_logic.card.Card;
import com.example.game_logic.card.CardRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DeckService {
    private final DeckRepo deckRepo;
    private final CardRepo cardRepo;
    private final Random random = new Random();

    public DeckService(DeckRepo deckRepo, CardRepo cardRepo) {
        this.deckRepo = deckRepo;
        this.cardRepo = cardRepo;
    }

    public Deck createDeck(String deckName, List<Long> cardIds) {
        Deck deck = new Deck();
        deck.setDeckName(deckName);
        deck.setCardIds(cardIds);
        return deckRepo.save(deck);
    }
    public Deck saveDeck(Deck deck) {
        return deckRepo.save(deck);
    }



    public Deck getDeck(Long id) {
        return deckRepo.findById(id).orElse(null);
    }

    public Card drawCard(Long deckId){
        Optional<Deck> optionalDeck = deckRepo.findById((deckId));
        if(optionalDeck.isEmpty()){return null;}

        Deck deck = optionalDeck.get();
        List<Long> cardIds = deck.getCardIds();
        if (cardIds.isEmpty()) return null; // No cards left

        int index = random.nextInt(cardIds.size());
        Long drawnCardId = cardIds.remove(index); // remove from deck

        deckRepo.save(deck); // save updated deck

        return cardRepo.findById(drawnCardId).orElse(null);

    }

    public String addCardById(Card card, Long deckId) {
        Optional<Deck> optionalDeck = deckRepo.findById(deckId);
        if (optionalDeck.isEmpty()) {
            return "Deck not found";
        }

        Deck deck = optionalDeck.get();

        List<Long> cardIds = deck.getCardIds();
        if (cardIds == null) {
            cardIds = new ArrayList<>();
            deck.setCardIds(cardIds);
        }

        cardIds.add(card.getId()); // add the card ID to the deck

        deckRepo.save(deck); // persist changes

        return "Card " + card.getId() + " added to deck " + deck.getDeckName();
    }

}

