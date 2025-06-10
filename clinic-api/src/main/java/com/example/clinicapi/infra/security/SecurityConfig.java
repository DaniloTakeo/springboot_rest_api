package com.example.clinicapi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.clinicapi.service.AutenticacaoService;

/**
 * Classe de configuração de segurança para a aplicação Spring Boot.
 * Define as regras de segurança HTTP,
 * o gerenciador de autenticação e o codificador de senhas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * O filtro de segurança customizado para processamento de JWTs.
     */
    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Serviço responsável por carregar
     * os detalhes do usuário para autenticação.
     */
    @Autowired
    private AutenticacaoService usuarioService;

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     * Define as permissões de acesso aos endpoints,
     * gerenciamento de sessão e adição de filtros customizados.
     *
     * @param http O objeto HttpSecurity para configurar a segurança web.
     * @return A SecurityFilterChain configurada.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/auth",
                    "/usuarios",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator",
                    "/actuator/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura o gerenciador de autenticação.
     * Utiliza o serviço de detalhes do usuário
     * e o codificador de senhas para a autenticação.
     *
     * @param http O objeto HttpSecurity
     * para obter o AuthenticationManagerBuilder.
     * @return O AuthenticationManager configurado.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    @SuppressWarnings("removal")
    public AuthenticationManager authenticationManager(
            final HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Fornece uma instância do
     * codificador de senhas BCryptPasswordEncoder.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
