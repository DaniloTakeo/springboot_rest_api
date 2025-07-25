package com.example.clinicapi.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.service.PacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public final class PacienteController {

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
     * Serviço responsável pela lógica de negócios para operações com pacientes.
     */
    private final PacienteService pacienteService;

    /**
     * Lista todos os pacientes (ativos e inativos) com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação.
     * @return Uma página de PacienteDTOs.
     */
    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listarTodos(
            @PageableDefault final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);

        log.info("Listando pacientes - página: {}, tamanho: {}", page, size);

        return ResponseEntity.ok(pacienteService.findAll(pageable));
    }

    /**
     * Lista apenas os pacientes que estão ativos no sistema com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação.
     * @return Uma página de PacienteDTOs de pacientes ativos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<Page<PacienteDTO>> listarAtivos(
            @PageableDefault final Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(TAMANHO_MINIMO_PAGINA,
                pageable.getPageSize()), TAMANHO_MAXIMO_PAGINA);

        log.info("Requisição recebida para listar "
                + "pacientes ativos - página: {}, tamanho: {}",
                page, size);

        return ResponseEntity.ok(pacienteService.findAllAtivos(pageable));
    }

    /**
     * Busca um paciente pelo seu ID.
     *
     * @param id O ID do paciente.
     * @return ResponseEntity com o PacienteDTO encontrado
     * ou status 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(
            @PathVariable final Long id) {
        log.info("Buscando paciente pelo ID: {}", id);

        return pacienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo registro de paciente no sistema.
     *
     * @param pacienteDTO Os dados do paciente a ser criado.
     * @param uriBuilder  Usado para construir a URI do recurso criado.
     * @return ResponseEntity com o PacienteDTO criado e status 201 Created.
     */
    @PostMapping
    public ResponseEntity<PacienteDTO> criar(
            @RequestBody @Valid final PacienteDTO pacienteDTO,
            final UriComponentsBuilder uriBuilder) {
        log.info("Criando novo paciente: {}", pacienteDTO);

        final PacienteDTO pacienteSalvo = pacienteService.save(pacienteDTO);
        final URI location = uriBuilder
                .path("/pacientes/{id}")
                .buildAndExpand(pacienteSalvo.id())
                .toUri();

        return ResponseEntity.created(location).body(pacienteSalvo);
    }

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param id  O ID do paciente a ser atualizado.
     * @param dto Os novos dados do paciente.
     * @return ResponseEntity com o PacienteDTO atualizado e status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> atualizar(
            @PathVariable final Long id,
            @RequestBody final PacienteDTO dto) {
        log.info("Atualizando paciente ID {} com dados: {}", id, dto);

        return ResponseEntity.ok(pacienteService.update(id, dto));
    }

    /**
     * Deleta (inativa) um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser deletado.
     * @return ResponseEntity com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable final Long id) {
        log.info("Deletando (inativando) paciente com ID: {}", id);

        pacienteService.deleteById(id);

        log.info("Paciente com ID {} inativado com sucesso", id);

        return ResponseEntity.noContent().build();
    }
}
