package com.example.clinicapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void cadastrarUsuario(DadosAutenticacaoDTO dados) {
        String senhaCriptografada = encoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados.login(), senhaCriptografada);
        repository.save(usuario);
    }
}