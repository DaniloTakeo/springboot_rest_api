package com.example.clinicapi.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

import lombok.extern.slf4j.Slf4j;

/**
 * Classe de configuração de segurança para a aplicação Spring Boot.
 * Define as regras de segurança HTTP,
 * o gerenciador de autenticação e o codificador de senhas.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     * Define as permissões de acesso aos endpoints,
     * gerenciamento de sessão e adição de filtros customizados.
     *
     * @param http O objeto {@link HttpSecurity}
     * usado para configurar a segurança da aplicação.
     * @param securityFilter O filtro de segurança customizado
     * que intercepta requisições para validação do JWT.
     * @param usuarioService Serviço responsável por autenticar
     * e carregar os dados do usuário.
     * @return A {@link SecurityFilterChain}
     * configurada com as regras definidas.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final SecurityFilter securityFilter,
            final AutenticacaoService usuarioService) throws Exception {
        log.info("Configurando SecurityFilterChain com endpoints "
                + "públicos e autenticação JWT.");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> {
                try {
                    auth
                        .requestMatchers(
                            "/auth/**",
                            "/auth",
                            "/usuarios",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/actuator",
                            "/actuator/**",
                            "/oauth2/**",
                            "/login/oauth2/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .oauth2Login(Customizer.withDefaults());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
     * @param http O objeto {@link HttpSecurity} utilizado
     * para acessar o {@link AuthenticationManagerBuilder}.
     * @param usuarioService Serviço responsável por carregar
     * os detalhes do usuário para autenticação.
     * @return O {@link AuthenticationManager} configurado.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    @SuppressWarnings("removal")
    public AuthenticationManager authenticationManager(
            final HttpSecurity http,
            final AutenticacaoService usuarioService) throws Exception {
        log.info("Criando AuthenticationManager com AutenticacaoService e "
                + "BCryptPasswordEncoder.");

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
