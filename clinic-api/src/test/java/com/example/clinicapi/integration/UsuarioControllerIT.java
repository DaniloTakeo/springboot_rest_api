package com.example.clinicapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIT extends TestBaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCadastrarUsuarioComSucesso() throws Exception {
        DadosAutenticacaoDTO dados = new DadosAutenticacaoDTO("novoUsuario", "senhaSegura123");

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarBadRequestQuandoLoginNaoInformado() throws Exception {
        DadosAutenticacaoDTO dados = new DadosAutenticacaoDTO(null, "senhaSegura123");

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.login").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarBadRequestQuandoSenhaNaoInformada() throws Exception {
        DadosAutenticacaoDTO dados = new DadosAutenticacaoDTO("usuarioTeste", null);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.senha").exists());
    }
}