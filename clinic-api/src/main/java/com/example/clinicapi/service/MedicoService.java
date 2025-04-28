package com.example.clinicapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.repository.MedicoRepository;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Buscar todos os médicos
    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    // Buscar médico por ID
    public Optional<Medico> findById(Long id) {
        return medicoRepository.findById(id);
    }

    // Listar médicos ativos
    public List<Medico> findAllAtivos() {
        return medicoRepository.findAllAtivos();
    }

    // Buscar médicos ativos por especialidade
    public List<Medico> findByEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidadeAndAtivoTrue(especialidade);
    }

    // Criar ou atualizar médico
    public Medico save(Medico medico) {
        return medicoRepository.save(medico);
    }

    // Deletar médico por ID
    public void deleteById(Long id) {
        medicoRepository.deleteById(id);
    }
}