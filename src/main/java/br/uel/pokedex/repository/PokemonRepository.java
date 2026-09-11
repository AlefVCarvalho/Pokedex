package br.uel.pokedex.repository;

import br.uel.pokedex.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    boolean existsByNumeroPokedex(Integer numeroPokedex);
}
