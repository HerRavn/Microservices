package com.example.game_logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/drawCard")
public class DrawCardController {

    private final DrawCardService drawCardService;
    private final FetchDeck fetchDeck;

    @Autowired
    public DrawCardController(DrawCardService drawCardService, FetchDeck fetchDeck) {
        this.drawCardService = drawCardService;
        this.fetchDeck = fetchDeck;
    }

    @GetMapping
    public Card drawCard() {
        List<Card> deck = fetchDeck.fetchDeck();
        return drawCardService.drawCard(deck);
    }
}

