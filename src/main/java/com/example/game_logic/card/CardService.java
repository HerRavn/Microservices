package com.example.game_logic.card;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CardService {
    private final CardRepo cardRepo;
    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    private static final Map<Integer, String> RANKS = Map.ofEntries(
            Map.entry(1, "ace"),
            Map.entry(11, "jack"),
            Map.entry(12, "queen"),
            Map.entry(13, "king")
    );


    public void initCards() {
        if (cardRepo.count() == 52) return; // Already initialized

        List<Card> cards = new ArrayList<>();
        for (Suite suite : Suite.values()) {
            for (int value = 1; value <= 13; value++) {
                Card card = new Card();
                card.setValue(value);
                card.setSuite(suite);
                String rank = RANKS.getOrDefault(value, String.valueOf(value));
                card.setFilename(rank + "_of_" + suite.name().toLowerCase() + ".svg");

                cards.add(card);
            }
        }
        cardRepo.saveAll(cards);
    }

    public List<Card> getAllCards(){
        return cardRepo.findAll();
    }

}
