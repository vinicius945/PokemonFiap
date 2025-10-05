package com.fiap.pokemon.model;
import jakarta.persistence.*;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Treinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    @OneToMany(mappedBy = "treinador", cascade = CascadeType.ALL)
    private List<Pokemon> pokemons;
}
