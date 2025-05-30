package com.example.clinicapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.clinicapi.model.StatusConsulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ConsultaDTO(
        Long id,

        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "ID do médico é obrigatório")
        Long medicoId,

        @NotNull(message = "Data e hora da consulta são obrigatórias")
        @Future(message = "A data da consulta deve ser no futuro")
        LocalDateTime dataHora,
        String motivoCancelamento,
        StatusConsulta status
    ) implements Serializable { }
