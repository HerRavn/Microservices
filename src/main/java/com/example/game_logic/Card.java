package com.example.game_logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private Long id;
    private int value;
    private Suite suite;
    private String filename;

}
