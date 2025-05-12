package com.example.clinicapi.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.model.StatusConsulta;
import com.example.clinicapi.service.ConsultaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAgendarConsultaComSucesso() throws Exception {
        // Arrange
        ConsultaDTO request = new ConsultaDTO(null, 1L, 1L, LocalDateTime.now().plusDays(1), null, null);
        ConsultaDTO response = new ConsultaDTO(10L, 1L, 1L, request.dataHora(), null, StatusConsulta.AGENDADA);

        when(consultaService.createConsulta(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/consultas/10")))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarConsultaPorIdComSucesso() throws Exception {
        ConsultaDTO consultaDTO = new ConsultaDTO(10L, 1L, 1L, LocalDateTime.now(), null, StatusConsulta.AGENDADA);

        when(consultaService.findById(10L)).thenReturn(Optional.of(consultaDTO));

        mockMvc.perform(get("/consultas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar404AoBuscarConsultaInexistente() throws Exception {
        when(consultaService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/consultas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarConsultaComSucesso() throws Exception {
        ConsultaDTO request = new ConsultaDTO(null, 1L, 1L, LocalDateTime.now(), null, StatusConsulta.CANCELADA);
        ConsultaDTO updated = new ConsultaDTO(10L, 1L, 1L, request.dataHora(), null, StatusConsulta.CANCELADA);

        when(consultaService.updateConsulta(eq(10L), any())).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/consultas/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCancelarConsultaComSucesso() throws Exception {
        mockMvc.perform(delete("/consultas/10"))
                .andExpect(status().isNoContent());
        
        verify(consultaService).deleteById(10L);
    }
}