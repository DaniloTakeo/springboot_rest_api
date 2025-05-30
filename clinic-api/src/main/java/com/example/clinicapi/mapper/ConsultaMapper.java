package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.model.Consulta;

/**
 * Interface de mapeamento para converter entre a entidade {@link Consulta}
 * e o Data Transfer Object (DTO) {@link ConsultaDTO}.
 * Utiliza MapStruct para geração automática da implementação.
 */
@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    /**
     * Converte uma entidade {@link Consulta} para um {@link ConsultaDTO}.
     * Mapeia os IDs do paciente e médico para campos simples no DTO.
     *
     * @param consulta A entidade Consulta a ser convertida.
     * @return O ConsultaDTO resultante.
     */
    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "status", target = "status")
    ConsultaDTO toDTO(Consulta consulta);

    /**
     * Converte um {@link ConsultaDTO} para uma entidade {@link Consulta}.
     * Mapeia os IDs de paciente e médico do DTO para as entidades aninhadas.
     * O status da consulta também é mapeado.
     *
     * @param consultaDTO O ConsultaDTO a ser convertido.
     * @return A entidade Consulta resultante.
     */
    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "medicoId", target = "medico.id")
    @Mapping(source = "status", target = "status")
    Consulta toEntity(ConsultaDTO consultaDTO);
}
