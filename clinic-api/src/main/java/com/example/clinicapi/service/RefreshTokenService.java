package com.example.clinicapi.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.clinicapi.model.RefreshToken;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.RefreshTokenRepository;
import com.example.clinicapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável por gerenciar os refresh tokens utilizados
 * para renovar tokens de acesso (JWT) sem exigir novo login.
 *
 * Fornece métodos para criação, validação e remoção de refresh tokens,
 * garantindo controle seguro de sessões autenticadas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    /**
     * Repositório para operações com a entidade {@link RefreshToken}.
     */
    private final RefreshTokenRepository repository;

    /**
     * Repositório de usuários para associação com tokens.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Tempo de expiração do refresh token em milissegundos.
     * Configurado via application.properties.
     */
    @Value("${security.jwt.refresh-token-expiration-ms}")
    private Long refreshTokenDurationMs;

    /**
     * Cria e persiste um novo refresh token para o usuário informado.
     *
     * @param username login do usuário
     * @return {@link RefreshToken} gerado e salvo no banco
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public RefreshToken criarRefreshToken(final String username) {
        log.info("Criando refresh token para o usuário '{}'", username);

        Usuario usuario = usuarioRepository.findByLogin(username)
            .orElseThrow(() -> {
                log.warn("Usuário '{}' não encontrado ao gerar token",
                        username);
                return new UsernameNotFoundException("Usuário não encontrado");
            });

        RefreshToken token = new RefreshToken();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setDataExpiracao(
            Instant.now().plusMillis(refreshTokenDurationMs)
        );

        RefreshToken salvo = repository.save(token);

        log.info("Refresh token criado para usuário '{}'. Expira em {}",
            username, salvo.getDataExpiracao());

        return salvo;
    }

    /**
     * Verifica se o refresh token informado é válido. Se expirado,
     * remove-o do banco e lança exceção.
     *
     * @param token valor do refresh token
     * @return {@link RefreshToken} válido
     * @throws RuntimeException se o token for inexistente ou expirado
     */
    public RefreshToken verificarValidade(final String token) {
        log.info("Verificando validade do refresh token");

        RefreshToken refreshToken = repository.findByToken(token)
            .orElseThrow(() -> {
                log.warn("Token de refresh inválido ou inexistente");
                return new RuntimeException("Token não encontrado");
            });

        if (refreshToken.getDataExpiracao().isBefore(Instant.now())) {
            log.warn("Refresh token expirado em {}, removendo do banco",
                refreshToken.getDataExpiracao());
            repository.delete(refreshToken);
            throw new RuntimeException("Token expirado. "
                    + "Faça login novamente.");
        }

        log.info("Refresh token válido até {}",
                refreshToken.getDataExpiracao());
        return refreshToken;
    }

    /**
     * Exclui o refresh token associado ao usuário informado.
     *
     * @param usuario usuário cujo token deve ser removido
     */
    public void excluirPorUsuario(final Usuario usuario) {
        log.info("Removendo refresh token associado ao usuário '{}'",
            usuario.getLogin());
        repository.deleteByUsuario(usuario);
        log.info("Refresh token removido com sucesso");
    }
}
