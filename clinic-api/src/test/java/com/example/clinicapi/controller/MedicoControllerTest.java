package com.example.clinicapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.service.MedicoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicoControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarTodosMedicosPaginados() throws Exception {
        MedicoDTO medico1 = new MedicoDTO(1L, "Dra. Ana", "ana@email.com", "123456", null, Especialidade.CARDIOLOGIA, true);
        MedicoDTO medico2 = new MedicoDTO(2L, "Dr. Paulo", "paulo@email.com", "654321", null, Especialidade.ORTOPEDIA, true);

        Pageable pageable = PageRequest.of(0, 2);
        Page<MedicoDTO> page = new PageImpl<>(List.of(medico1, medico2), pageable, 2);

        given(medicoService.findAll(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/medicos?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("Dra. Ana"))
                .andExpect(jsonPath("$.content[1].nome").value("Dr. Paulo"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarMedicosAtivosPaginados() throws Exception {
        MedicoDTO medico1 = new MedicoDTO(1L, "Dra. Ana", "ana@email.com", "123456", null, Especialidade.CARDIOLOGIA, true);
        Pageable pageable = PageRequest.of(0, 1);
        Page<MedicoDTO> page = new PageImpl<>(List.of(medico1), pageable, 1);

        given(medicoService.findAllAtivos(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/medicos/ativos?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].ativo").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarMedicoPorId() throws Exception {
        MedicoDTO medico = new MedicoDTO(1L, "Dra. Ana", "ana@email.com", "123456", null, Especialidade.CARDIOLOGIA, true);
        given(medicoService.findById(1L)).willReturn(Optional.of(medico));

        mockMvc.perform(get("/medicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Dra. Ana"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarMedico() throws Exception {
        MedicoDTO input = new MedicoDTO(1L, "Dra. Ana", "ana@email.com", "123456", null, Especialidade.CARDIOLOGIA, true);
        MedicoDTO output = new MedicoDTO(1L, input.nome(), input.email(), input.crm(), input.telefone(), input.especialidade(), true);

        given(medicoService.save(any(MedicoDTO.class))).willReturn(output);

        mockMvc.perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Dra. Ana"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarMedico() throws Exception {
        MedicoDTO input = new MedicoDTO(1L, "Dra. Ana", "ana@email.com", "123456", null, Especialidade.CARDIOLOGIA, true);
        MedicoDTO output = new MedicoDTO(1L, "Dra. Ana Atualizada", input.email(), input.crm(), input.telefone(), input.especialidade(), true);

        given(medicoService.update(eq(1L), any(MedicoDTO.class))).willReturn(output);

        mockMvc.perform(put("/medicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dra. Ana Atualizada"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveDeletarMedico() throws Exception {

        mockMvc.perform(delete("/medicos/1"))
                .andExpect(status().isNoContent());
        
        verify(medicoService).deleteById(1L);
    }
}
