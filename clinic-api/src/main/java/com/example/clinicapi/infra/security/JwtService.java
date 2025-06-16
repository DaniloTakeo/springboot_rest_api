package com.example.clinicapi.infra.security;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Serviço responsável por gerar e validar JSON Web Tokens (JWTs).
 * Gerencia a criação do token com base em um sujeito
 * e a extração do sujeito de um token existente.
 */
@Service
public final class JwtService {

    /**
     * Logger estático utilizado para registrar mensagens de log relacionadas à
     * execução da {@link JwtService}, como requisições recebidas,
     * operações bem-sucedidas, falhas e outras informações relevantes
     * durante o ciclo de vida da requisição.
     * <p>Utiliza a implementação do SLF4J fornecida pelo Logback.</p>
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JwtService.class);

    /**
     * A chave secreta utilizada para assinar e validar os tokens JWT,
     * carregada das propriedades da aplicação.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Define o tempo de expiração do token
     * em milissegundos por unidade de tempo.
     */
    private static final long MILLIS_PER_SECOND = 1000L;

    /**
     * Define o tempo de expiração do token
     * em milissegundos por unidade de tempo.
     */
    private static final long SECONDS_PER_MINUTE = 60L;

    /**
     * Define o tempo de expiração do token
     * em milissegundos por unidade de tempo.
     */
    private static final long MINUTES_PER_HOUR = 60L;
    /**
     * O tempo de expiração padrão para os tokens JWT, em horas.
     */
    private static final long TOKEN_EXPIRATION_HOURS = 2L;

    /**
     * Obtém a chave de assinatura para o JWT a partir da chave secreta.
     *
     * @return A chave de assinatura.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Gera um novo token JWT para o sujeito especificado.
     * O token inclui o sujeito, data de emissão e data de expiração.
     *
     * @param subject O sujeito do token (geralmente o login do usuário).
     * @return O token JWT gerado.
     */
    public String generateToken(final String subject) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime()
                + MILLIS_PER_SECOND * SECONDS_PER_MINUTE
                * MINUTES_PER_HOUR * TOKEN_EXPIRATION_HOURS);

        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        LOGGER.info("Token gerado para '{}', expira em {}",
                subject, expiration);

        return token;
    }

    /**
     * Extrai o sujeito (subject) de um token JWT.
     *
     * @param token O token JWT a ser parseado.
     * @return O sujeito do token.
     */
    public String getSubject(final String token) {
        try {
            String subject = Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            LOGGER.info("Subject extraído do token: '{}'", subject);
            return subject;
        } catch (JwtException e) {
            LOGGER.error("Erro ao extrair subject do token: {}",
                    e.getMessage());
            throw e;
        }
    }
}
