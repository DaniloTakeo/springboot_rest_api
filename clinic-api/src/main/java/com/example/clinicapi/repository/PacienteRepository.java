package com.example.clinicapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Paciente;

/**
 * Interface de repositório para a entidade {@link Paciente}.
 * Fornece métodos de persistência para operações
 * CRUD (Criar, Ler, Atualizar, Excluir)
 * e funcionalidades de paginação e ordenação, estendendo JpaRepository.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca todos os pacientes ativos com paginação.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de objetos Paciente que estão ativos.
     */
    @Query("SELECT p FROM Paciente p WHERE p.ativo = true")
    Page<Paciente> findAllAtivos(Pageable pageable);
}
