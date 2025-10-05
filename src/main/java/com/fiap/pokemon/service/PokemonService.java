package com.fiap.pokemon.service;

import java.util.List;

import com.fiap.pokemon.model.Pokemon;
import com.fiap.pokemon.repository.PokemonRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PokemonService {

    private final PokemonRepository repo;

    public PokemonService(PokemonRepository repo) {
        this.repo = repo;
    }

    public List<Pokemon> listarTodos() {
        return repo.findAll();
    }

    public Optional<Pokemon> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Pokemon salvar(Pokemon pokemon) {
        if (pokemon.getNivel() < 1 || pokemon.getNivel() > 100) {
            throw new IllegalArgumentException("O nível do Pokémon deve estar entre 1 e 100.");
        }
        return repo.save(pokemon);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}




