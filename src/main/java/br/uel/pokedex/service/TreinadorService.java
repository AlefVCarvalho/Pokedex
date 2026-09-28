package br.uel.pokedex.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.repository.TreinadorRepository;

@Service
public class TreinadorService {

    private final TreinadorRepository repository;

    public TreinadorService(TreinadorRepository repository) {
        this.repository = repository;
    }

    public List<Treinador> listar() {
        return repository.findAll();
    }

    public Treinador buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Treinador nao encontrado"));
    }

    public Treinador salvar(Treinador treinador) {
        if (treinador.getNome() == null || treinador.getNome().isBlank()
                || treinador.getEmail() == null || treinador.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e email sao obrigatorios");
        }

        if (treinador.getId() != null) {
            Treinador atual = buscar(treinador.getId());
            boolean emailAlterado = !atual.getEmail().equalsIgnoreCase(treinador.getEmail());
            if (emailAlterado && repository.existsByEmailIgnoreCase(treinador.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um treinador com esse email");
            }
        } else if (repository.existsByEmailIgnoreCase(treinador.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um treinador com esse email");
        }

        return repository.save(treinador);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador nao encontrado");
        }
        repository.deleteById(id);
    }
}
