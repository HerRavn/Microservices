package com.example.game_logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameLogicApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameLogicApplication.class, args);
/*
        // test to fetch a deck, draw a card, remove it from the main deck and add it to the opentabledeck

            // Create the fetcher
            FetchDeck fetchDeck = new FetchDeck();
            // Call the method that gets the deck from your Spring Boot API
            List<Card> shuffledDeck = fetchDeck.fetchDeck();
            // Call the method that draws a card from the deck
            DrawCardService drawCardService = new DrawCardService();
            Card card = drawCardService.drawCard(shuffledDeck);
            System.out.println("You drew a " + card.getValue() + " of " + card.getSuite());

            //remove card
            shuffledDeck.remove(card);
            for(Card card1 : shuffledDeck){
                System.out.println(card1.getValue() + " of " + card1.getSuite());
            }

            //add to tabledeck
            List<Card> openTableDeck = new ArrayList<>(); // ✅ mutable list
            openTableDeck.add(0, card); // add at start of list

            System.out.println("\nOpen table deck:");
            for (Card c : openTableDeck) {
                System.out.println(c.getValue() + " of " + c.getSuite());
            }
*/
        }
	}

