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
import com.example.clinicapi.dto.TokenDTO;
import com.example.clinicapi.infra.security.JwtService;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    /**
     * Gerenciador de autenticação para processar as requisições de login.
     */
    private final AuthenticationManager manager;

    /**
     * Serviço responsável pelo cadastro de novos usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Serviço para manipulação de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Realiza a autenticação de um usuário e gera um token JWT.
     *
     * @param dados As credenciais de autenticação (login e senha) do usuário.
     * @return Um TokenDTO contendo o token JWT gerado.
     */
    @PostMapping
    public TokenDTO login(
            @RequestBody @Valid final DadosAutenticacaoDTO dados) {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.login(),
                        dados.senha())
        );

        var usuario = (Usuario) auth.getPrincipal();
        var token = jwtService.generateToken(usuario.getLogin());
        return new TokenDTO(token);
    }

    /**
     * Cadastra um novo usuário no sistema, atribuindo o papel padrão
     * {@code ROLE_USER}.
     * A senha é automaticamente criptografada. Caso o login já esteja em uso,
     * uma resposta 409 (Conflict) será retornada.
     * Se o cadastro for bem-sucedido,
     * um token JWT é gerado e retornado no corpo da resposta, juntamente com o
     * cabeçalho {@code Location} apontando para o recurso recém-criado.
     *
     * @param dados Um DTO contendo o login e a senha do novo usuário.
     * @return Um {@code ResponseEntity} com status 201 (Created)
     * e token JWT no corpo,
     *         ou 409 (Conflict) se o login já estiver em uso.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenDTO> cadastrarUsuarioComToken(
            @RequestBody @Valid final DadosAutenticacaoDTO dados) {

        if (usuarioService.loginJaExiste(dados.login())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioService.cadastrarUsuario(dados);

        final URI location = URI.create("/auth/" + dados.login());
        final String token = jwtService.generateToken(dados.login());

        return ResponseEntity
                .created(location)
                .body(new TokenDTO(token));
    }
}
