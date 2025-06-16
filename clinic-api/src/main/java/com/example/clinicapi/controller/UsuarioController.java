package com.example.clinicapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public final class UsuarioController {

    /**
     * Serviço responsável pela lógica de negócios para operações de usuário.
     */
    private final UsuarioService service;

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param dados As credenciais de autenticação (login e senha)
     * do novo usuário.
     * @return ResponseEntity com status 200 OK após o cadastro.
     */
    @PostMapping
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid final DadosAutenticacaoDTO dados) {
        log.info("Requisição recebida para cadastro de novo usuário:"
                + " login={}", dados.login());
        service.cadastrarUsuario(dados);
        log.info("Usuário cadastrado com sucesso: login={}", dados.login());
        return ResponseEntity.ok().build();
    }
}
