package com.example.game_logic;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DrawCardService {
    Card card;

    public Card drawCard(List<Card> deck) {
        card = deck.getFirst();
        System.out.println("Drawing a card...");
        return card;
    }

}
