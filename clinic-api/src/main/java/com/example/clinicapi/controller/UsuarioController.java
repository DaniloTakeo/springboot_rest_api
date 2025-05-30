package com.example.clinicapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public final class UsuarioController { // Torne a classe final

    /**
     * Serviço responsável pela lógica de negócios para operações de usuário.
     */
    @Autowired
    private UsuarioService service;

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param dados As credenciais de autenticação (login e senha)
     * do novo usuário.
     * @return ResponseEntity com status 200 OK após o cadastro.
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid final DadosAutenticacaoDTO dados) {
        service.cadastrarUsuario(dados);
        return ResponseEntity.ok().build();
    }
}
