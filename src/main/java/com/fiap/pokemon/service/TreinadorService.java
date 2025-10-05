package com.fiap.pokemon.service;



import com.fiap.pokemon.model.Treinador;
import com.fiap.pokemon.repository.TreinadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreinadorService {

    private final TreinadorRepository repo;

    public TreinadorService(TreinadorRepository repo) {
        this.repo = repo;
    }

    public List<Treinador> listarTodos() {
        return repo.findAll();
    }

    public Optional<Treinador> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Treinador salvar(Treinador treinador) {
        boolean emailJaExiste = repo.findAll().stream()
                .anyMatch(t -> t.getEmail().equalsIgnoreCase(treinador.getEmail())
                        && !t.getId().equals(treinador.getId()));

        if (emailJaExiste) {
            throw new IllegalArgumentException("Já existe um treinador com esse e-mail!");
        }

        return repo.save(treinador);
    }


    public void deletar(Long id) {
        Treinador treinador = repo.findById(id).orElseThrow(() -> new RuntimeException("Treinador não encontrado"));
        if (treinador.getPokemons() != null && !treinador.getPokemons().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir o treinador pois ele possui Pokémons capturados.");
        }
        repo.deleteById(id);
    }
}