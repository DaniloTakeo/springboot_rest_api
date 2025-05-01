package com.example.clinicapi.controller;

import java.net.URI;

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

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaDTO> agendar(
            @RequestBody ConsultaDTO consultaDTO,
            UriComponentsBuilder uriBuilder) {
        ConsultaDTO createdConsulta = consultaService.createConsulta(consultaDTO);

        URI uri = uriBuilder.path("/consultas/{id}").buildAndExpand(createdConsulta.id()).toUri();
        
        return ResponseEntity.created(uri).body(createdConsulta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return consultaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> atualizar(@PathVariable Long id, @RequestBody ConsultaDTO consultaDTO) {
        return consultaService.updateConsulta(id, consultaDTO)
                .map(updatedConsulta -> ResponseEntity.ok(updatedConsulta))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        consultaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}