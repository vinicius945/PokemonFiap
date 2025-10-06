package com.fiap.pokemon.controller;


import com.fiap.pokemon.model.Treinador;
import com.fiap.pokemon.service.TreinadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/treinadores")
public class TreinadorController {

    private final TreinadorService treinadorService;

    public TreinadorController(TreinadorService treinadorService) {
        this.treinadorService = treinadorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("treinadores", treinadorService.listarTodos());
        return "treinadores";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("treinador", new Treinador());
        return "novo-treinador";
    }

    @PostMapping
    public String salvar(@ModelAttribute Treinador treinador) {
        treinadorService.salvar(treinador);
        return "redirect:/treinadores";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Treinador treinador = treinadorService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Treinador não encontrado"));
        model.addAttribute("treinador", treinador);
        return "editar-treinador";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Treinador atualizado) {
        Treinador original = treinadorService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Treinador não encontrado"));

        original.setNome(atualizado.getNome());
        original.setEmail(atualizado.getEmail());
        treinadorService.salvar(original);

        return "redirect:/treinadores";
    }


    @GetMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id) {
        treinadorService.deletarTreinador(id);
        return "redirect:/treinadores";
    }
}
