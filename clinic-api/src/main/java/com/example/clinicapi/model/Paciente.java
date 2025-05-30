package com.example.clinicapi.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
@Table(name = "pacientes")
public class Paciente {

    /**
     * O identificador único do paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome completo do paciente.
     */
    private String nome;

    /**
     * O endereço de email do paciente.
     */
    private String email;

    /**
     * O número de Cadastro de Pessoas Físicas (CPF) do paciente.
     */
    private String cpf;

    /**
     * O número de telefone do paciente.
     */
    private String telefone;

    /**
     * A data de nascimento do paciente.
     */
    private LocalDate dataNascimento;

    /**
     * Indica se o cadastro do paciente está ativo no sistema.
     */
    private boolean ativo;
	
}
