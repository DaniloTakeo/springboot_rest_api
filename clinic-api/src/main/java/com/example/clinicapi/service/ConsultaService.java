package com.example.clinicapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.ConsultaDTO;
import com.example.clinicapi.mapper.ConsultaMapper;
import com.example.clinicapi.model.Consulta;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.model.StatusConsulta;
import com.example.clinicapi.repository.ConsultaRepository;
import com.example.clinicapi.repository.MedicoRepository;
import com.example.clinicapi.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaMapper consultaMapper;

    public ConsultaDTO createConsulta(ConsultaDTO consultaDTO) {
        Optional<Paciente> paciente = pacienteRepository.findById(consultaDTO.pacienteId());
        Optional<Medico> medico = medicoRepository.findById(consultaDTO.medicoId());

        if (paciente.isPresent() && medico.isPresent()) {
            Consulta consulta = new Consulta();
            consulta.setPaciente(paciente.get());
            consulta.setMedico(medico.get());
            consulta.setDataHora(consultaDTO.dataHora());
            consulta.setStatus(StatusConsulta.AGENDADA);

            Consulta savedConsulta = consultaRepository.save(consulta);

            return consultaMapper.toDTO(savedConsulta);
        }

        throw new IllegalArgumentException("Paciente ou Médico não encontrados");
    }
    
    public Page<ConsultaDTO> findAll(Pageable pageable) {
        return consultaRepository.findAll(pageable)
                .map(consultaMapper::toDTO);
    }

    public Optional<ConsultaDTO> findById(Long id) {
        return consultaRepository.findById(id).map(consultaMapper::toDTO);
    }

    public void deleteById(Long id) {
        consultaRepository.deleteById(id);
    }
    
    public Optional<ConsultaDTO> updateConsulta(Long id, ConsultaDTO consultaDTO) {
        Optional<Consulta> existingConsulta = consultaRepository.findById(id);

        if (existingConsulta.isPresent()) {
            Consulta consulta = existingConsulta.get();

            if (consultaDTO.pacienteId() != null) {
                Optional<Paciente> paciente = pacienteRepository.findById(consultaDTO.pacienteId());
                paciente.ifPresent(consulta::setPaciente);
            }
            if (consultaDTO.medicoId() != null) {
                Optional<Medico> medico = medicoRepository.findById(consultaDTO.medicoId());
                medico.ifPresent(consulta::setMedico);
            }
            if (consultaDTO.dataHora() != null) {
                consulta.setDataHora(consultaDTO.dataHora());
            }
            if (consultaDTO.status() != null) {
                consulta.setStatus(consultaDTO.status());
            }

            Consulta updatedConsulta = consultaRepository.save(consulta);
            return Optional.of(consultaMapper.toDTO(updatedConsulta));
        }

        throw new IllegalArgumentException("Consulta não encontrada para o ID fornecido");
    }
}