package com.example.clinicapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
	
}
