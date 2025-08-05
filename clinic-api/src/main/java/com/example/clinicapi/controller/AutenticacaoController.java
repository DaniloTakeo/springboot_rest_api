package com.example.clinicapi.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.dto.RefreshTokenRequest;
import com.example.clinicapi.dto.TokenResponse;
import com.example.clinicapi.infra.security.JwtService;
import com.example.clinicapi.model.RefreshToken;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.service.RefreshTokenService;
import com.example.clinicapi.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST responsável por autenticação e registro de usuários.
 * <p>
 * Fornece endpoints para login, cadastro e refresh de tokens JWT.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    /**
     * Gerenciador de autenticação para processar requisições de login.
     */
    private final AuthenticationManager manager;

    /**
     * Serviço responsável pelo cadastro e validação de usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Serviço para geração de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Serviço responsável pela criação e validação de refresh tokens.
     */
    private final RefreshTokenService refreshTokenService;

    /**
     * Realiza a autenticação de um usuário e gera um token JWT.
     * Também gera e retorna um refresh token.
     *
     * @param dados As credenciais de autenticação (login e senha) do usuário.
     * @return Um {@link TokenResponse} com access token e refresh token.
     */
    @PostMapping
    public TokenResponse login(@RequestBody @Valid
                               final DadosAutenticacaoDTO dados) {
        log.info("Tentativa de login para o usuário '{}'", dados.login());

        Authentication auth = manager.authenticate(
            new UsernamePasswordAuthenticationToken(
                dados.login(), dados.senha()));

        Usuario usuario = (Usuario) auth.getPrincipal();

        String accessToken = jwtService.generateToken(usuario.getLogin());
        String refreshToken = refreshTokenService
            .criarRefreshToken(usuario.getLogin()).getToken();

        log.info("Login bem-sucedido para o usuário '{}'",
            usuario.getLogin());

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * Cadastra um novo usuário com perfil padrão {@code ROLE_USER}.
     * Se bem-sucedido, retorna o access token e o refresh token gerados.
     *
     * @param dados DTO com login e senha do novo usuário.
     * @return {@link ResponseEntity} com status 201 e tokens gerados,
     * ou 409 se o login já estiver em uso.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> cadastrarUsuarioComToken(
            @RequestBody @Valid final DadosAutenticacaoDTO dados) {
        log.info("Tentativa de cadastro para o usuário '{}'", dados.login());

        if (usuarioService.loginJaExiste(dados.login())) {
            log.warn("Cadastro negado: login '{}' já está em uso",
                dados.login());

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioService.cadastrarUsuario(dados);

        String accessToken = jwtService.generateToken(dados.login());
        String refreshToken = refreshTokenService
            .criarRefreshToken(dados.login()).getToken();

        log.info("Usuário '{}' cadastrado com sucesso", dados.login());

        URI location = URI.create("/auth/" + dados.login());

        return ResponseEntity.created(location)
            .body(new TokenResponse(accessToken, refreshToken));
    }

    /**
     * Endpoint responsável por renovar o access token a partir de um
     * refresh token válido.
     *
     * @param request {@link RefreshTokenRequest} contendo o refresh token.
     * @return {@link TokenResponse} com novo access token
     * e o mesmo refresh token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody final RefreshTokenRequest request) {
        log.info("Requisição de refresh token recebida");

        RefreshToken token = refreshTokenService
            .verificarValidade(request.refreshToken());

        String newAccessToken = jwtService
            .generateToken(token.getUsuario().getLogin());

        log.info("Novo access token gerado com sucesso para '{}'",
            token.getUsuario().getLogin());

        return ResponseEntity.ok(
            new TokenResponse(newAccessToken, token.getToken()));
    }
}
