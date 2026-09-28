package br.uel.pokedex.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.uel.pokedex.model.Equipe;
import br.uel.pokedex.model.EquipeParticipacao.Status;
import br.uel.pokedex.model.Treinador;
import br.uel.pokedex.service.EquipeService;
import br.uel.pokedex.service.PokemonService;

@Controller
public class EquipeWebController {

    private final EquipeService equipeService;
    private final PokemonService pokemonService;

    public EquipeWebController(EquipeService equipeService, PokemonService pokemonService) {
        this.equipeService = equipeService;
        this.pokemonService = pokemonService;
    }

    @GetMapping("/equipes")
    public String equipes(@RequestParam(required = false, defaultValue = "") String busca,
            Model model, HttpSession session) {
        Treinador treinador = treinador(session);
        Set<Long> minhasEquipesIds = equipeService.equipesDoTreinador(treinador.getId()).stream()
            .map(Equipe::getId).collect(Collectors.toSet());
        List<Equipe> equipes = equipeService.listar().stream()
            .filter(equipe -> !minhasEquipesIds.contains(equipe.getId()))
                .toList();
        model.addAttribute("equipes", equipes);
        model.addAttribute("minhasEquipes", equipeService.equipesDoTreinador(treinador.getId()));
        model.addAttribute("equipesGerenciadas", equipeService.equipesGerenciadas(treinador.getId()));
        model.addAttribute("busca", busca);
        model.addAttribute("convites", equipeService.convites(treinador.getId()));
        model.addAttribute("solicitacoes", equipeService.solicitacoes(treinador.getId()));
        model.addAttribute("treinadores", equipeService.buscarTreinadores("", treinador.getId()));
        model.addAttribute("solicitacoesPendentes", equipeService.solicitacoesPendentes(treinador.getId()));
        model.addAttribute("treinadorLogado", treinador);
        return "equipes";
    }

    @GetMapping("/equipes/nova")
    public String nova(Model model, HttpSession session) {
        treinador(session);
        model.addAttribute("equipe", new Equipe());
        model.addAttribute("pokemons", pokemonService.listarTodos());
        model.addAttribute("selectedPokemonIds", Set.of());
        return "equipe-form";
    }

    @GetMapping("/equipes/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        Treinador treinador = treinador(session);
        Equipe equipe = equipeService.buscar(id);
        verificarDono(equipe, treinador);
        model.addAttribute("equipe", equipe);
        model.addAttribute("pokemons", pokemonService.listarTodos());
        model.addAttribute("selectedPokemonIds", equipe.getPokemons().stream()
            .map(pokemon -> pokemon.getId()).collect(Collectors.toSet()));
        return "equipe-form";
    }

    @PostMapping("/equipes/salvar")
    public String salvar(@RequestParam(required = false) Long id, @RequestParam String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) List<Long> pokemonIds, HttpSession session) {
        Treinador treinador = treinador(session);
        if (id != null) {
            verificarDono(equipeService.buscar(id), treinador);
        }
        equipeService.salvar(id, nome, descricao, treinador.getId(), pokemonIds);
        return "redirect:/equipes";
    }

    @PostMapping("/equipes/{id}/excluir")
    public String excluir(@PathVariable Long id, HttpSession session) {
        Treinador treinador = treinador(session);
        verificarDono(equipeService.buscar(id), treinador);
        equipeService.excluir(id);
        return "redirect:/equipes";
    }

    @GetMapping("/equipes/{id}")
    public String detalhe(@PathVariable Long id, Model model, HttpSession session) {
        Treinador treinador = treinador(session);
        model.addAttribute("equipe", equipeService.buscar(id));
        model.addAttribute("treinadorLogado", treinador);
        model.addAttribute("podeGerenciar", equipeService.buscar(id).getTreinador() != null
                && equipeService.buscar(id).getTreinador().getId().equals(treinador.getId()));
        return "equipe-detalhe";
    }

    @PostMapping("/equipes/{id}/convidar")
    public String convidar(@PathVariable Long id, @RequestParam Long treinadorId,
            HttpSession session, RedirectAttributes attributes) {
        equipeService.convidar(id, treinadorId, treinador(session).getId());
        attributes.addFlashAttribute("mensagem", "Convite enviado.");
        return "redirect:/equipes";
    }

    @PostMapping("/equipes/convidar")
    public String convidarPelaBusca(@RequestParam Long equipeId, @RequestParam Long treinadorId,
            HttpSession session, RedirectAttributes attributes) {
        equipeService.convidar(equipeId, treinadorId, treinador(session).getId());
        attributes.addFlashAttribute("mensagem", "Convite enviado.");
        return "redirect:/equipes";
    }

    @PostMapping("/equipes/{id}/solicitar")
    public String solicitar(@PathVariable Long id, HttpSession session, RedirectAttributes attributes) {
        equipeService.solicitar(id, treinador(session).getId());
        attributes.addFlashAttribute("mensagem", "Solicitacao enviada.");
        return "redirect:/equipes";
    }

    @PostMapping("/participacoes/{id}/aceitar")
    public String aceitar(@PathVariable Long id, HttpSession session) {
        equipeService.responder(id, Status.ACEITA, treinador(session).getId());
        return "redirect:/equipes";
    }

    @PostMapping("/participacoes/{id}/recusar")
    public String recusar(@PathVariable Long id, HttpSession session) {
        equipeService.responder(id, Status.RECUSADA, treinador(session).getId());
        return "redirect:/equipes";
    }

    private Treinador treinador(HttpSession session) {
        Treinador treinador = (Treinador) session.getAttribute(AuthController.TREINADOR_SESSAO);
        if (treinador == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Faca login para continuar");
        }
        return treinador;
    }

    private void verificarDono(Equipe equipe, Treinador treinador) {
        if (equipe.getTreinador() == null || !equipe.getTreinador().getId().equals(treinador.getId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Apenas o dono pode alterar a equipe");
        }
    }
}
