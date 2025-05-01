package com.example.clinicapi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        return ResponseEntity.ok(medicoService.findAll());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<MedicoDTO>> listarAtivos() {
        return ResponseEntity.ok(medicoService.findAllAtivos());
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<MedicoDTO>> buscarPorEspecialidade(@PathVariable Especialidade especialidade) {
        return ResponseEntity.ok(medicoService.findByEspecialidade(especialidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        return medicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> criar(@RequestBody MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder) {
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