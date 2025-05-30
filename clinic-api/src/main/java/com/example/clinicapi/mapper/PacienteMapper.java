package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.model.Paciente;

/**
 * Interface de mapeamento para converter entre a entidade {@link Paciente}
 * e o Data Transfer Object (DTO) {@link PacienteDTO}.
 * Utiliza MapStruct para geração automática da implementação.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper {

    /**
     * Converte uma entidade {@link Paciente} para um {@link PacienteDTO}.
     *
     * @param paciente A entidade Paciente a ser convertida.
     * @return O PacienteDTO resultante.
     */
    PacienteDTO toDTO(Paciente paciente);

    /**
     * Converte um {@link PacienteDTO} para uma entidade {@link Paciente}.
     *
     * @param pacienteDTO O PacienteDTO a ser convertido.
     * @return A entidade Paciente resultante.
     */
    Paciente toEntity(PacienteDTO pacienteDTO);
}
