package br.uel.pokedex.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.uel.pokedex.model.Pokemon;

@Controller
public class CatalogController {

    private static final String ARTWORK_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        // Dados temporários enquanto a persistência ainda não está integrada às páginas.
        List<Pokemon> pokemons = List.of(
                pokemon(1, "Bulbasaur", "Grama", "Veneno"),
                pokemon(4, "Charmander", "Fogo", null),
                pokemon(7, "Squirtle", "Água", null),
                pokemon(25, "Pikachu", "Elétrico", null),
                pokemon(92, "Gastly", "Fantasma", "Veneno"),
                pokemon(133, "Eevee", "Normal", null),
                pokemon(143, "Snorlax", "Normal", null),
                pokemon(149, "Dragonite", "Dragão", "Voador"),
                pokemon(197, "Umbreon", "Sombrio", null),
                pokemon(282, "Gardevoir", "Psíquico", "Fada"),
                pokemon(448, "Lucario", "Lutador", "Aço")
        );

        List<String> tipos = pokemons.stream()
                .flatMap(pokemon -> Stream.of(pokemon.getTipoPrimario(), pokemon.getTipoSecundario()))
                .filter(tipo -> tipo != null)
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("tipos", tipos);

        return "catalogo";
    }

    private Pokemon pokemon(int numero, String nome, String tipoPrimario, String tipoSecundario) {
        return Pokemon.builder()
                .numeroPokedex(numero)
                .nome(nome)
                .tipoPrimario(tipoPrimario)
                .tipoSecundario(tipoSecundario)
                .imagemUrl(ARTWORK_URL + numero + ".png")
                .build();
    }
}
