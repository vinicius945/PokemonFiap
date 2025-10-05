package com.fiap.pokemon.controller;

import com.fiap.pokemon.model.Pokemon;
import com.fiap.pokemon.model.Treinador;
import com.fiap.pokemon.service.PokemonService;
import com.fiap.pokemon.service.TreinadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;
    private final TreinadorService treinadorService;

    public PokemonController(PokemonService pokemonService, TreinadorService treinadorService) {
        this.pokemonService = pokemonService;
        this.treinadorService = treinadorService;
    }

    @GetMapping
    public String listarPokemons(Model model) {
        List<Pokemon> pokemons = pokemonService.listarTodos();
        model.addAttribute("pokemons", pokemons);
        return "index";
    }

    @GetMapping("/novo")
    public String novoPokemon(Model model) {
        model.addAttribute("pokemon", new Pokemon());
        model.addAttribute("treinadores", treinadorService.listarTodos());
        return "novo-pokemon";
    }

    @PostMapping
    public String salvarPokemon(@ModelAttribute Pokemon pokemon, @RequestParam Long treinadorId) {
        Treinador treinador = treinadorService.buscarPorId(treinadorId)
                .orElseThrow(() -> new RuntimeException("Treinador não encontrado"));

        pokemon.setTreinador(treinador);
        pokemon.setDataCaptura(LocalDate.now());
        pokemonService.salvar(pokemon);
        return "redirect:/pokemons";
    }

    @GetMapping("/{id}/evoluir")
    public String evoluirPokemon(@PathVariable Long id) {
        Pokemon pokemon = pokemonService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado"));

        if (pokemon.getNivel() >= 100) {
            throw new IllegalStateException("Pokémon já está no nível máximo!");
        }

        pokemon.setNivel(pokemon.getNivel() + 1);
        pokemonService.salvar(pokemon);
        return "redirect:/pokemons";
    }

    @GetMapping("/{id}/editar")
    public String editarPokemon(@PathVariable Long id, Model model) {
        Pokemon pokemon = pokemonService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado"));

        model.addAttribute("pokemon", pokemon);
        model.addAttribute("treinadores", treinadorService.listarTodos());
        return "editar-pokemon";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizarPokemon(@PathVariable Long id, @ModelAttribute Pokemon atualizado, @RequestParam Long treinadorId) {
        Pokemon original = pokemonService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pokémon não encontrado"));

        Treinador treinador = treinadorService.buscarPorId(treinadorId)
                .orElseThrow(() -> new RuntimeException("Treinador não encontrado"));

        original.setNome(atualizado.getNome());
        original.setTipo(atualizado.getTipo());
        original.setNivel(atualizado.getNivel());
        original.setTreinador(treinador);
        pokemonService.salvar(original);

        return "redirect:/pokemons";
    }


    @GetMapping("/{id}/liberar")
    public String liberarPokemon(@PathVariable Long id) {
        pokemonService.deletar(id);
        return "redirect:/pokemons";
    }
}

