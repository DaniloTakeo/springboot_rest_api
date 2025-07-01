package com.example.clinicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.mapper.MedicoMapper;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.repository.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private MedicoMapper medicoMapper;

    @InjectMocks
    private MedicoService medicoService;

    @Test
    void deveRetornarListaDeMedicoDTO() {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setNome("Dr. Mock");

        MedicoDTO dto = new MedicoDTO(1L, "Dr. Mock", "dr@mock.com", "1111", "99999999", Especialidade.CARDIOLOGIA, true);

        when(medicoRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(medico)));
        when(medicoMapper.toDTO(medico)).thenReturn(dto);

        Page<MedicoDTO> result = medicoService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Dr. Mock", result.getContent().get(0).nome());
    }
    
    @Test
    void deveRetornarListaVaziaQuandoNaoExistemMedicos() {
        when(medicoRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<MedicoDTO> result = medicoService.findAll(PageRequest.of(0, 10));

        assertEquals(0, result.getTotalElements());
    }
    
    @Test
    void deveRetornarMedicoDTOQuandoExistir() {
        Medico medico = new Medico(1L, "Dr. Mock", "1111", Especialidade.CARDIOLOGIA,"dr@mock.com", "99999999", true);
        MedicoDTO dto = new MedicoDTO(1L, "Dr. Mock", "dr@mock.com", "1111", "99999999", Especialidade.CARDIOLOGIA, true);

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoMapper.toDTO(medico)).thenReturn(dto);

        MedicoDTO result = medicoService.findById(1L).orElseThrow();

        assertEquals("Dr. Mock", result.nome());
        assertEquals("dr@mock.com", result.email());
    }
    
    @Test
    void deveLancarExcecaoQuandoMedicoNaoExistir() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, this::buscarMedicoOuFalhar);

        assertEquals("Médico não encontrado", exception.getMessage());
    }

    private MedicoDTO buscarMedicoOuFalhar() {
        return medicoService.findById(1L).orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
    }

    @Test
    void deveCriarMedico() {
        MedicoDTO dto = new MedicoDTO(1L, "Dr. Mock", "dr@mock.com", "1111", "99999999", Especialidade.CARDIOLOGIA, true);
        Medico medico = new Medico(1L, "Dr. Mock", "1111", Especialidade.CARDIOLOGIA,"dr@mock.com", "99999999", true);

        when(medicoMapper.toEntity(dto)).thenReturn(medico);
        when(medicoRepository.save(medico)).thenReturn(medico);
        when(medicoMapper.toDTO(medico)).thenReturn(dto);

        MedicoDTO result = medicoService.save(dto);

        assertNotNull(result);
        assertEquals("Dr. Mock", result.nome());
        assertEquals("dr@mock.com", result.email());
    }

    
    @Test
    void deveLancarExcecaoQuandoDadosInvalidos() {
        MedicoDTO medicoInvalidoDTO = new MedicoDTO(null, null, "dr@mock.com", "1111", "99999999", Especialidade.CARDIOLOGIA, true);

        Medico medicoInvalido = new Medico(null, null, "1111", Especialidade.CARDIOLOGIA, "dr@mock.com", null, true);
        when(medicoMapper.toEntity(medicoInvalidoDTO)).thenReturn(medicoInvalido);

        when(medicoRepository.save(any(Medico.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicoService.save(medicoInvalidoDTO);
        });

        assertEquals("Dados inválidos", exception.getMessage());
    }
}