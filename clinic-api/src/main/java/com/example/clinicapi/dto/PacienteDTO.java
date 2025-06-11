package com.example.clinicapi.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record PacienteDTO(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}",
            message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}",
            message = "Telefone deve conter 10 ou 11 dígitos numéricos")
        String telefone,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        Boolean ativo
        ) implements Serializable { }
