package br.uel.pokedex.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.repository.TreinadorRepository;

@Service
public class AuthService {

    private final TreinadorRepository repository;

    public AuthService(TreinadorRepository repository) {
        this.repository = repository;
    }

    public Treinador cadastrar(String nome, String email, String senha, String confirmacao) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank()
                || senha == null || senha.length() < 4 || !senha.equals(confirmacao)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Preencha os dados e confirme uma senha com pelo menos 4 caracteres");
        }
        if (repository.existsByEmailIgnoreCase(email.trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este email ja esta cadastrado");
        }

        Treinador treinador = new Treinador();
        treinador.setNome(nome.trim());
        treinador.setEmail(email.trim().toLowerCase());
        treinador.setSenha(senha);
        return repository.save(treinador);
    }

    public Treinador autenticar(String email, String senha) {
        return repository.findByEmailIgnoreCase(email == null ? "" : email.trim())
                .filter(treinador -> treinador.getSenha().equals(senha))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Email ou senha invalidos"));
    }
}
