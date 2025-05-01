package com.example.clinicapi.dto;

import com.example.clinicapi.model.Especialidade;

public record MedicoDTO(
	    Long id,
	    String nome,
	    String email,
	    String crm,
	    String telefone,
	    Especialidade especialidade,
	    Boolean ativo
	) {}