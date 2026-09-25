/*
 * Controller da página inicial.
 * Carrega os Pokémon do banco e envia os destaques e estatísticas para a Home.
 */
package br.uel.pokedex.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.service.PokemonService;

@Controller
public class HomeController {

    private final PokemonService pokemonService;

    public HomeController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Pokemon> pokemons = pokemonService.listarTodos();

        long totalTipos = pokemons.stream()
                .flatMap(pokemon -> Stream.of(
                        pokemon.getTipoPrimario(), pokemon.getTipoSecundario()))
                .filter(tipo -> tipo != null)
                .distinct()
                .count();

        model.addAttribute("totalPokemons", pokemons.size());
        model.addAttribute("totalTipos", totalTipos);
        model.addAttribute("totalRegioes", 9);
        model.addAttribute("destaques", pokemons.stream().limit(3).toList());

        return "home";
    }
}
