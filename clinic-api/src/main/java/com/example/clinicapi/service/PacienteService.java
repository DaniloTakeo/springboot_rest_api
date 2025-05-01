package com.example.clinicapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.PacienteDTO;
import com.example.clinicapi.mapper.PacienteMapper;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    
    private final PacienteMapper pacienteMapper = PacienteMapper.INSTANCE;

    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::toDTO) 
                .collect(Collectors.toList());
    }

    public Optional<PacienteDTO> findById(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toDTO); 
    }

    public List<PacienteDTO> findAllAtivos() {
        return pacienteRepository.findAllAtivos()
                .stream()
                .map(pacienteMapper::toDTO) 
                .collect(Collectors.toList());
    }

    public PacienteDTO save(PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
      
        Paciente pacienteSalvo = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(pacienteSalvo); 
    }
    
    public PacienteDTO update(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (dto.nome() != null) paciente.setNome(dto.nome());
        if (dto.email() != null) paciente.setEmail(dto.email());
        if (dto.cpf() != null) paciente.setCpf(dto.cpf());
        if (dto.telefone() != null) paciente.setTelefone(dto.telefone());
        if (dto.dataNascimento() != null) paciente.setDataNascimento(dto.dataNascimento());
        if (dto.ativo() != null) paciente.setAtivo(dto.ativo());

        return pacienteMapper.toDTO(pacienteRepository.save(paciente));
    }


    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }
}