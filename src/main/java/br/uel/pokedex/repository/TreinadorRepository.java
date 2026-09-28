package br.uel.pokedex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uel.pokedex.model.Treinador;

public interface TreinadorRepository extends JpaRepository<Treinador, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Treinador> findByEmailIgnoreCase(String email);
    List<Treinador> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email);
}
