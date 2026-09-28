package br.uel.pokedex.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokeApiService {

    private final RestClient client = RestClient.builder()
            .baseUrl("https://pokeapi.co/api/v2")
            .build();

    public List<PokeApiPokemon> listarDestaques() {
        List<PokeApiPokemon> pokemons = new ArrayList<>();
        for (int id = 1; id <= 30; id++) {
            try {
                pokemons.add(detalhar(id));
            } catch (RuntimeException ignored) {
                return pokemons;
            }
        }
        return pokemons;
    }

    private PokeApiPokemon detalhar(int id) {
        JsonNode data = client.get().uri("/pokemon/{id}", id).retrieve().body(JsonNode.class);
        JsonNode types = data.path("types");
        String primary = types.size() > 0 ? types.get(0).path("type").path("name").asText() : "normal";
        String secondary = types.size() > 1 ? types.get(1).path("type").path("name").asText() : "";
        String name = data.path("name").asText();
        String image = data.path("sprites").path("other").path("official-artwork").path("front_default").asText();
        return new PokeApiPokemon(id, name, primary, secondary, image);
    }

    public record PokeApiPokemon(int id, String nome, String tipoPrimario,
            String tipoSecundario, String imagemUrl) {
    }
}
