package com.example.clinicapi.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Consulta {

	private Long id;
	private Paciente paciente;
	private Medico medico;
	private LocalDateTime dataHora;
	private String motivoCancelamento;
	private StatusConsulta status;
	
}
