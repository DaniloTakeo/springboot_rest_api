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
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.model.Consulta;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.model.StatusConsulta;
import com.example.clinicapi.repository.ConsultaRepository;
import com.example.clinicapi.repository.MedicoRepository;
import com.example.clinicapi.repository.PacienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConsultaControllerIT extends TestBaseIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAgendarNovaConsulta() throws Exception {
        Paciente paciente = pacienteRepository.save(new Paciente(null, "Lucas", "lucas@email.com", "12345678900",
                "11999999999", LocalDate.of(1990, 1, 1), true));
        Medico medico = medicoRepository.save(new Medico(null, "Dr. Miguel", "999999", Especialidade.DERMATOLOGIA, "miguel@email.com", "123456789", true));

        ConsultaDTO dto = new ConsultaDTO(
                null,
                paciente.getId(),
                medico.getId(),
                LocalDateTime.now().plusDays(1),
                "Consulta de rotina",
                StatusConsulta.AGENDADA
        );
        
        mockMvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveListarConsultas() throws Exception {
        Paciente paciente = pacienteRepository.save(new Paciente(null, "Amanda", "amanda@email.com", "12345678902",
                "11777777777", LocalDate.of(1985, 4, 15), true));
        Medico medico = medicoRepository.save(new Medico(null, "Dr. Miguel", "999999", Especialidade.DERMATOLOGIA, "miguel@email.com", "123456789", true));

        Consulta consulta = new Consulta(null, paciente, medico, LocalDateTime.now().plusDays(2), StatusConsulta.REALIZADA, "Retorno");
        consultaRepository.save(consulta);

        mockMvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].motivoCancelamento").value("Retorno"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCancelarConsulta() throws Exception {
        Paciente paciente = pacienteRepository.save(new Paciente(null, "Carlos", "carlos@email.com", "12345678904",
                "11555555555", LocalDate.of(1970, 12, 12), true));
        Medico medico = medicoRepository.save(new Medico(null, "Dr. Miguel", "999999", Especialidade.DERMATOLOGIA, "miguel@email.com", "123456789", true));
        Consulta consulta = consultaRepository.save(new Consulta(null, paciente, medico, LocalDateTime.now().plusDays(3), StatusConsulta.AGENDADA, "Avaliação"));

        mockMvc.perform(delete("/consultas/" + consulta.getId()))
                .andExpect(status().isNoContent());

        Optional<Consulta> encontrada = consultaRepository.findById(consulta.getId());
        assertTrue(encontrada.isEmpty());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarConsultaPorId() throws Exception {
        Paciente paciente = pacienteRepository.save(new Paciente(null, "Julia", "julia@email.com", "12345678905",
                "11888888888", LocalDate.of(1995, 3, 10), true));
        Medico medico = medicoRepository.save(new Medico(null, "Dr. Pedro", "888888", Especialidade.PEDIATRIA, "pedro@email.com", "987654321", true));
        Consulta consulta = consultaRepository.save(new Consulta(null, paciente, medico, LocalDateTime.now().plusDays(2), StatusConsulta.AGENDADA, "Consulta pediátrica"));

        mockMvc.perform(get("/consultas/" + consulta.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(consulta.getId()))
                .andExpect(jsonPath("$.motivoCancelamento").value("Consulta pediátrica"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar404QuandoConsultaNaoEncontrada() throws Exception {
        mockMvc.perform(get("/consultas/999999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarConsulta() throws Exception {
        Paciente paciente = pacienteRepository.save(new Paciente(null, "Beatriz", "bia@email.com", "12345678906",
                "11777777777", LocalDate.of(1992, 7, 20), true));
        Medico medico = medicoRepository.save(new Medico(null, "Dr. Ana", "777777", Especialidade.GINECOLOGIA, "ana@email.com", "123456789", true));
        Consulta consulta = consultaRepository.save(new Consulta(null, paciente, medico, LocalDateTime.now().plusDays(5), StatusConsulta.AGENDADA, "Avaliação inicial"));

        ConsultaDTO atualizado = new ConsultaDTO(consulta.getId(), paciente.getId(), medico.getId(),
                consulta.getDataHora().plusDays(1), "Remarcada", StatusConsulta.AGENDADA);

        mockMvc.perform(put("/consultas/" + consulta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivoCancelamento").value("Remarcada"));
    }
}