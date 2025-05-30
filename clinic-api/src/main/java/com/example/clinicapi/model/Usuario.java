package com.example.clinicapi.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um usuário do sistema, utilizado para autenticação e autorização.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("serial")
public final class Usuario implements UserDetails {

    /**
     * O identificador único do usuário.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome de login do usuário, utilizado para autenticação.
     */
    private String login;

    /**
     * A senha criptografada do usuário.
     */
    private String senha;

    /**
     * O papel (role) de acesso do usuário no sistema.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Construtor alternativo usado na criação
     * de novos usuários com role padrão.
     *
     * @param pLogin             nome de login do usuário
     * @param pSenhaCriptografada senha já criptografada
     */
    public Usuario(final String pLogin, final String pSenhaCriptografada) {
        this.login = pLogin;
        this.senha = pSenhaCriptografada;
        this.role = Role.ROLE_USER;
    }

    /**
     * Retorna as autoridades concedidas ao usuário.
     * Este método é utilizado pelo Spring Security para determinar
     * as permissões do usuário. Subclasses devem garantir que
     * todas as autoridades relevantes sejam retornadas.
     *
     * @return Uma coleção de GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     * Este método é utilizado pelo Spring Security. Subclasses devem
     * retornar a senha (geralmente criptografada).
     *
     * @return A senha do usuário.
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o nome de usuário utilizado para autenticar o usuário.
     * Este método é utilizado pelo Spring Security. Subclasses devem
     * retornar o nome de usuário único.
     *
     * @return O nome de usuário.
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Indica se a conta do usuário não expirou.
     * Este método é utilizado pelo Spring Security. Subclasses podem
     * implementar lógica de expiração de conta.
     *
     * @return true se a conta é válida (não expirada), false caso contrário.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário não está bloqueado.
     * Este método é utilizado pelo Spring Security. Subclasses podem
     * implementar lógica de bloqueio de conta.
     *
     * @return true se a conta não está bloqueada, false caso contrário.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais (senha) do usuário não expiraram.
     * Este método é utilizado pelo Spring Security. Subclasses podem
     * implementar lógica de expiração de credenciais.
     *
     * @return true se as credenciais são válidas (não expiradas),
     * false caso contrário.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Este método é utilizado pelo Spring Security. Subclasses podem
     * Indica se o usuário está habilitado ou desabilitado.
     * implementar lógica de habilitação/desabilitação de conta.
     *
     * @return true se o usuário está habilitado, false caso contrário.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
