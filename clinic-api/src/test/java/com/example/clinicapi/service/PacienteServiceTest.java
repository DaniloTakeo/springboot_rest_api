package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.mapper.PacienteMapper;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void deveRetornarListaDePacienteDTO() {
        Paciente paciente = new Paciente(1L, "João", "joao@email.com", "12345678900", "99999999", LocalDate.of(1990, 1, 1), true);
        PacienteDTO dto = new PacienteDTO(1L, "João", "joao@email.com", "12345678900", "99999999", LocalDate.of(1990, 1, 1), true);

        when(pacienteRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(paciente)));
        when(pacienteMapper.toDTO(paciente)).thenReturn(dto);

        Page<PacienteDTO> result = pacienteService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("João", result.getContent().get(0).nome());
    }
    
    @Test
    void deveRetornarPacientePorId() {
        Paciente paciente = new Paciente(1L, "João", "joao@email.com", "12345678901", "11999999999", LocalDate.of(1990, 1, 1), true);
        PacienteDTO dto = new PacienteDTO(1L, "João", "joao@email.com", "12345678901", "11999999999", LocalDate.of(1990, 1, 1), true);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteMapper.toDTO(paciente)).thenReturn(dto);

        Optional<PacienteDTO> result = pacienteService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("João", result.get().nome());
    }
    
    @Test
    void deveLancarExcecaoQuandoPacienteNaoExistir() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PacienteDTO> result = pacienteService.findById(1L);

        assertTrue(result.isEmpty());
    }
    
    @Test
    void deveRetornarPacientesAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Paciente paciente = new Paciente(1L, "Maria", "maria@email.com", "12345678901", "11999999999", LocalDate.of(1985, 5, 5), true);
        PacienteDTO dto = new PacienteDTO(1L, "Maria", "maria@email.com", "12345678901", "11999999999", LocalDate.of(1985, 5, 5), true);
        Page<Paciente> pacientes = new PageImpl<>(List.of(paciente));

        when(pacienteRepository.findAllAtivos(pageable)).thenReturn(pacientes);
        when(pacienteMapper.toDTO(paciente)).thenReturn(dto);

        Page<PacienteDTO> result = pacienteService.findAllAtivos(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Maria", result.getContent().get(0).nome());
    }
    
    @Test
    void deveAtualizarPaciente() {
        Long id = 1L;
        Paciente paciente = new Paciente(id, "Antigo", "antigo@email.com", "11111111111", "11988888888", LocalDate.of(1980, 1, 1), true);
        PacienteDTO dtoAtualizado = new PacienteDTO(id, "Novo Nome", "novo@email.com", "11111111111", "11988888888", LocalDate.of(1980, 1, 1), true);
        Paciente pacienteAtualizado = new Paciente(id, "Novo Nome", "novo@email.com", "11111111111", "11988888888", LocalDate.of(1980, 1, 1), true);

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteAtualizado);
        when(pacienteMapper.toDTO(pacienteAtualizado)).thenReturn(dtoAtualizado);

        PacienteDTO result = pacienteService.update(id, dtoAtualizado);

        assertEquals("Novo Nome", result.nome());
        assertEquals("novo@email.com", result.email());
    }
    
    @Test
    void deveExcluirPacientePorId() {
        Long id = 1L;

        doNothing().when(pacienteRepository).deleteById(id);

        pacienteService.deleteById(id);

        verify(pacienteRepository, times(1)).deleteById(id);
    }
    
    @Test
    void deveSalvarNovoPaciente() {
        PacienteDTO dtoEntrada = new PacienteDTO(null, "Carlos", "carlos@email.com", "98765432100", "11911111111", LocalDate.of(1995, 3, 15), true);
        Paciente paciente = new Paciente(null, "Carlos", "carlos@email.com", "98765432100", "11911111111", LocalDate.of(1995, 3, 15), true);
        Paciente pacienteSalvo = new Paciente(1L, "Carlos", "carlos@email.com", "98765432100", "11911111111", LocalDate.of(1995, 3, 15), true);
        PacienteDTO dtoRetorno = new PacienteDTO(1L, "Carlos", "carlos@email.com", "98765432100", "11911111111", LocalDate.of(1995, 3, 15), true);

        when(pacienteMapper.toEntity(dtoEntrada)).thenReturn(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(pacienteSalvo);
        when(pacienteMapper.toDTO(pacienteSalvo)).thenReturn(dtoRetorno);

        PacienteDTO resultado = pacienteService.save(dtoEntrada);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.nome());
        assertEquals("carlos@email.com", resultado.email());
    }
}
