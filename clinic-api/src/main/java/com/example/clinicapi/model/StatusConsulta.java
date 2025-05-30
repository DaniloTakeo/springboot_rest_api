package com.example.clinicapi.model;

public enum StatusConsulta {
    AGENDADA("Agendada"),
    CANCELADA("Cancelada"),
    REALIZADA("Realizada");

    private final String descricao;

    StatusConsulta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
