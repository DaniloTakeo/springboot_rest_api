package com.example.clinicapi.infra.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.clinicapi.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro de segurança responsável por processar
 * tokens JWT para autenticação
 * e autorização de requisições.
 * Estende OncePerRequestFilter para garantir que o
 * filtro seja executado uma única vez por requisição.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class SecurityFilter extends OncePerRequestFilter {

    /**
     * Serviço para manipulação de JWTs (gerar, extrair sujeito).
     */
    private final JwtService jwtService;
    /**
     * Repositório para acessar informações de usuário no banco de dados.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Executa a lógica do filtro para cada requisição HTTP.
     * Recupera o token JWT, valida-o
     * e autentica o usuário se o token for válido.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @param chain    A cadeia de filtros.
     * @throws ServletException Se ocorrer um erro de servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {

        log.debug("Executando filtro de segurança para a requisição: {}",
                request.getRequestURI());

        final String token = recuperarToken(request);
        if (token != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            final String login = jwtService.getSubject(token);
            final var usuario = usuarioRepository
                    .findByLogin(login);

            if (usuario.isPresent()) {
                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usuario.get(),
                                null,
                                usuario.get()
                                .getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext()
                .setAuthentication(authentication);
                log.info("Usuário autenticado com sucesso: {}", login);
            } else {
                log.warn("Usuário não encontrado no banco de dados: {}",
                        login);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho
     * "Authorization" da requisição.
     *
     * @param request O objeto HttpServletRequest.
     * @return O token JWT (sem o prefixo "Bearer "),
     * ou null se não for encontrado.
     */
    private String recuperarToken(final HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        final String token = (header != null && header.startsWith("Bearer "))
                ? header.replace("Bearer ", "") : null;
        log.debug("Token extraído do cabeçalho Authorization: {}",
                token != null ? "[PROTEGIDO]" : "null");

        return token;
    }

    /**
     * Determina se o filtro deve ser aplicado à requisição atual.
     * Ignora o filtro para o endpoint de autenticação.
     *
     * @param request O objeto HttpServletRequest.
     * @return true se o filtro
     * NÃO deve ser executado, false caso contrário.
     * @throws ServletException Se ocorrer um erro de servlet.
     */
    @Override
    protected boolean shouldNotFilter(
            final HttpServletRequest request)
                    throws ServletException {
        final String path = request.getRequestURI();
        return path.equals("/auth");
    }
}
