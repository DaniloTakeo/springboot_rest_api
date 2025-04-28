package com.example.clinicapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
	// Listar apenas pacientes ativos
    @Query("SELECT p FROM Paciente p WHERE p.ativo = true")
    List<Paciente> findAllAtivos();
}