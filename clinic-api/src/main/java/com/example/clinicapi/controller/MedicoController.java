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

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.service.MedicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
public final class MedicoController {

    /**
     * Serviço responsável pela lógica de negócios das operações de médico.
     */
    @Autowired
    private MedicoService medicoService;

    /**
     * Lista todos os médicos ativos e inativos com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação.
     * @return Uma página de MedicoDTOs.
     */
    @GetMapping
    public ResponseEntity<Page<MedicoDTO>> listarTodos(
            @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(medicoService.findAll(pageable));
    }

    /**
     * Lista apenas os médicos que estão ativos no sistema com paginação.
     *
     * @param pageable Objeto Pageable para configurar a paginação.
     * @return Uma página de MedicoDTOs de médicos ativos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<Page<MedicoDTO>> listarAtivos(
            @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(medicoService.findAllAtivos(pageable));
    }

    /**
     * Busca médicos por especialidade com paginação.
     *
     * @param especialidade A especialidade a ser filtrada.
     * @param pageable      Objeto Pageable para configurar a paginação.
     * @return Uma página de MedicoDTOs que correspondem à especialidade.
     */
    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<Page<MedicoDTO>> buscarPorEspecialidade(
            @PathVariable final Especialidade especialidade,
            @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(
                medicoService.findByEspecialidade(especialidade, pageable));
    }

    /**
     * Busca um médico pelo seu ID.
     *
     * @param id O ID do médico.
     * @return ResponseEntity com o MedicoDTO encontrado
     * ou status 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable final Long id) {
        return medicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo registro de médico no sistema.
     *
     * @param medicoDTO  Os dados do médico a ser criado.
     * @param uriBuilder Usado para construir a URI do recurso criado.
     * @return ResponseEntity com o MedicoDTO criado e status 201 Created.
     */
    @PostMapping
    public ResponseEntity<MedicoDTO> criar(
            @RequestBody @Valid final MedicoDTO medicoDTO,
            final UriComponentsBuilder uriBuilder) {
        final MedicoDTO medicoSalvo = medicoService.save(medicoDTO);
        final URI uri = uriBuilder.path("/medicos/{id}")
                                  .buildAndExpand(medicoSalvo.id())
                                  .toUri();
        return ResponseEntity.created(uri).body(medicoSalvo);
    }

    /**
     * Atualiza os dados de um médico existente.
     *
     * @param id        O ID do médico a ser atualizado.
     * @param medicoDTO Os novos dados do médico.
     * @return ResponseEntity com o MedicoDTO atualizado e status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(
            @PathVariable final Long id,
            @RequestBody final MedicoDTO medicoDTO) {
        final MedicoDTO medicoAtualizado = medicoService.update(id, medicoDTO);
        return ResponseEntity.ok(medicoAtualizado);
    }

    /**
     * Deleta (inativa) um médico pelo seu ID.
     *
     * @param id O ID do médico a ser deletado.
     * @return ResponseEntity com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable final Long id) {
        medicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
