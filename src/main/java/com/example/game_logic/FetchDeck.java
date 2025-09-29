package com.example.game_logic;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class FetchDeck {
    private static final String URL = "http://localhost:8001/api/deck/getDeck";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Card> fetchDeck() {
        ResponseEntity<List<Card>> response =
                restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        return response.getBody();
    }
}
