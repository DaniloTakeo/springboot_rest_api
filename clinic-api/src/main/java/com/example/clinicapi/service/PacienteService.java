package com.example.clinicapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clinicapi.model.Paciente;
import com.example.clinicapi.repository.PacienteRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // Buscar todos os pacientes
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    // Buscar paciente por ID
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    // Listar todos os pacientes ativos
    public List<Paciente> findAllAtivos() {
        return pacienteRepository.findAllAtivos();
    }

    // Criar ou atualizar paciente
    public Paciente save(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // Deletar paciente por ID
    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }
}