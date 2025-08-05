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
 *
 * <p>Este serviço também pode ser responsável por ações colaterais como
 * o envio de e-mails para pacientes, notificando sobre o agendamento
 * ou alterações no status da consulta.</p>
 *
 * <p>As entidades Consulta, Paciente e Médico são validadas e manipuladas
 * conforme regras de negócio previamente definidas.</p>
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
     * Serviço responsável pelo envio de e-mails para pacientes.
     * Utilizado para notificações de agendamento e atualizações de status.
     */
    private final EmailService emailService;

    /**
     * Valor máximo permitido para o tamanho da página de resultados.
     * Utilizado para limitar a quantidade de dados retornados
     * em uma única requisição.
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
     * Envia um e-mail de confirmação ao paciente após o agendamento.
     *
     * @param consultaDTO O DTO contendo os dados da consulta a ser criada.
     * @return O ConsultaDTO da consulta salva.
     * @throws IllegalArgumentException
     * Se o paciente ou médico não forem encontrados.
     */
    public ConsultaDTO createConsulta(final ConsultaDTO consultaDTO) {
        log.info("Tentando agendar nova consulta: paciente={},"
                + " medico={}, dataHora={}",
                consultaDTO.pacienteId(), consultaDTO.medicoId(),
                consultaDTO.dataHora());

        final Optional<Paciente> paciente = pacienteRepository
                .findById(consultaDTO.pacienteId());
        final Optional<Medico> medico = medicoRepository
                .findById(consultaDTO.medicoId());

        if (paciente.isPresent() && medico.isPresent()) {
            final Consulta consulta = new Consulta();
            consulta.setPaciente(paciente.get());
            consulta.setMedico(medico.get());
            consulta.setDataHora(consultaDTO.dataHora());
            consulta.setStatus(StatusConsulta.AGENDADA);

            final Consulta savedConsulta = consultaRepository.save(consulta);

            log.info("Consulta agendada com sucesso: id={}, dataHora={}",
                    savedConsulta.getId(), savedConsulta.getDataHora());

            emailService.enviarEmail(
                    paciente.get().getEmail(),
                    "Consulta agendada com sucesso",
                    String.format("Olá %s, sua consulta foi agendada para %s.",
                            paciente.get().getNome(),
                            savedConsulta.getDataHora().toString())
            );

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
        int size = Math.min(
                Math.max(
                        TAMANHO_MINIMO_PAGINA,
                        pageable.getPageSize()),
                TAMANHO_MAXIMO_PAGINA);
        String sort = pageable.getSort().toString().replaceAll("[\r\n]", "");

        log.info("Listando todas as consultas - "
                + "página: {}, tamanho: {}, ordenação: {}",
                page,
                size,
                sort);

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
                    log.info("Consulta encontrada: id={}", consulta.getId());
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
     * Permite a atualização de paciente, médico, data/hora, status
     * e motivo de cancelamento.
     * Envia notificação por e-mail ao paciente caso o
     * status da consulta seja alterado.
     *
     * @param id O ID da consulta a ser atualizada.
     * @param consultaDTO O DTO contendo os dados atualizados da consulta.
     * @return Um Optional contendo o ConsultaDTO atualizado, se a consulta
     * for encontrada.
     * @throws IllegalArgumentException Se a consulta não for encontrada.
     */
    public Optional<ConsultaDTO> updateConsulta(final Long id,
            final ConsultaDTO consultaDTO) {
        log.info("Tentando atualizar consulta de ID: {}", id);
        final Optional<Consulta> existingConsulta = consultaRepository
                .findById(id);

        if (existingConsulta.isPresent()) {
            final Consulta consulta = existingConsulta.get();

            if (consultaDTO.pacienteId() != null) {
                pacienteRepository.findById(consultaDTO.pacienteId())
                        .ifPresent(consulta::setPaciente);
            }
            if (consultaDTO.medicoId() != null) {
                medicoRepository.findById(consultaDTO.medicoId())
                        .ifPresent(consulta::setMedico);
            }
            if (consultaDTO.dataHora() != null) {
                consulta.setDataHora(consultaDTO.dataHora());
            }

            boolean statusAlterado = false;
            if (consultaDTO.status() != null
                    && !consultaDTO.status().equals(consulta.getStatus())) {
                consulta.setStatus(consultaDTO.status());
                statusAlterado = true;
            }

            if (consultaDTO.motivoCancelamento() != null) {
                consulta.setMotivoCancelamento(consultaDTO
                        .motivoCancelamento());
            }

            final Consulta updatedConsulta = consultaRepository.save(consulta);
            log.info("Consulta ID {} atualizada com sucesso", id);

            if (statusAlterado && consulta.getPaciente() != null) {
                emailService.enviarEmail(
                        consulta.getPaciente().getEmail(),
                        "Status da sua consulta foi atualizado",
                        String.format("Olá %s, o status da sua "
                                + "consulta agora é: %s.",
                                consulta.getPaciente().getNome(),
                                consulta.getStatus().name())
                );
            }

            return Optional.of(consultaMapper.toDTO(updatedConsulta));
        }

        log.warn("Falha ao atualizar - consulta ID {} não encontrada", id);
        throw new IllegalArgumentException("Consulta não encontrada "
                + "para o ID fornecido");
    }
}
