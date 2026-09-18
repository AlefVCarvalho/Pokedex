package br.uel.pokedex.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.service.PokemonService;

@Controller
public class CatalogController {

    private final PokemonService pokemonService;

    public CatalogController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        List<Pokemon> pokemons = pokemonService.listarTodos();

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
}
