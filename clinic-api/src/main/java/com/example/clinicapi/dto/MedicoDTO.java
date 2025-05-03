package com.example.clinicapi.dto;

import com.example.clinicapi.model.Especialidade;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicoDTO(
	    Long id,
	    
	    @NotBlank(message = "Nome é obrigatório")
	    String nome,
	    
	    @Email(message = "Email inválido")
	    String email,
	    
	    @NotBlank(message = "CRM é obrigatório")
	    String crm,
	    
	    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos numéricos")
	    String telefone,
	    
	    @NotNull(message = "Especialidade é obrigatória")
	    Especialidade especialidade,
	    
	    Boolean ativo
	) {}