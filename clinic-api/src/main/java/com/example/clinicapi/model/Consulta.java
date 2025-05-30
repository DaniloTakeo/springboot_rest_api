package com.example.clinicapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "consultas")
public class Consulta {

	/**
	 * O identificador único da consulta.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O paciente associado a esta consulta.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /**
     * O médico responsável por esta consulta.
     */
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    /**
     * Data e hora agendadas para a consulta.
     */
    @Column(nullable = false)
    private LocalDateTime dataHora;

    /**
     * O status atual da consulta (ex: AGENDADA, CANCELADA, REALIZADA).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;

    /**
     * Motivo do cancelamento da consulta, se aplicável.
     */
    @Column(nullable = true)
    private String motivoCancelamento;

}
