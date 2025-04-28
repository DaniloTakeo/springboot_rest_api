package com.example.clinicapi.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clinicapi.model.Consulta;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.ConsultaRepository;
import com.example.clinicapi.repository.MedicoRepository;
import com.example.clinicapi.repository.PacienteRepository;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // Criar uma consulta
    public Consulta createConsulta(Long pacienteId, Long medicoId, String dataHora) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        Optional<Medico> medico = medicoRepository.findById(medicoId);

        if (paciente.isPresent() && medico.isPresent()) {
            Consulta consulta = new Consulta();
            consulta.setPaciente(paciente.get());
            consulta.setMedico(medico.get());
            consulta.setDataHora(LocalDateTime.parse(dataHora));
            return consultaRepository.save(consulta);
        }

        throw new IllegalArgumentException("Paciente ou Médico não encontrados");
    }

    // Buscar consulta por ID
    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    // Deletar consulta por ID
    public void deleteById(Long id) {
        consultaRepository.deleteById(id);
    }
}