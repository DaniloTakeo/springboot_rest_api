package com.example.clinicapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.dto.TokenDTO;
import com.example.clinicapi.infra.security.JwtService;
import com.example.clinicapi.model.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	@Autowired
    private AuthenticationManager manager;
	
	@Autowired
    private JwtService jwtService;

    @PostMapping
    public TokenDTO login(@RequestBody @Valid DadosAutenticacaoDTO dados) {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.login(), dados.senha())
        );
        
        var usuario = (Usuario) auth.getPrincipal();
        var token = jwtService.generateToken(usuario.getLogin());
        return new TokenDTO(token);
    }
}