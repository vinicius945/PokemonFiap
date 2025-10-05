package com.fiap.pokemon.repository;


import com.fiap.pokemon.model.Treinador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreinadorRepository extends JpaRepository <Treinador, Long>{
}
