package com.example.clinicapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.clinicapi.model.Especialidade;
import com.example.clinicapi.model.Medico;

/**
 * Interface de repositório para a entidade {@link Medico}.
 * Fornece métodos de persistência para operações
 * CRUD (Criar, Ler, Atualizar, Excluir)
 * e funcionalidades de paginação e ordenação, estendendo JpaRepository.
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Busca todos os médicos ativos com paginação.
     *
     * @param pageable Objeto Pageable para paginação e ordenação.
     * @return Uma página de objetos Medico que estão ativos.
     */
    @Query("SELECT m FROM Medico m WHERE m.ativo = true")
    Page<Medico> findAllAtivos(Pageable pageable);

    /**
     * Busca médicos ativos por especialidade com paginação.
     *
     * @param especialidade A especialidade do médico.
     * @param pageable      Objeto Pageable para paginação e ordenação.
     * @return Uma página de objetos Medico
     * que estão ativos e possuem a especialidade fornecida.
     */
    @Query("SELECT m FROM Medico m WHERE m.ativo = "
            + "true AND m.especialidade = :especialidade")
    Page<Medico> findByEspecialidadeAndAtivoTrue(Especialidade especialidade,
            Pageable pageable);
}
