package com.example.clinicapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
	
}