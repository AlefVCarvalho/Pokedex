package br.uel.pokedex.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.uel.pokedex.model.Equipe;
import br.uel.pokedex.model.EquipeParticipacao;
import br.uel.pokedex.model.EquipeParticipacao.Status;
import br.uel.pokedex.model.EquipeParticipacao.Tipo;
import br.uel.pokedex.model.EstatisticaEquipe;
import br.uel.pokedex.model.Pokemon;
import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.repository.EquipeRepository;
import br.uel.pokedex.repository.PokemonRepository;
import br.uel.pokedex.repository.TreinadorRepository;
import br.uel.pokedex.repository.EquipeParticipacaoRepository;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final TreinadorRepository treinadorRepository;
    private final PokemonRepository pokemonRepository;
    private final EquipeParticipacaoRepository participacaoRepository;

    public EquipeService(EquipeRepository equipeRepository, TreinadorRepository treinadorRepository,
            PokemonRepository pokemonRepository, EquipeParticipacaoRepository participacaoRepository) {
        this.equipeRepository = equipeRepository;
        this.treinadorRepository = treinadorRepository;
        this.pokemonRepository = pokemonRepository;
        this.participacaoRepository = participacaoRepository;
    }

    public List<Equipe> listar() {
        return equipeRepository.findAll();
    }

    public List<Equipe> equipesGerenciadas(Long treinadorId) {
        return equipeRepository.findByTreinadorId(treinadorId);
    }

    public List<Equipe> equipesDoTreinador(Long treinadorId) {
        return equipeRepository.findByMembrosId(treinadorId);
    }

        public Set<Long> solicitacoesPendentes(Long treinadorId) {
        return equipeRepository.findAll().stream()
            .filter(equipe -> participacaoRepository
                .findByEquipeIdAndTreinadorIdAndTipoAndStatus(
                    equipe.getId(), treinadorId, Tipo.SOLICITACAO, Status.PENDENTE)
                .isPresent())
            .map(Equipe::getId)
            .collect(Collectors.toSet());
        }

    public EstatisticaEquipe buscarEstatisticas(Long equipeId) {
        EstatisticaEquipe estatistica = buscar(equipeId).getEstatisticas();
        if (estatistica == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estatisticas da equipe nao encontradas");
        }
        return estatistica;
    }

    @Transactional
    public EstatisticaEquipe salvarEstatisticas(Long equipeId, Integer vitorias,
            Integer derrotas, Integer empates) {
        if (vitorias == null || derrotas == null || empates == null
                || vitorias < 0 || derrotas < 0 || empates < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "As estatisticas devem ser numeros maiores ou iguais a zero");
        }
        Equipe equipe = buscar(equipeId);
        EstatisticaEquipe estatistica = equipe.getEstatisticas();
        if (estatistica == null) {
            estatistica = new EstatisticaEquipe();
            estatistica.setEquipe(equipe);
            equipe.setEstatisticas(estatistica);
        }
        estatistica.setVitorias(vitorias);
        estatistica.setDerrotas(derrotas);
        estatistica.setEmpates(empates);
        equipeRepository.save(equipe);
        return estatistica;
    }

    public Equipe buscar(Long id) {
        return equipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Equipe nao encontrada"));
    }

    @Transactional
    public Equipe salvar(Long id, String nome, String descricao, Long treinadorId, List<Long> pokemonIds) {
        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da equipe e obrigatorio");
        }

        Equipe equipe = id == null ? new Equipe() : buscar(id);
        equipe.setNome(nome.trim());
        equipe.setDescricao(descricao);
        Treinador dono = buscarTreinador(treinadorId);
        equipe.setTreinador(dono);
        if (dono != null) {
            equipe.getMembros().add(dono);
        }
        equipe.getPokemons().clear();

        if (pokemonIds != null) {
            for (Long pokemonId : pokemonIds) {
                Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Pokemon nao encontrado: " + pokemonId));
                equipe.getPokemons().add(pokemon);
            }
        }

        return equipeRepository.save(equipe);
    }

    public void excluir(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipe nao encontrada");
        }
        equipeRepository.deleteById(id);
    }

    public List<Treinador> buscarTreinadores(String busca, Long atualId) {
        return treinadorRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        busca == null ? "" : busca, busca == null ? "" : busca).stream()
                .filter(treinador -> !treinador.getId().equals(atualId))
                .toList();
    }

    public EquipeParticipacao convidar(Long equipeId, Long treinadorId, Long donoId) {
        Equipe equipe = buscar(equipeId);
        validarDono(equipe, donoId);
        Treinador treinador = treinadorRepository.findById(treinadorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador nao encontrado"));
        if (equipe.getMembros().contains(treinador)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este treinador ja esta na equipe");
        }
        EquipeParticipacao participacao = participacaoRepository
                .findByEquipeIdAndTreinadorIdAndTipo(equipeId, treinadorId, Tipo.CONVITE)
                .orElseGet(EquipeParticipacao::new);
        participacao.setEquipe(equipe);
        participacao.setTreinador(treinador);
        participacao.setTipo(Tipo.CONVITE);
        participacao.setStatus(Status.PENDENTE);
        return participacaoRepository.save(participacao);
    }

    public EquipeParticipacao solicitar(Long equipeId, Long treinadorId) {
        Equipe equipe = buscar(equipeId);
        if (equipe.getTreinador() != null && equipe.getTreinador().getId().equals(treinadorId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O dono ja pertence a equipe");
        }
        Treinador treinador = treinadorRepository.findById(treinadorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador nao encontrado"));
        EquipeParticipacao participacao = participacaoRepository
                .findByEquipeIdAndTreinadorIdAndTipo(equipeId, treinadorId, Tipo.SOLICITACAO)
                .orElseGet(EquipeParticipacao::new);
        participacao.setEquipe(equipe);
        participacao.setTreinador(treinador);
        participacao.setTipo(Tipo.SOLICITACAO);
        participacao.setStatus(Status.PENDENTE);
        return participacaoRepository.save(participacao);
    }

    @Transactional
    public void responder(Long participacaoId, Status status, Long treinadorId) {
        EquipeParticipacao participacao = participacaoRepository.findById(participacaoId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Participacao nao encontrada"));
        boolean conviteDoJogador = participacao.getTipo() == Tipo.CONVITE
                && participacao.getTreinador().getId().equals(treinadorId);
        boolean solicitacaoDaEquipe = participacao.getTipo() == Tipo.SOLICITACAO
                && participacao.getEquipe().getTreinador() != null
                && participacao.getEquipe().getTreinador().getId().equals(treinadorId);
        if (!conviteDoJogador && !solicitacaoDaEquipe) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Voce nao pode responder esta participacao");
        }
        participacao.setStatus(status);
        if (status == Status.ACEITA) {
            participacao.getEquipe().getMembros().add(participacao.getTreinador());
            equipeRepository.save(participacao.getEquipe());
        }
        participacaoRepository.save(participacao);
    }

    public List<EquipeParticipacao> convites(Long treinadorId) {
        return participacaoRepository.findByTreinadorIdAndTipoAndStatus(treinadorId, Tipo.CONVITE, Status.PENDENTE);
    }

    public List<EquipeParticipacao> solicitacoes(Long treinadorId) {
        return participacaoRepository.findByEquipeTreinadorIdAndTipoAndStatus(
            treinadorId, Tipo.SOLICITACAO, Status.PENDENTE);
    }

    private void validarDono(Equipe equipe, Long treinadorId) {
        if (equipe.getTreinador() == null || !equipe.getTreinador().getId().equals(treinadorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o dono pode convidar treinadores");
        }
    }

    private Treinador buscarTreinador(Long treinadorId) {
        if (treinadorId == null) {
            return null;
        }
        return treinadorRepository.findById(treinadorId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Treinador nao encontrado"));
    }
}
