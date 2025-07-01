package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.clinicapi.infra.security.JwtService;

import io.jsonwebtoken.JwtException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "minha-chave-supersecreta-para-teste-123456"); // pelo menos 256 bits
    }

    @Test
    void deveGerarTokenValido() {
        String token = jwtService.generateToken("usuario123");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveExtrairSubjectDoTokenValido() {
        String token = jwtService.generateToken("usuario123");
        String subject = jwtService.getSubject(token);

        assertEquals("usuario123", subject);
    }

    @Test
    void deveLancarExcecaoParaTokenInvalido() {
        JwtException exception = assertThrows(
                JwtException.class,
                () -> jwtService.getSubject("token.invalido.aqui")
        );

        assertTrue(exception.getMessage().contains("Erro ao extrair subject do token JWT"));
    }
}