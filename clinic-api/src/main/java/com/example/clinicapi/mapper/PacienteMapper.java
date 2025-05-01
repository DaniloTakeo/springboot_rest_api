package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.model.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
	
	PacienteMapper INSTANCE = Mappers.getMapper(PacienteMapper.class);
    PacienteDTO toDTO(Paciente paciente);
    Paciente toEntity(PacienteDTO pacienteDTO);
}