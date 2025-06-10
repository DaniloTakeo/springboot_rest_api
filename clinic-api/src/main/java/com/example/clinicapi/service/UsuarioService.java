package com.example.clinicapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela lógica de negócios relacionada a usuários,
 * como o cadastro de novos usuários e a criptografia de senhas.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    /**
     * Repositório para operações de persistência de usuários.
     */
    private final UsuarioRepository repository;
    /**
     * Componente para codificação de senhas.
     */
    private final PasswordEncoder encoder;

    /**
     * Cadastra um novo usuário no sistema, criptografando sua senha.
     *
     * @param dados O DTO contendo o login e a senha do novo usuário.
     */
    public void cadastrarUsuario(final DadosAutenticacaoDTO dados) {
        final String senhaCriptografada = encoder.encode(dados.senha());
        final Usuario usuario = new Usuario(dados.login(), senhaCriptografada);
        repository.save(usuario);
    }

    /**
     * Verifica se já existe um usuário cadastrado com o login informado.
     *
     * @param login O nome de login a ser verificado.
     * @return {@code true} se o login já estiver em uso,
     * {@code false} caso contrário.
     */
    public boolean loginJaExiste(final String login) {
        return repository.existsByLogin(login);
    }
}
