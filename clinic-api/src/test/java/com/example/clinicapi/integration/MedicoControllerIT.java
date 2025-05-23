package com.example.clinicapi.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.repository.MedicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MedicoControllerIT {
	
	@Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("test_db")
        .withUsername("testuser")
        .withPassword("testpass");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
	    registry.add("spring.datasource.url", mysql::getJdbcUrl);
	    registry.add("spring.datasource.username", mysql::getUsername);
	    registry.add("spring.datasource.password", mysql::getPassword);
	    registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
	    registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
	    registry.add("spring.flyway.url", mysql::getJdbcUrl);
	    registry.add("spring.flyway.user", mysql::getUsername);
	    registry.add("spring.flyway.password", mysql::getPassword);
	    registry.add("spring.flyway.enabled", () -> "true");
	}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarENaoListarMedicoInativo() throws Exception {
        MedicoDTO medicoDTO = new MedicoDTO(null, "Dra. Carla", "carla@email.com", "123456", null, Especialidade.DERMATOLOGIA, false);

        mockMvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/medicos/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarEListarMedicoAtivo() throws Exception {
        MedicoDTO medicoDTO = new MedicoDTO(null, "Dr. João", "joao@email.com", "654321", null, Especialidade.CARDIOLOGIA, true);

        mockMvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/medicos/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Dr. João"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarMedico() throws Exception {
        Medico medico = new Medico(null, "Dr. Fabio", "111111", Especialidade.PEDIATRIA, "fabio@email.com", "123456789", true);
        medico = medicoRepository.save(medico);

        MedicoDTO atualizado = new MedicoDTO(null, "Dr. Fabio Atualizado", medico.getEmail(), medico.getCrm(), medico.getTelefone(), medico.getEspecialidade(), true);

        mockMvc.perform(put("/medicos/" + medico.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Fabio Atualizado"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveExcluirMedico() throws Exception {
        Medico medico = new Medico(null, "Dr. Miguel", "999999", Especialidade.DERMATOLOGIA, "miguel@email.com", "123456789", true);
        medico = medicoRepository.save(medico);

        mockMvc.perform(delete("/medicos/" + medico.getId()))
                .andExpect(status().isNoContent());

        Optional<Medico> encontrado = medicoRepository.findById(medico.getId());
        assertTrue(encontrado.isEmpty() || !encontrado.get().getAtivo());
    }
}