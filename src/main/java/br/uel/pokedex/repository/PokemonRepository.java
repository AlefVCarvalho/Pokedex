package br.uel.pokedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uel.pokedex.model.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    boolean existsByNumeroPokedex(Integer numeroPokedex);
}
