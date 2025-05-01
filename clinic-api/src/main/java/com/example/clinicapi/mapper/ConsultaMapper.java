package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.model.Consulta;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "status", target = "status")
    ConsultaDTO toDTO(Consulta consulta);

    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "status", target = "status")
    Consulta toEntity(ConsultaDTO consultaDTO);
}