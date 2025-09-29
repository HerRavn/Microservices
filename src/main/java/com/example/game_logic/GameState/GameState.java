package com.example.game_logic.GameState;

import com.example.game_logic.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.OneToMany;


import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class GameState {
    @Id
    @Column(name = "game_id")
    private Long gameId;
    private int round;
    @Column(name = "player_1_score")
    private int player1Score;
    @Column(name = "player_2_score")
    private int player2Score;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @Column(name = "table_deck")
    private List<Card> tableDeck;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_state_table_deck",
            joinColumns = @JoinColumn(name = "game_state_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "open_table_deck")
    private List<Card> openTableDeck;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_state_table_deck",
            joinColumns = @JoinColumn(name = "game_state_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "player_1_hand")
    private List<Card> player1Hand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_state_table_deck",
            joinColumns = @JoinColumn(name = "game_state_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "player_2_hand")
    private List<Card> player2Hand;
    @Column(name = "game_active")
    private boolean gameActive;

    public GameState(Long gameId) {
        this.gameId = gameId;
        this.round = 1;
        this.player1Score = 0;
        this.player2Score = 0;
        this.tableDeck = null;
        this.openTableDeck = null;
        this.player1Hand = null;
        this.player2Hand = null;
        this.gameActive = true;
    }

}
