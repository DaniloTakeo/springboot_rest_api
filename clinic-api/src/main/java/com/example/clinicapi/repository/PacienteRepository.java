package com.example.clinicapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
	// Listar apenas pacientes ativos
    @Query("SELECT p FROM Paciente p WHERE p.ativo = true")
   Page<Paciente> findAllAtivos(Pageable pageable);
}