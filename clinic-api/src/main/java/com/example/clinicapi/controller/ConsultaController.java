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

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.service.ConsultaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    /**
     * Serviço responsável pela lógica de negócios das operações de consulta.
     */
    @Autowired
    private ConsultaService consultaService;

    /**
     * Agenda uma nova consulta.
     *
     * @param consultaDTO Os dados da consulta a ser agendada.
     * @param uriBuilder  Componente para construir a URI de
     * localização do novo recurso.
     * @return ResponseEntity com a ConsultaDTO criada e o status 201 Created.
     */
    @PostMapping
    public ResponseEntity<ConsultaDTO> agendar(
            @RequestBody @Valid final ConsultaDTO consultaDTO,
            final UriComponentsBuilder uriBuilder) {
        ConsultaDTO createdConsulta = consultaService
                .createConsulta(consultaDTO);

        URI uri = uriBuilder.path("/consultas/{id}")
                .buildAndExpand(createdConsulta.id())
                .toUri();

        return ResponseEntity.created(uri).body(createdConsulta);
    }

    /**
     * Busca uma consulta pelo seu ID.
     *
     * @param id O ID da consulta a ser buscada.
     * @return ResponseEntity com a ConsultaDTO encontrada e status 200 OK,
     * ou 404 Not Found se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(
            @PathVariable final Long id) {
        return consultaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas as consultas com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação
     * (tamanho da página, número da página, ordenação).
     * @return ResponseEntity com uma página de ConsultaDTOs e status 200 OK.
     */
    @GetMapping
    public ResponseEntity<Page<ConsultaDTO>> listar(
            @PageableDefault final Pageable pageable) {
        Page<ConsultaDTO> consultas = consultaService
                .findAll(pageable);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Atualiza uma consulta existente.
     *
     * @param id          O ID da consulta a ser atualizada.
     * @param consultaDTO Os novos dados da consulta.
     * @return ResponseEntity com a ConsultaDTO atualizada e status 200 OK,
     * ou 404 Not Found se não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> atualizar(
            @PathVariable final Long id,
            @RequestBody final ConsultaDTO consultaDTO) {
        return consultaService.updateConsulta(id, consultaDTO)
                .map(updatedConsulta -> ResponseEntity.ok(updatedConsulta))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cancela uma consulta pelo seu ID.
     *
     * @param id O ID da consulta a ser cancelada.
     * @return ResponseEntity com status 204 No Content
     * se a consulta for cancelada com sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable final Long id) {
        consultaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
