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

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarTodos() {
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<PacienteDTO>> listarAtivos() {
        return ResponseEntity.ok(pacienteService.findAllAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Long id) {
        return pacienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> criar(@RequestBody PacienteDTO pacienteDTO, UriComponentsBuilder uriBuilder) {
        PacienteDTO pacienteSalvo = pacienteService.save(pacienteDTO);
        URI location = uriBuilder
            .path("/pacientes/{id}")
            .buildAndExpand(pacienteSalvo.id())
            .toUri();
        
        return ResponseEntity.created(location).body(pacienteSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> atualizar(@PathVariable Long id, @RequestBody PacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}