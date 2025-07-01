package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComSenhaCriptografada() {
        DadosAutenticacaoDTO dto = new DadosAutenticacaoDTO("user123", "senha123");

        String senhaCriptografada = "$2a$10$abcdefg"; // Simula BCrypt
        when(encoder.encode("senha123")).thenReturn(senhaCriptografada);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        usuarioService.cadastrarUsuario(dto);

        verify(repository).save(captor.capture());
        Usuario usuarioSalvo = captor.getValue();

        assertEquals("user123", usuarioSalvo.getLogin());
        assertEquals(senhaCriptografada, usuarioSalvo.getPassword());
    }

    @Test
    void deveRetornarTrueQuandoLoginJaExiste() {
        when(repository.existsByLogin("user123")).thenReturn(true);

        boolean resultado = usuarioService.loginJaExiste("user123");

        assertTrue(resultado);
    }

    @Test
    void deveRetornarFalseQuandoLoginNaoExiste() {
        when(repository.existsByLogin("user123")).thenReturn(false);

        boolean resultado = usuarioService.loginJaExiste("user123");

        assertFalse(resultado);
    }
}