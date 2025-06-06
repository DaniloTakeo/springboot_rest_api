package com.example.clinicapi.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/pacientes")
public final class PacienteController { // Torne a classe final

    /**
     * Serviço responsável pela lógica de negócios para operações com pacientes.
     */
    @Autowired
    private PacienteService pacienteService;

    /**
     * Lista todos os pacientes (ativos e inativos) com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação.
     * @return Uma página de PacienteDTOs.
     */
    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listarTodos(
            @PageableDefault final Pageable pageable) {
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
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
