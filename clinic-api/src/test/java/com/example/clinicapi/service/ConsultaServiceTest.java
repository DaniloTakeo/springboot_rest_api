package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.mapper.ConsultaMapper;
import com.example.clinicapi.model.Consulta;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.model.StatusConsulta;
import com.example.clinicapi.repository.ConsultaRepository;
import com.example.clinicapi.repository.MedicoRepository;
import com.example.clinicapi.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private MedicoRepository medicoRepository;
    @Mock
    private ConsultaMapper consultaMapper;

    @InjectMocks
    private ConsultaService consultaService;
    
    @Test
    void deveCriarConsultaComPacienteEMedicoValidos() {
        ConsultaDTO dto = new ConsultaDTO(null, 1L, 1L, LocalDateTime.now(), null, StatusConsulta.AGENDADA);
        Paciente paciente = new Paciente();
        Medico medico = new Medico();
        Consulta consultaSalva = new Consulta();
        ConsultaDTO dtoRetorno = new ConsultaDTO(10L, 1L, 1L, dto.dataHora(), null, StatusConsulta.AGENDADA);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);
        when(consultaMapper.toDTO(consultaSalva)).thenReturn(dtoRetorno);

        ConsultaDTO resultado = consultaService.createConsulta(dto);

        assertEquals(StatusConsulta.AGENDADA, resultado.status());
        verify(consultaRepository).save(any(Consulta.class));
    }
    
    @Test
    void deveRetornarConsultaPorId() {
        Consulta consulta = new Consulta();
        ConsultaDTO dto = new ConsultaDTO(1L, 1L, 1L, LocalDateTime.now(), null, StatusConsulta.AGENDADA);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaMapper.toDTO(consulta)).thenReturn(dto);

        Optional<ConsultaDTO> result = consultaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(StatusConsulta.AGENDADA, result.get().status());
    }
    
    @Test
    void deveAtualizarConsultaExistente() {
        Long id = 1L;
        Consulta consultaExistente = new Consulta();
        Paciente paciente = new Paciente();
        Medico medico = new Medico();
        Consulta consultaAtualizada = new Consulta();
        ConsultaDTO dtoEntrada = new ConsultaDTO(id, 1L, 1L, LocalDateTime.now(), "Desistência", StatusConsulta.CANCELADA);
        ConsultaDTO dtoSaida = new ConsultaDTO(id, 1L, 1L, dtoEntrada.dataHora(), "Desistência", StatusConsulta.CANCELADA);

        when(consultaRepository.findById(id)).thenReturn(Optional.of(consultaExistente));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaAtualizada);
        when(consultaMapper.toDTO(consultaAtualizada)).thenReturn(dtoSaida);

        Optional<ConsultaDTO> result = consultaService.updateConsulta(id, dtoEntrada);

        assertTrue(result.isPresent());
        assertEquals(StatusConsulta.CANCELADA, result.get().status());
    }
    
    @Test
    void deveLancarExcecaoSeConsultaNaoForEncontradaParaAtualizacao() {
        Long id = 999L;
        ConsultaDTO dto = new ConsultaDTO(id, null, null, LocalDateTime.now(), "Motivo", StatusConsulta.CANCELADA);

        when(consultaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> consultaService.updateConsulta(id, dto));
    }
    
    @Test
    void deveExcluirConsultaPorId() {
        Long id = 1L;

        doNothing().when(consultaRepository).deleteById(id);

        consultaService.deleteById(id);

        verify(consultaRepository, times(1)).deleteById(id);
    }
}