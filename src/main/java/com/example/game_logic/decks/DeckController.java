package com.example.game_logic.decks;

import com.example.game_logic.card.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decks")
public class DeckController {
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }
    @PostMapping
    public ResponseEntity<Deck> createDeck(@RequestBody DeckDTO request) {
        System.out.println("Received deck name: " + request.deckName());
        System.out.println("Received card IDs: " + request.cardIds());
        Deck deck = deckService.createDeck(request.deckName(), request.cardIds());
        return ResponseEntity.ok(deck);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Deck> getDeck(@PathVariable Long id) {
        Deck deck = deckService.getDeck(id);
        return deck != null ? ResponseEntity.ok(deck) : ResponseEntity.notFound().build();
    }
    @PostMapping("/save")
    public ResponseEntity<Deck> saveDeck(@RequestBody Deck deck) {
        return ResponseEntity.ok(deckService.saveDeck(deck));
    }

    @PostMapping("/{id}/draw")
    public ResponseEntity<Card> drawCard(@PathVariable Long id) {
        Card drawnCard = deckService.drawCard(id);
        if (drawnCard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drawnCard);
    }

}

