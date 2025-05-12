package com.example.clinicapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PacienteControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;
    
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarPacientePorId() throws Exception {
        PacienteDTO dto = new PacienteDTO(1L, "João", "joao@email.com", "12345678900", "11999999999", 
                                           LocalDate.of(1990, 1, 1), true);

        given(pacienteService.findById(1L)).willReturn(Optional.of(dto));

        mockMvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarTodosPacientesPaginados() throws Exception {
        PacienteDTO paciente1 = new PacienteDTO(1L, "João", "joao@email.com", "12345678900", "11999999999",
                                                LocalDate.of(1990, 1, 1), true);
        PacienteDTO paciente2 = new PacienteDTO(2L, "Maria", "maria@email.com", "12345678901", "11888888888",
                                                LocalDate.of(1985, 5, 5), true);

        Pageable pageable = PageRequest.of(0, 2);
        Page<PacienteDTO> page = new PageImpl<>(List.of(paciente1, paciente2), pageable, 2);
        given(pacienteService.findAll(any(Pageable.class))).willReturn(page);
        
        mockMvc.perform(get("/pacientes?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("João"))
                .andExpect(jsonPath("$.content[1].nome").value("Maria"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarPacientesAtivosPaginados() throws Exception {
        PacienteDTO paciente1 = new PacienteDTO(1L, "Carlos", "carlos@email.com", "12345678902", "11777777777",
                                                LocalDate.of(1978, 3, 3), true);

        Pageable pageable = PageRequest.of(0, 1);
        Page<PacienteDTO> page = new PageImpl<>(List.of(paciente1), pageable, 1);
        given(pacienteService.findAllAtivos(any(Pageable.class))).willReturn(page);
                
        mockMvc.perform(get("/pacientes/ativos?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value("Carlos"))
                .andExpect(jsonPath("$.content[0].ativo").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar404SePacienteNaoExistir() throws Exception {
        given(pacienteService.findById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveSalvarPaciente() throws Exception {
        PacienteDTO dto = new PacienteDTO(null, "Maria", "maria@email.com", "12345678901", "11988887777", 
                                           LocalDate.of(1985, 5, 10), true);
        PacienteDTO salvo = new PacienteDTO(10L, dto.nome(), dto.email(), dto.cpf(), dto.telefone(), dto.dataNascimento(), dto.ativo());

        given(pacienteService.save(any())).willReturn(salvo);

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/pacientes/10")))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarPaciente() throws Exception {
        PacienteDTO dto = new PacienteDTO(1L, "Carlos", "carlos@email.com", "12345678909", "11944443333",
                                           LocalDate.of(1975, 7, 15), true);

        given(pacienteService.update(eq(1L), any())).willReturn(dto);

        mockMvc.perform(put("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveDeletarPaciente() throws Exception {
        mockMvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent());

        verify(pacienteService).deleteById(1L);
    }
}
