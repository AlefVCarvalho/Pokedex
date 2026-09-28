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

import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.service.TreinadorService;

@RestController
@RequestMapping("/api/treinadores")
public class TreinadorController {

    private final TreinadorService service;

    public TreinadorController(TreinadorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Treinador> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Treinador buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Treinador criar(@RequestBody Treinador treinador) {
        treinador.setId(null);
        return service.salvar(treinador);
    }

    @PutMapping("/{id}")
    public Treinador atualizar(@PathVariable Long id, @RequestBody Treinador treinador) {
        treinador.setId(id);
        return service.salvar(treinador);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
