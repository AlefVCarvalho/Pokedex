package br.uel.pokedex.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.uel.pokedex.model.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

	@Override
	@EntityGraph(attributePaths = { "treinador", "pokemons", "membros", "estatisticas" })
	java.util.List<Equipe> findAll();

	@Override
	@EntityGraph(attributePaths = { "treinador", "pokemons", "membros", "estatisticas" })
	java.util.Optional<Equipe> findById(Long id);

	@EntityGraph(attributePaths = { "treinador", "pokemons", "membros", "estatisticas" })
	java.util.List<Equipe> findByTreinadorId(Long treinadorId);

	@EntityGraph(attributePaths = { "treinador", "pokemons", "membros", "estatisticas" })
	java.util.List<Equipe> findByMembrosId(Long treinadorId);
}
