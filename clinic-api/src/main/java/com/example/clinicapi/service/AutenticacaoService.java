package com.example.clinicapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.clinicapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável por carregar detalhes do usuário para autenticação
 * no Spring Security. Implementa a interface {@link UserDetailsService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    /**
     * Repositório para operações de acesso a dados de usuários.
     */
    private final UsuarioRepository repository;

    /**
     * Carrega os detalhes do usuário pelo seu nome de usuário (login).
     * Este método é utilizado pelo Spring Security
     * durante o processo de autenticação.
     *
     * @param username O nome de usuário (login) do usuário a ser carregado.
     * @return Os detalhes do usuário,
     * encapsulados em um objeto {@link UserDetails}.
     * @throws UsernameNotFoundException Se o usuário
     * com o nome de usuário fornecido não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        log.debug("Iniciando autenticação para o usuário: {}", username);
        return repository.findByLogin(username)
                .map(usuario -> {
                    log.info("Usuário encontrado: {}", username);
                    return usuario;
                })
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", username);
                    return new UsernameNotFoundException(
                            "Usuário não encontrado");
                });
    }
}
