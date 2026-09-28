package br.uel.pokedex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uel.pokedex.model.EquipeParticipacao;
import br.uel.pokedex.model.EquipeParticipacao.Status;
import br.uel.pokedex.model.EquipeParticipacao.Tipo;

public interface EquipeParticipacaoRepository extends JpaRepository<EquipeParticipacao, Long> {
    List<EquipeParticipacao> findByTreinadorIdAndTipoAndStatus(Long treinadorId, Tipo tipo, Status status);
    List<EquipeParticipacao> findByEquipeIdAndTipoAndStatus(Long equipeId, Tipo tipo, Status status);
    List<EquipeParticipacao> findByEquipeTreinadorIdAndTipoAndStatus(Long treinadorId, Tipo tipo, Status status);
    Optional<EquipeParticipacao> findByEquipeIdAndTreinadorIdAndTipo(Long equipeId, Long treinadorId, Tipo tipo);
    Optional<EquipeParticipacao> findByEquipeIdAndTreinadorIdAndTipoAndStatus(
            Long equipeId, Long treinadorId, Tipo tipo, Status status);
}
