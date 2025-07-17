package com.example.clinicapi.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.mapper.MedicoMapper;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pela lógica de negócios dos médicos,
 * incluindo listagem, busca, criação, atualização e exclusão de médicos.
 * Gerencia o cache para listagens de médicos ativos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Lazy
public class MedicoService {

    /**
     * Valor máximo permitido para o tamanho da página de resultados.
     * Utilizado para evitar requisições com paginações excessivamente grandes.
     */
    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    /**
     * Valor mínimo permitido para o tamanho da página de resultados.
     * Garante que sempre haja ao menos um item por página.
     */
    private static final int TAMANHO_MINIMO_PAGINA = 1;

    /**
     * Repositório para operações de persistência de médicos.
     */
    private final @Lazy MedicoRepository medicoRepository;

    /**
     * Mapper para conversão entre entidades Medico e MedicoDTO.
     */
    private final MedicoMapper medicoMapper;

    /**
     * Retorna uma página de todos os médicos existentes,
     * convertidos para DTOs.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de MedicoDTOs.
     */
    public Page<MedicoDTO> findAll(final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);

        log.debug("Buscando todos os médicos com paginação: "
                + "página {}, tamanho {}",
                page, size);
        return medicoRepository.findAll(pageable)
                .map(medicoMapper::toDTO);
    }

    /**
     * Busca um médico pelo seu ID e o converte para DTO.
     *
     * @param id O ID do médico a ser buscado.
     * @return Um Optional contendo o MedicoDTO, se encontrado.
     * @throws EntityNotFoundException Se o médico não for encontrado.
     */
    public Optional<MedicoDTO> findById(final Long id) {
        log.debug("Buscando médico por ID: {}", id);

        return medicoRepository.findById(id)
                .map(medicoMapper::toDTO);
    }

    /**
     * Retorna uma página de todos os médicos ativos, convertidos para DTOs,
     * utilizando cache.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de MedicoDTOs representando médicos ativos.
     */
    @Cacheable("medicosAtivos")
    public Page<MedicoDTO> findAllAtivos(final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);

        log.debug("Buscando médicos ativos com paginação: "
                + "página {}, tamanho {}",
                page, size);

        return medicoRepository.findAllAtivos(pageable)
                .map(medicoMapper::toDTO);
    }

    /**
     * Retorna uma página de médicos ativos por especialidade,
     * convertidos para DTOs.
     *
     * @param especialidade A especialidade do médico.
     * @param pageable      Objeto Pageable para paginação e ordenação.
     * @return Uma página de MedicoDTOs
     * representando médicos ativos de uma especialidade.
     */
    public Page<MedicoDTO> findByEspecialidade(
            final Especialidade especialidade,
            final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);

        log.debug("Buscando médicos ativos por especialidade: "
                + "{} - Página {}, tamanho {}",
                especialidade, page, size);

        return medicoRepository
                .findByEspecialidadeAndAtivoTrue(especialidade, pageable)
                .map(medicoMapper::toDTO);
    }

    /**
     * Salva um novo médico no banco de dados.
     * Limpa o cache de 'medicosAtivos' após a operação.
     *
     * @param medicoDTO O DTO contendo os dados do médico a ser salvo.
     * @return O MedicoDTO do médico salvo.
     */
    @CacheEvict(value = "medicosAtivos", allEntries = true)
    public MedicoDTO save(final MedicoDTO medicoDTO) {
        log.info("Salvando novo médico: {}", medicoDTO);
        final Medico medico = medicoMapper.toEntity(medicoDTO);
        return medicoMapper.toDTO(medicoRepository.save(medico));
    }

    /**
     * Atualiza os dados de um médico existente.
     * Limpa o cache de 'medicosAtivos' após a operação.
     *
     * @param id        O ID do médico a ser atualizado.
     * @param medicoDTO O DTO contendo os dados atualizados do médico.
     * @return O MedicoDTO do médico atualizado.
     * @throws RuntimeException Se o médico não for encontrado.
     */
    @CacheEvict(value = "medicosAtivos", allEntries = true)
    public MedicoDTO update(final Long id, final MedicoDTO medicoDTO) {
        log.info("Atualizando médico com ID: {}", id);
        final Medico medicoExistente = medicoRepository.findById(id)
                .orElseThrow(() ->
                new RuntimeException("Médico não encontrado"));

        if (medicoDTO.nome() != null) {
            medicoExistente.setNome(medicoDTO.nome());
        }
        if (medicoDTO.crm() != null) {
            medicoExistente.setCrm(medicoDTO.crm());
        }
        if (medicoDTO.especialidade() != null) {
            medicoExistente.setEspecialidade(medicoDTO.especialidade());
        }
        if (medicoDTO.email() != null) {
            medicoExistente.setEmail(medicoDTO.email());
        }
        if (medicoDTO.telefone() != null) {
            medicoExistente.setTelefone(medicoDTO.telefone());
        }
        if (medicoDTO.ativo() != null) {
            medicoExistente.setAtivo(medicoDTO.ativo());
        }

        return medicoMapper.toDTO(medicoRepository.save(medicoExistente));
    }

    /**
     * Exclui um médico pelo seu ID.
     * Limpa o cache de 'medicosAtivos' após a operação.
     *
     * @param id O ID do médico a ser excluído.
     */
    @CacheEvict(value = "medicosAtivos", allEntries = true)
    public void deleteById(final Long id) {
        log.info("Excluindo (inativando) médico com ID: {}", id);
        medicoRepository.deleteById(id);
    }
}
