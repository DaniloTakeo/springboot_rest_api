package com.example.clinicapi.dto;

import java.time.LocalDate;

public record PacienteDTO(
	    Long id,
	    String nome,
	    String email,
	    String cpf,
	    String telefone,
	    LocalDate dataNascimento,
	    Boolean ativo
	) {}