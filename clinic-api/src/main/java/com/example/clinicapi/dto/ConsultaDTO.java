package com.example.clinicapi.dto;

import java.time.LocalDateTime;

import com.example.clinicapi.model.StatusConsulta;

public record ConsultaDTO(
	    Long id,
	    Long pacienteId,
	    Long medicoId,
	    LocalDateTime dataHora,
	    String motivoCancelamento,
	    StatusConsulta status
	) {}