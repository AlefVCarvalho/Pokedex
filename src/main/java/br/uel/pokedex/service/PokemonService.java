package br.uel.pokedex.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
        return repository.findById(id).orElse(null);
    }

    public Pokemon salvar(Pokemon pokemon) {
        return repository.save(pokemon);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
