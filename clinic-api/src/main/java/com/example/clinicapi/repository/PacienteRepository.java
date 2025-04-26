package com.example.clinicapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
}