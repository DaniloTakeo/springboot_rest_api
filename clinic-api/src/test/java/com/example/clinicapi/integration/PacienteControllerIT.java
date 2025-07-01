package com.example.clinicapi.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.PacienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PacienteControllerIT extends TestBaseIT {
	
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarEPersistirPaciente() throws Exception {
        PacienteDTO dto = new PacienteDTO(
            null,
            "Ana Souza",
            "ana@email.com",
            "12345678900",
            "11912345678",
            LocalDate.of(1995, 2, 15),
            true
        );

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarPacientesAtivos() throws Exception {
        Paciente paciente1 = new Paciente(null, "Carlos Lima", "carlos@email.com", "12345678901",
                "11999999999", LocalDate.of(1990, 1, 1), true);
        Paciente paciente2 = new Paciente(null, "Paula Silva", "paula@email.com", "12345678902",
                "11888888888", LocalDate.of(1988, 6, 10), false);

        pacienteRepository.saveAll(List.of(paciente1, paciente2));

        mockMvc.perform(get("/pacientes/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value("Carlos Lima"))
                .andExpect(jsonPath("$.content[0].ativo").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarPaciente() throws Exception {
        Paciente paciente = new Paciente(null, "Joana", "joana@email.com", "12345678903",
                "11777777777", LocalDate.of(1980, 3, 20), true);
        paciente = pacienteRepository.save(paciente);

        PacienteDTO atualizado = new PacienteDTO(null, "Joana Atualizada", paciente.getEmail(),
                paciente.getCpf(), paciente.getTelefone(), paciente.getDataNascimento(), true);

        mockMvc.perform(put("/pacientes/" + paciente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Joana Atualizada"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveExcluirPaciente() throws Exception {
        Paciente paciente = new Paciente(null, "Luis", "luis@email.com", "12345678904",
                "11777777777", LocalDate.of(1975, 4, 4), true);
        paciente = pacienteRepository.save(paciente);

        mockMvc.perform(delete("/pacientes/" + paciente.getId()))
                .andExpect(status().isNoContent());

        Optional<Paciente> encontrado = pacienteRepository.findById(paciente.getId());
        assertTrue(encontrado.isEmpty() || !encontrado.get().isAtivo());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar404QuandoPacienteNaoExistir() throws Exception {
        mockMvc.perform(get("/pacientes/{id}", 9999L))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarBadRequestQuandoNomeNaoInformado() throws Exception {
        PacienteDTO dto = new PacienteDTO(null, "", "email@email.com", "12345678900", "11999999999", LocalDate.of(2000,1,1), true);

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").exists());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRespeitarParametrosDePaginacao() throws Exception {
        mockMvc.perform(get("/pacientes")
                .param("page", "0")
                .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(50));
    }
}