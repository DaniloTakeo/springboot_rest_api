package com.example.clinicapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
	
	// Listar apenas médicos ativos
    @Query("SELECT m FROM Medico m WHERE m.ativo = true")
    List<Medico> findAllAtivos();

    // Buscar médicos ativos por especialidade
    @Query("SELECT m FROM Medico m WHERE m.ativo = true AND m.especialidade = :especialidade")
    List<Medico> findByEspecialidadeAndAtivoTrue(Especialidade especialidade);
}