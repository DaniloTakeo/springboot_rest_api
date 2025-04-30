package com.example.clinicapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody DadosAutenticacaoDTO dados) {
        service.cadastrarUsuario(dados);
        return ResponseEntity.ok().build();
    }
}