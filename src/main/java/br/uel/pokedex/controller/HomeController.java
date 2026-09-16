package br.uel.pokedex.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.uel.pokedex.model.Pokemon;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalPokemons", "151");
        model.addAttribute("totalTipos", "18");
        model.addAttribute("totalRegioes", "9");
        model.addAttribute("destaques", List.of(
                Pokemon.builder().numeroPokedex(25).nome("Pikachu").tipoPrimario("Elétrico")
                        .descricao("O Pokémon rato elétrico que conquistou o mundo.")
                        .imagemUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png")
                        .build(),
                Pokemon.builder().numeroPokedex(6).nome("Charizard").tipoPrimario("Fogo")
                        .tipoSecundario("Voador").descricao("Suas chamas alcançam temperaturas incríveis.")
                        .imagemUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png")
                        .build(),
                Pokemon.builder().numeroPokedex(149).nome("Dragonite").tipoPrimario("Dragão")
                        .tipoSecundario("Voador").descricao("Um Pokémon gentil capaz de dar a volta ao mundo.")
                        .imagemUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/149.png")
                        .build()
        ));
        return "home";
    }
}