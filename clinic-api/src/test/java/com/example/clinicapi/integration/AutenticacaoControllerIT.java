package com.example.clinicapi.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.clinicapi.dto.DadosAutenticacaoDTO;
import com.example.clinicapi.dto.TokenResponse;
import com.example.clinicapi.model.Usuario;
import com.example.clinicapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AutenticacaoControllerIT extends TestBaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarNovoUsuarioERetornarToken() throws Exception {
        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO("novo_usuario", "senhaSegura123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/auth/novo_usuario"))
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void deveRetornarConflictQuandoLoginJaExistir() throws Exception {
        String loginExistente = "usuario_existente";
        String senha = "senha123";

        Usuario usuario = new Usuario(loginExistente,
            passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);

        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO(loginExistente, senha);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRealizarLoginEReceberToken() throws Exception {
        String login = "usuario_login";
        String senha = "senhaLogin";

        Usuario usuario = new Usuario(login, passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);

        DadosAutenticacaoDTO dados = new DadosAutenticacaoDTO(login, senha);

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void deveRetornarErroAoTentarLoginComCredenciaisInvalidas() throws Exception {
        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO("inexistente", "senhaErrada");

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenGeradoDeveSerDecodificavel() throws Exception {
        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO("usuario_token", "senha123");

        Usuario usuario = new Usuario(dados.login(),
            passwordEncoder.encode(dados.senha()));
        usuarioRepository.save(usuario);

        MvcResult result = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andReturn();

        String respostaJson = result.getResponse().getContentAsString();
        TokenResponse tokenResponse =
            objectMapper.readValue(respostaJson, TokenResponse.class);

        assertNotNull(tokenResponse.accessToken());
        assertNotNull(tokenResponse.refreshToken());
        assertTrue(tokenResponse.accessToken().startsWith("eyJ"));
    }

    @Test
    void cadastroDeveEstarAbertoSemAutenticacao() throws Exception {
        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO("livre", "livre123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated());
    }

    @Test
    void loginDeveEstarAbertoSemAutenticacao() throws Exception {
        Usuario usuario =
            new Usuario("publico", passwordEncoder.encode("12345"));
        usuarioRepository.save(usuario);

        DadosAutenticacaoDTO dados =
            new DadosAutenticacaoDTO("publico", "12345");

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk());
    }

    @Nested
    class ValidacaoCamposTest {

        @Test
        void deveRetornarBadRequestQuandoLoginNaoInformadoNoCadastro()
                throws Exception {
            DadosAutenticacaoDTO dados =
                new DadosAutenticacaoDTO(null, "senhaValida123");

            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dados)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornarBadRequestQuandoSenhaNaoInformadaNoCadastro()
                throws Exception {
            DadosAutenticacaoDTO dados =
                new DadosAutenticacaoDTO("usuario", null);

            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dados)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornarBadRequestQuandoLoginNaoInformadoNoLogin()
                throws Exception {
            DadosAutenticacaoDTO dados =
                new DadosAutenticacaoDTO(null, "senha123");

            mockMvc.perform(post("/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dados)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornarBadRequestQuandoSenhaNaoInformadaNoLogin()
                throws Exception {
            DadosAutenticacaoDTO dados =
                new DadosAutenticacaoDTO("usuario", null);

            mockMvc.perform(post("/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dados)))
                    .andExpect(status().isBadRequest());
        }
    }
}