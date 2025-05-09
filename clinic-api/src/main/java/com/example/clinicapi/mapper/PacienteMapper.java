package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.model.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
	
    PacienteDTO toDTO(Paciente paciente);
    Paciente toEntity(PacienteDTO pacienteDTO);
}