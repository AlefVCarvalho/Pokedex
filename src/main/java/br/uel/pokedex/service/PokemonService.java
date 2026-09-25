/*
 * Reúne as regras simples do CRUD de Pokémon.
 * Aqui validamos duplicidade, buscamos registros e salvamos ou excluímos dados no MySQL.
 */
package br.uel.pokedex.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.repository.PokemonRepository;

@Service
public class PokemonService {

    private final PokemonRepository repository;

    public PokemonService(PokemonRepository repository) {
        this.repository = repository;
    }

    public List<Pokemon> listarTodos() {
        return repository.findAll();
    }

    public Pokemon buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pokemon nao encontrado"));
    }

    public Pokemon salvar(Pokemon pokemon) {
        validarCamposObrigatorios(pokemon);

        if (pokemon.getId() == null) {
            if (repository.existsByNumeroPokedex(pokemon.getNumeroPokedex())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Ja existe um Pokemon com esse numero da Pokedex");
            }
        } else {
            Pokemon cadastrado = buscarPorId(pokemon.getId());
            boolean numeroFoiAlterado = !Objects.equals(
                    cadastrado.getNumeroPokedex(),
                    pokemon.getNumeroPokedex());

            if (numeroFoiAlterado
                    && repository.existsByNumeroPokedex(pokemon.getNumeroPokedex())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Ja existe um Pokemon com esse numero da Pokedex");
            }
        }

        if (pokemon.getTipoSecundario() != null
                && pokemon.getTipoSecundario().isBlank()) {
            pokemon.setTipoSecundario(null);
        }

        return repository.save(pokemon);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pokemon nao encontrado");
        }

        repository.deleteById(id);
    }

    private void validarCamposObrigatorios(Pokemon pokemon) {
        if (pokemon.getNumeroPokedex() == null
                || pokemon.getNome() == null || pokemon.getNome().isBlank()
                || pokemon.getTipoPrimario() == null || pokemon.getTipoPrimario().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Preencha o ID, o nome e o tipo principal do Pokemon");
        }
    }
}
