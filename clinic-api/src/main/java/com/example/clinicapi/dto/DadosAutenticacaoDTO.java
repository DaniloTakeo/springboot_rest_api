package com.example.clinicapi.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacaoDTO(
        @NotBlank(message = "Login é obrigatório")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) { }
