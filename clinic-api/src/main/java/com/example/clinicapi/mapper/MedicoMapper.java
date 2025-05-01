package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.model.Medico;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
    MedicoDTO toDTO(Medico medico);
    Medico toEntity(MedicoDTO medicoDTO);
}