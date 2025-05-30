package com.example.clinicapi.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enumeração que define os papéis (roles) de acesso dos usuários.
 * Cada role representa uma autoridade reconhecida pelo Spring Security.
 */
public enum Role implements GrantedAuthority {

	/**
     * Representa um usuário comum com permissões básicas.
     */
    ROLE_USER,
    
    /**
     * Representa um administrador com permissões elevadas.
     */
    ROLE_ADMIN;

    /**
     * Retorna o nome da role como autoridade.
     * Usado internamente pelo Spring Security.
     *
     * @return o nome da role (ex: ROLE_USER)
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
