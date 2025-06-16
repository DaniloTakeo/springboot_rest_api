package com.example.clinicapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * Logger estático utilizado para registrar mensagens de log relacionadas à
     * execução da {@link UsuarioService}, como requisições recebidas,
     * operações bem-sucedidas, falhas e outras informações relevantes
     * durante o ciclo de vida da requisição.
     * <p>Utiliza a implementação do SLF4J fornecida pelo Logback.</p>
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UsuarioService.class);

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
        LOGGER.info("Iniciando cadastro para login '{}'", dados.login());

        final String senhaCriptografada = encoder.encode(dados.senha());
        final Usuario usuario = new Usuario(dados.login(), senhaCriptografada);
        repository.save(usuario);

        LOGGER.info("Usuário '{}' cadastrado com sucesso", dados.login());
    }

    /**
     * Verifica se já existe um usuário cadastrado com o login informado.
     *
     * @param login O nome de login a ser verificado.
     * @return {@code true} se o login já estiver em uso,
     * {@code false} caso contrário.
     */
    public boolean loginJaExiste(final String login) {
        boolean existe = repository.existsByLogin(login);

        if (existe) {
            LOGGER.warn("Login '{}' já está em uso", login);
        } else {
            LOGGER.info("Login '{}' disponível para cadastro", login);
        }

        return existe;
    }
}
