package com.example.clinicapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Usuario;

/**
 * Interface de repositório para a entidade {@link Usuario}.
 * Fornece métodos de persistência para operações
 * CRUD (Criar, Ler, Atualizar, Excluir)
 * e funcionalidades de paginação e ordenação, estendendo JpaRepository.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu login.
     *
     * @param login O login do usuário.
     * @return Um Optional contendo o usuário,
     * se encontrado, ou um Optional vazio.
     */
    Optional<Usuario> findByLogin(String login);
}
