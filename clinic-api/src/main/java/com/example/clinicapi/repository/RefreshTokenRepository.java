package com.example.clinicapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.RefreshToken;
import com.example.clinicapi.model.Usuario;

/**
 * Repositório responsável pelas operações de acesso a dados da entidade
 * {@link RefreshToken}. Estende {@link JpaRepository} para fornecer
 * operações padrão como salvar, atualizar, excluir e buscar.
 *
 * Também define métodos customizados para buscar e remover tokens com base
 * no valor do token ou no usuário associado.
 */
@Repository
public interface RefreshTokenRepository extends
    JpaRepository<RefreshToken, Long> {

    /**
     * Busca um refresh token pelo seu valor (string).
     *
     * @param token valor do refresh token
     * @return um {@link Optional} contendo o token, se encontrado
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Remove o refresh token associado ao usuário informado.
     *
     * @param usuario o usuário cujo token deve ser removido
     */
    void deleteByUsuario(Usuario usuario);
}
