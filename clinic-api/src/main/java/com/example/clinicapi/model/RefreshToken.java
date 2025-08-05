package com.example.clinicapi.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um Refresh Token utilizado
 * para renovar o token de acesso (JWT) de um usuário
 * sem a necessidade de reautenticação por login e senha.
 * <p>
 * Cada usuário pode possuir um único refresh token associado,
 * que tem uma data de expiração
 * e pode ser usado para gerar novos access tokens.
 * </p>
 *
 * <p>
 * Essa entidade é persistida no banco de dados através
 * do JPA e gerenciada pelo Flyway.
 * </p>
 *
 * @author Danilo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /**
     * Identificador único do refresh token.
     * Gerado automaticamente como chave primária da tabela.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * String única que representa o valor do refresh token.
     * Deve ser armazenado de forma segura e não pode se repetir.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Referência para o usuário associado a este refresh token.
     * Cada refresh token pertence a exatamente um usuário.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    /**
     * Data e hora de expiração deste refresh token.
     * Após essa data, o token não pode mais ser usado
     * para renovar o access token.
     */
    @Column(name = "data_expiracao", nullable = false)
    private Instant dataExpiracao;
}
