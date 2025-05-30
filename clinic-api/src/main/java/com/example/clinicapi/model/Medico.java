package com.example.clinicapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    private String email;

    private String telefone;

    private boolean ativo;

    public Medico(String nome, String crm, Especialidade especialidade, String email, String telefone) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.email = email;
        this.telefone = telefone;
        this.ativo = true;
    }
}
