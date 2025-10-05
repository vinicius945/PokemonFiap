package com.fiap.pokemon.repository;

import com.fiap.pokemon.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository  extends JpaRepository <Pokemon, Long> {
}
