package com.example.clinicapi.model;

public enum StatusConsulta {

    /**
     * Indica que a consulta foi agendada e está aguardando realização.
     */
    AGENDADA("Agendada"),
    /**
     * Indica que a consulta foi cancelada antes de ser realizada.
     */
    CANCELADA("Cancelada"),
    /**
     * Indica que a consulta foi concluída.
     */
    REALIZADA("Realizada");

    /**
     * A descrição amigável do status da consulta.
     */
    private final String descricao;

    StatusConsulta(final String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável do status da consulta.
     *
     * @return A descrição do status.
     */
    public String getDescricao() {
        return descricao;
    }
}
