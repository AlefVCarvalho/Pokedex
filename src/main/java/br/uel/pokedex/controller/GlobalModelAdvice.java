package br.uel.pokedex.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("treinadorLogado")
    public Object treinadorLogado(HttpSession session) {
        return session.getAttribute(AuthController.TREINADOR_SESSAO);
    }
}
