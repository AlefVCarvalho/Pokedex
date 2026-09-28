package br.uel.pokedex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.uel.pokedex.model.Equipe;
import br.uel.pokedex.model.EstatisticaEquipe;
import br.uel.pokedex.service.EquipeService;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeService service;

    public EquipeController(EquipeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipe> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Equipe buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipe criar(@RequestBody EquipeRequest request) {
        return salvar(null, request);
    }

    @PutMapping("/{id}")
    public Equipe atualizar(@PathVariable Long id, @RequestBody EquipeRequest request) {
        return salvar(id, request);
    }

    @GetMapping("/{equipeId}/estatisticas")
    public EstatisticaEquipe buscarEstatisticas(@PathVariable Long equipeId) {
        return service.buscarEstatisticas(equipeId);
    }

    @PutMapping("/{equipeId}/estatisticas")
    public EstatisticaEquipe salvarEstatisticas(@PathVariable Long equipeId,
            @RequestBody EstatisticaRequest request) {
        return service.salvarEstatisticas(equipeId, request.vitorias(), request.derrotas(), request.empates());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    private Equipe salvar(Long id, EquipeRequest request) {
        return service.salvar(id, request.nome(), request.descricao(), request.treinadorId(), request.pokemonIds());
    }

    public record EquipeRequest(String nome, String descricao, Long treinadorId, List<Long> pokemonIds) {
    }

    public record EstatisticaRequest(Integer vitorias, Integer derrotas, Integer empates) {
    }
}
