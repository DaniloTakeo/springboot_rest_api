package com.example.clinicapi.model;

import lombok.Data;

@Data
public class Medico {

	private Long id;
	private String nome;
	private String crm;
	private Especialidade especialidade;
	private String email;
	private String telefone;
	private Boolean ativo;
	
}
