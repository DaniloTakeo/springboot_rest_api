package com.example.clinicapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.clinicapi.dto.MedicoDTO;
import com.example.clinicapi.mapper.MedicoMapper;
import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;
import com.example.clinicapi.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    
    private final MedicoMapper medicoMapper;

    public List<MedicoDTO> findAll() {
        return medicoRepository.findAll()
                .stream()
                .map(medicoMapper::toDTO)
                .toList();
    }

    public Optional<MedicoDTO> findById(Long id) {
        return medicoRepository.findById(id)
                .map(medicoMapper::toDTO);
    }

    public List<MedicoDTO> findAllAtivos() {
        return medicoRepository.findAllAtivos()
                .stream()
                .map(medicoMapper::toDTO)
                .toList();
    }

    public List<MedicoDTO> findByEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidadeAndAtivoTrue(especialidade)
                .stream()
                .map(medicoMapper::toDTO)
                .toList();
    }
    
    public MedicoDTO save(MedicoDTO medicoDTO) {
        Medico medico = medicoMapper.toEntity(medicoDTO);
        return medicoMapper.toDTO(medicoRepository.save(medico));
    }

    public MedicoDTO update(Long id, MedicoDTO medicoDTO) {
        Medico medicoExistente = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        if (medicoDTO.nome() != null) medicoExistente.setNome(medicoDTO.nome());
        if (medicoDTO.crm() != null) medicoExistente.setCrm(medicoDTO.crm());
        if (medicoDTO.especialidade() != null) medicoExistente.setEspecialidade(medicoDTO.especialidade());
        if (medicoDTO.email() != null) medicoExistente.setEmail(medicoDTO.email());
        if (medicoDTO.telefone() != null) medicoExistente.setTelefone(medicoDTO.telefone());
        if (medicoDTO.ativo() != null) medicoExistente.setAtivo(medicoDTO.ativo());

        return medicoMapper.toDTO(medicoRepository.save(medicoExistente));
    }

    public void deleteById(Long id) {
        medicoRepository.deleteById(id);
    }
}