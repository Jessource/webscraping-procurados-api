package com.webscraping.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "procurados")
@Getter
@Setter
public class Procurados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String crime;
    String nome;
    String imagem;

    @Enumerated(EnumType.STRING)
    OrigemDados origemDados;
}
