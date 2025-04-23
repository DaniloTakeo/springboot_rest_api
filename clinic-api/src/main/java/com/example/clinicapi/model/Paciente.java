package com.example.clinicapi.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Paciente {

	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String telefone;
	private LocalDate dataNascimento;
	private Boolean ativo;
	
}
