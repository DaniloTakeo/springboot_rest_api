package com.example.clinicapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Consulta;

/**
 * Interface de repositório para a entidade {@link Consulta}.
 * Fornece métodos de persistência para operações CRUD
 * (Criar, Ler, Atualizar, Excluir)
 * e funcionalidades de paginação e ordenação, estendendo JpaRepository.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

}
