package com.example.clinicapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.mapper.PacienteMapper;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócios dos pacientes,
 * incluindo listagem, busca, criação, atualização e exclusão de pacientes.
 */
@Service
@RequiredArgsConstructor
public class PacienteService {

    /**
     * Repositório para operações de persistência de pacientes.
     */
    private final PacienteRepository pacienteRepository;
    /**
     * Mapper para conversão entre entidades Paciente e PacienteDTO.
     */
    private final PacienteMapper pacienteMapper;

    /**
     * Retorna uma página de todos os pacientes existentes,
     * convertidos para DTOs.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de PacienteDTOs.
     */
    public Page<PacienteDTO> findAll(final Pageable pageable) {
        return pacienteRepository.findAll(pageable)
                .map(pacienteMapper::toDTO);
    }

    /**
     * Busca um paciente pelo seu ID e o converte para DTO.
     *
     * @param id O ID do paciente a ser buscado.
     * @return Um Optional contendo o PacienteDTO, se encontrado.
     */
    public Optional<PacienteDTO> findById(final Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toDTO);
    }

    /**
     * Retorna uma página de todos os pacientes ativos,
     * convertidos para DTOs.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de PacienteDTOs representando pacientes ativos.
     */
    public Page<PacienteDTO> findAllAtivos(final Pageable pageable) {
        return pacienteRepository.findAllAtivos(pageable)
                .map(pacienteMapper::toDTO);
    }

    /**
     * Salva um novo paciente no banco de dados.
     *
     * @param pacienteDTO O DTO contendo os dados do paciente a ser salvo.
     * @return O PacienteDTO do paciente salvo.
     */
    public PacienteDTO save(final PacienteDTO pacienteDTO) {
        final Paciente paciente = pacienteMapper.toEntity(pacienteDTO);

        final Paciente pacienteSalvo = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(pacienteSalvo);
    }

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param id  O ID do paciente a ser atualizado.
     * @param dto O DTO contendo os dados atualizados do paciente.
     * @return O PacienteDTO do paciente atualizado.
     * @throws EntityNotFoundException Se o paciente não for encontrado.
     */
    public PacienteDTO update(final Long id, final PacienteDTO dto) {
        final Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() ->
                new EntityNotFoundException("Paciente não encontrado"));

        if (dto.nome() != null) {
            paciente.setNome(dto.nome());
        }
        if (dto.email() != null) {
            paciente.setEmail(dto.email());
        }
        if (dto.cpf() != null) {
            paciente.setCpf(dto.cpf());
        }
        if (dto.telefone() != null) {
            paciente.setTelefone(dto.telefone());
        }
        if (dto.dataNascimento() != null) {
            paciente.setDataNascimento(dto.dataNascimento());
        }
        if (dto.ativo() != null) {
            paciente.setAtivo(dto.ativo());
        }

        return pacienteMapper.toDTO(pacienteRepository.save(paciente));
    }

    /**
     * Exclui um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser excluído.
     */
    public void deleteById(final Long id) {
        pacienteRepository.deleteById(id);
    }
}
