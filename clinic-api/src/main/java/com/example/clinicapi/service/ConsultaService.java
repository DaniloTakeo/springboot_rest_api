package com.example.clinicapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.mapper.ConsultaMapper;
import com.example.clinicapi.model.Consulta;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.model.StatusConsulta;
import com.example.clinicapi.repository.ConsultaRepository;
import com.example.clinicapi.repository.MedicoRepository;
import com.example.clinicapi.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pela lógica de negócios das consultas,
 * incluindo criação, listagem, busca, atualização e exclusão de consultas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    /**
     * Repositório para operações de persistência de consultas.
     */
    private final ConsultaRepository consultaRepository;
    /**
     * Repositório para operações de persistência de pacientes.
     */
    private final PacienteRepository pacienteRepository;
    /**
     * Repositório para operações de persistência de médicos.
     */
    private final MedicoRepository medicoRepository;
    /**
     * Mapper para conversão entre entidades Consulta e ConsultaDTO.
     */
    private final ConsultaMapper consultaMapper;
    /**
     * Valor máximo permitido para o tamanho da página de resultados.
     * Utilizado para limitar a quantidade de
     * dados retornados em uma única requisição.
     */
    private static final int TAMANHO_MAXIMO_PAGINA = 100;
    /**
     * Valor mínimo permitido para o tamanho da página de resultados.
     * Garante que ao menos um item será retornado por página.
     */
    private static final int TAMANHO_MINIMO_PAGINA = 1;

    /**
     * Cria uma nova consulta com base nos dados fornecidos.
     * Valida a existência do paciente e do médico antes de agendar a consulta.
     *
     * @param consultaDTO O DTO contendo os dados da consulta a ser criada.
     * @return O ConsultaDTO da consulta salva.
     * @throws IllegalArgumentException Se o paciente ou
     * médico não forem encontrados.
     */
    public ConsultaDTO createConsulta(final ConsultaDTO consultaDTO) {
        log.info("Tentando agendar nova consulta:"
                + " paciente={}, medico={}, dataHora={}",
                consultaDTO.pacienteId(), consultaDTO.medicoId(),
                consultaDTO.dataHora());

        final Optional<Paciente> paciente =
                pacienteRepository.findById(consultaDTO.pacienteId());
        final Optional<Medico> medico =
                medicoRepository.findById(consultaDTO.medicoId());

        if (paciente.isPresent() && medico.isPresent()) {
            final Consulta consulta = new Consulta();
            consulta.setPaciente(paciente.get());
            consulta.setMedico(medico.get());
            consulta.setDataHora(consultaDTO.dataHora());
            consulta.setStatus(StatusConsulta.AGENDADA);

            final Consulta savedConsulta = consultaRepository.save(consulta);

            log.info("Consulta agendada com sucesso: id={}, dataHora={}",
                    savedConsulta.getId(), savedConsulta.getDataHora());

            return consultaMapper.toDTO(savedConsulta);
        }

        log.warn("Falha ao agendar consulta - "
                + "paciente ou médico não encontrados");

        throw new IllegalArgumentException(
                "Paciente ou Médico não encontrados");
    }

    /**
     * Retorna uma página de todas as consultas existentes,
     * convertidas para DTOs.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de ConsultaDTOs.
     */
    public Page<ConsultaDTO> findAll(final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);
        String sort = pageable.getSort().toString().replaceAll("[\r\n]", "");

        log.info("Listando todas as consultas - "
                + "página: {}, tamanho: {}, ordenação: {}",
                page, size, sort);

        return consultaRepository.findAll(pageable)
                .map(consultaMapper::toDTO);
    }

    /**
     * Busca uma consulta pelo seu ID e a converte para DTO.
     *
     * @param id O ID da consulta a ser buscada.
     * @return Um Optional contendo o ConsultaDTO,
     * se encontrado, ou um Optional vazio.
     */
    public Optional<ConsultaDTO> findById(final Long id) {
        log.info("Buscando consulta por ID: {}", id);
        return consultaRepository.findById(id)
                .map(consulta -> {
                    log.info("Consulta encontrada: id={}",
                            consulta.getId());
                    return consultaMapper.toDTO(consulta);
                });
    }

    /**
     * Exclui uma consulta pelo seu ID.
     *
     * @param id O ID da consulta a ser excluída.
     */
    public void deleteById(final Long id) {
        log.info("Cancelando consulta de ID: {}", id);
        consultaRepository.deleteById(id);
        log.info("Consulta de ID {} cancelada com sucesso", id);
    }

    /**
     * Atualiza os dados de uma consulta existente.
     * Permite a atualização de paciente, médico, data/hora e status.
     *
     * @param id O ID da consulta a ser atualizada.
     * @param consultaDTO O DTO contendo os dados
     * atualizados da consulta.
     * @return Um Optional contendo o ConsultaDTO
     * atualizado, se a consulta for encontrada.
     * @throws IllegalArgumentException
     * Se a consulta não for encontrada.
     */
    public Optional<ConsultaDTO> updateConsulta(
            final Long id, final ConsultaDTO consultaDTO) {
        log.info("Tentando atualizar consulta de ID: {}", id);
        final Optional<Consulta> existingConsulta = consultaRepository
                .findById(id);

        if (existingConsulta.isPresent()) {
            final Consulta consulta = existingConsulta.get();

            if (consultaDTO.pacienteId() != null) {
                final Optional<Paciente> paciente =
                        pacienteRepository.findById(consultaDTO.pacienteId());
                paciente.ifPresent(consulta::setPaciente);
            }
            if (consultaDTO.medicoId() != null) {
                final Optional<Medico> medico =
                        medicoRepository.findById(consultaDTO.medicoId());
                medico.ifPresent(consulta::setMedico);
            }
            if (consultaDTO.dataHora() != null) {
                consulta.setDataHora(consultaDTO.dataHora());
            }
            if (consultaDTO.status() != null) {
                consulta.setStatus(consultaDTO.status());
            }
            if (consultaDTO.motivoCancelamento() != null) {
                consulta.setMotivoCancelamento(consultaDTO
                        .motivoCancelamento());
            }

            final Consulta updatedConsulta = consultaRepository.save(consulta);
            log.info("Consulta ID {} atualizada com sucesso", id);
            return Optional.of(consultaMapper.toDTO(updatedConsulta));
        }

        log.warn("Falha ao atualizar - consulta ID {} não encontrada", id);
        throw new IllegalArgumentException(
                "Consulta não encontrada para o ID fornecido");
    }
}
