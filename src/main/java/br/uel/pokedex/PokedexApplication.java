package br.uel.pokedex;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.service.PokemonService;

@SpringBootApplication
public class PokedexApplication {

    private static final String ARTWORK_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";

    public static void main(String[] args) {
        SpringApplication.run(PokedexApplication.class, args);
    }

    @Bean
    CommandLineRunner inserirPokemonsIniciais(PokemonService pokemonService) {
        return args -> {
            List<Pokemon> cadastrados = pokemonService.listarTodos();

            List<Pokemon> pokemonsIniciais = List.of(
                    pokemon(1, "Bulbasaur", "Grama", "Veneno", null),
                    pokemon(4, "Charmander", "Fogo", null, null),
                    pokemon(7, "Squirtle", "Água", null, null),
                    pokemon(25, "Pikachu", "Elétrico", null,
                            "O Pokémon rato elétrico que conquistou o mundo."),
                    pokemon(92, "Gastly", "Fantasma", "Veneno", null),
                    pokemon(133, "Eevee", "Normal", null, null),
                    pokemon(143, "Snorlax", "Normal", null, null),
                    pokemon(149, "Dragonite", "Dragão", "Voador",
                            "Um Pokémon gentil capaz de dar a volta ao mundo."),
                    pokemon(197, "Umbreon", "Sombrio", null, null),
                    pokemon(282, "Gardevoir", "Psíquico", "Fada", null),
                    pokemon(448, "Lucario", "Lutador", "Aço", null),
                    pokemon(6, "Charizard", "Fogo", "Voador",
                            "Suas chamas alcançam temperaturas incríveis.")
            );

            for (Pokemon pokemon : pokemonsIniciais) {
                boolean jaExiste = cadastrados.stream()
                        .anyMatch(cadastrado -> cadastrado.getNumeroPokedex()
                                .equals(pokemon.getNumeroPokedex()));

                if (!jaExiste) {
                    pokemonService.salvar(pokemon);
                }
            }
        };
    }

    private Pokemon pokemon(int numero, String nome, String tipoPrimario,
                            String tipoSecundario, String descricao) {
        return Pokemon.builder()
                .numeroPokedex(numero)
                .nome(nome)
                .tipoPrimario(tipoPrimario)
                .tipoSecundario(tipoSecundario)
                .descricao(descricao)
                .imagemUrl(ARTWORK_URL + numero + ".png")
                .build();
    }
}
