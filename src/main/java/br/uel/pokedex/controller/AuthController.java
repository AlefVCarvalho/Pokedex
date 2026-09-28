package br.uel.pokedex.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.service.AuthService;

@Controller
public class AuthController {

    public static final String TREINADOR_SESSAO = "treinadorLogado";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/signin")
    public String signin(Model model) {
        model.addAttribute("modo", "signin");
        return "auth";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("modo", "signup");
        return "auth";
    }

    @PostMapping("/auth/signin")
    public String entrar(@RequestParam String email, @RequestParam String senha, HttpSession session) {
        Treinador treinador = authService.autenticar(email, senha);
        session.setAttribute(TREINADOR_SESSAO, treinador);
        return "redirect:/equipes";
    }

    @PostMapping("/auth/signup")
    public String cadastrar(@RequestParam String nome, @RequestParam String email,
            @RequestParam String senha, @RequestParam String confirmacao, HttpSession session) {
        Treinador treinador = authService.cadastrar(nome, email, senha, confirmacao);
        session.setAttribute(TREINADOR_SESSAO, treinador);
        return "redirect:/equipes";
    }

    @PostMapping("/auth/logout")
    public String sair(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
