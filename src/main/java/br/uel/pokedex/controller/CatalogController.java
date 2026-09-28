/*
 * Controla a tela de catálogo e as ações do CRUD.
 * Aqui ficam as rotas para listar, cadastrar, editar e excluir Pokémon.
 */
package br.uel.pokedex.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.service.PokemonService;
import br.uel.pokedex.service.PokeApiService;

@Controller
public class CatalogController {

    private final PokemonService pokemonService;
    private final PokeApiService pokeApiService;

    public CatalogController(PokemonService pokemonService, PokeApiService pokeApiService) {
        this.pokemonService = pokemonService;
        this.pokeApiService = pokeApiService;
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        List<Pokemon> pokemons = pokemonService.listarTodos();

        List<String> tipos = pokemons.stream()
                .flatMap(pokemon -> Stream.of(
                        pokemon.getTipoPrimario(),
                        pokemon.getTipoSecundario()))
                .filter(tipo -> tipo != null && !tipo.isBlank())
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("tipos", tipos);

        return "catalogo";
    }

    @GetMapping("/catalogo/novo")
    public String novoPokemon(Model model) {
        model.addAttribute("pokemon", new Pokemon());
        model.addAttribute("tituloFormulario", "Cadastrar Pokémon");
        model.addAttribute("pokeApiPokemons", pokeApiService.listarDestaques());
        return "pokemon-form";
    }

    @GetMapping("/catalogo/editar/{id}")
    public String editarPokemon(@PathVariable Long id, Model model) {
        model.addAttribute("pokemon", pokemonService.buscarPorId(id));
        model.addAttribute("tituloFormulario", "Editar Pokémon");
        return "pokemon-form";
    }

    @PostMapping("/catalogo/salvar")
    public String salvarPokemon(@ModelAttribute Pokemon pokemon) {
        pokemonService.salvar(pokemon);
        return "redirect:/catalogo";
    }

    @PostMapping("/catalogo/excluir/{id}")
    public String excluirPokemon(@PathVariable Long id) {
        pokemonService.excluir(id);
        return "redirect:/catalogo";
    }
}
