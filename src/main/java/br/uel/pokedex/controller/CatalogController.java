package br.uel.pokedex.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.uel.pokedex.model.CatalogPokemon;

@Controller
public class CatalogController {

    private static final String ARTWORK = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";

    // TODO: Alterar quando adidiconar o banco de dados
    @GetMapping("/catalogo")
    public String catalog(Model model) {
        List<CatalogPokemon> pokemons = List.of(
                pokemon(1, "Bulbasaur", "Grama", "Veneno", 45, 49, 49, 65, 65, 45),
                pokemon(4, "Charmander", "Fogo", null, 39, 52, 43, 60, 50, 65),
                pokemon(7, "Squirtle", "Água", null, 44, 48, 65, 50, 64, 43),
                pokemon(25, "Pikachu", "Elétrico", null, 35, 55, 40, 50, 50, 90),
                pokemon(92, "Gastly", "Fantasma", "Veneno", 30, 35, 30, 100, 35, 80),
                pokemon(133, "Eevee", "Normal", null, 55, 55, 50, 45, 65, 55),
                pokemon(143, "Snorlax", "Normal", null, 160, 110, 65, 65, 110, 30),
                pokemon(149, "Dragonite", "Dragão", "Voador", 91, 134, 95, 100, 100, 80),
                pokemon(197, "Umbreon", "Sombrio", null, 95, 65, 110, 60, 130, 65),
                pokemon(282, "Gardevoir", "Psíquico", "Fada", 68, 65, 65, 125, 115, 80),
                pokemon(448, "Lucario", "Lutador", "Aço", 70, 110, 70, 115, 70, 90)
        );
        model.addAttribute("pokemons", pokemons);
        model.addAttribute("tipos", pokemons.stream()
                .flatMap(pokemon -> Stream.of(pokemon.tipoPrimario(), pokemon.tipoSecundario()))
                .filter(tipo -> tipo != null)
                .distinct()
                .toList());
        return "catalogo";
    }

    private CatalogPokemon pokemon(int numero, String nome, String tipoPrimario, String tipoSecundario,
                                   int hp, int ataque, int defesa, int ataqueEspecial,
                                   int defesaEspecial, int velocidade) {
        return new CatalogPokemon(numero, nome, tipoPrimario, tipoSecundario,
                ARTWORK + numero + ".png",
                Map.of("HP", hp, "Ataque", ataque, "Defesa", defesa,
                        "Ataque Esp.", ataqueEspecial, "Defesa Esp.", defesaEspecial,
                        "Velocidade", velocidade));
    }
}