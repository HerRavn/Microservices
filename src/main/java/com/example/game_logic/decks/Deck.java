package com.example.game_logic.decks;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Long deckId;

    @Column(name = "deck_name")
    private String deckName;

    // Store only card IDs, not full card entities
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "deck_card_ids",
            joinColumns = @JoinColumn(name = "deck_id")
    )
    @Column(name = "card_id")
    private List<Long> cardIds;
}
