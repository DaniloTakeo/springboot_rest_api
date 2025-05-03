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
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<Page<MedicoDTO>> listarTodos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(medicoService.findAll(pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<Page<MedicoDTO>> listarAtivos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(medicoService.findAllAtivos(pageable));
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<Page<MedicoDTO>> buscarPorEspecialidade(@PathVariable Especialidade especialidade,
    		@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(medicoService.findByEspecialidade(especialidade, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        return medicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> criar(@RequestBody @Valid MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder) {
        MedicoDTO medicoSalvo = medicoService.save(medicoDTO);
        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medicoSalvo.id()).toUri();
        return ResponseEntity.created(uri).body(medicoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(@PathVariable Long id, @RequestBody MedicoDTO medicoDTO) {
        MedicoDTO medicoAtualizado = medicoService.update(id, medicoDTO);
        return ResponseEntity.ok(medicoAtualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}