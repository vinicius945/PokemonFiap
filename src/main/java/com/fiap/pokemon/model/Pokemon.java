package com.fiap.pokemon.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private int nivel;
    private LocalDate dataCaptura;

    @ManyToOne
    @JoinColumn(name = "treinador_id")
    private Treinador treinador;
}
