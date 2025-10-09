package com.example.game_logic.gamestate;

import com.example.game_logic.decks.Deck;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "main_deck_id")
    private Deck mainDeck;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "open_deck_id")
    private Deck openTableDeck;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_deck_id")
    private Deck playerDeck;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "computer_deck_id")
    private Deck computerDeck;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_hand_id")
    private Deck playerHand;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "computer_hand_id")
    private Deck computerHand;

    @Column(name = "player_score")
    private int playerScore;

    @Column(name = "computer_score")
    private int computerScore;

    @Column(name = "round_number")
    private int roundNumber;

    @Column(name = "is_game_over")
    private boolean isGameOver;
}
