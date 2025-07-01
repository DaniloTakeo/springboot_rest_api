package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private AutenticacaoService service;

    @Test
    void deveRetornarUserDetailsQuandoUsuarioExistir() {
        String login = "admin";
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha("senha123");

        when(repository.findByLogin(login)).thenReturn(Optional.of(usuario));

        UserDetails resultado = service.loadUserByUsername(login);

        assertNotNull(resultado);
        assertEquals(login, resultado.getUsername());
        assertEquals("senha123", resultado.getPassword());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        String login = "inexistente";
        when(repository.findByLogin(login)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername(login)
        );

        assertEquals("Usuário não encontrado", ex.getMessage());
    }
}
