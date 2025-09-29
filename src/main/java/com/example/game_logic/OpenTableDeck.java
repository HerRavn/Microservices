package com.example.game_logic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class OpenTableDeck {
    private final List<Card> tableDeck = Collections.synchronizedList(new ArrayList<>());

    public void addCard(Card card) {
        tableDeck.add(card);
    }

    public List<Card> getCards() {
        return new ArrayList<>(tableDeck); // defensive copy
    }
}


