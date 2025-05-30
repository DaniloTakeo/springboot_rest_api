package com.example.clinicapi.mapper;

import org.mapstruct.Mapper;

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.model.Medico;

/**
 * Interface de mapeamento para converter entre a entidade {@link Medico}
 * e o Data Transfer Object (DTO) {@link MedicoDTO}.
 * Utiliza MapStruct para geração automática da implementação.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper {

    /**
     * Converte uma entidade {@link Medico} para um {@link MedicoDTO}.
     *
     * @param medico A entidade Medico a ser convertida.
     * @return O MedicoDTO resultante.
     */
    MedicoDTO toDTO(Medico medico);

    /**
     * Converte um {@link MedicoDTO} para uma entidade {@link Medico}.
     *
     * @param medicoDTO O MedicoDTO a ser convertido.
     * @return A entidade Medico resultante.
     */
    Medico toEntity(MedicoDTO medicoDTO);
}
