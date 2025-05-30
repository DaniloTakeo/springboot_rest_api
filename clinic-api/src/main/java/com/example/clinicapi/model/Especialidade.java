package com.example.clinicapi.model;

public enum Especialidade {
    /**
     * Especialidade médica focada no coração e sistema circulatório.
     */
    CARDIOLOGIA("Cardiologia"),
    /**
     * Especialidade médica focada na pele, cabelo e unhas.
     */
    DERMATOLOGIA("Dermatologia"),
    /**
     * Especialidade médica focada na saúde da mulher.
     */
    GINECOLOGIA("Ginecologia"),
    /**
     * Especialidade médica focada nos ossos, articulações, ligamentos, tendões e músculos.
     */
    ORTOPEDIA("Ortopedia"),
    /**
     * Especialidade médica focada na saúde e desenvolvimento de crianças.
     */
    PEDIATRIA("Pediatria");

    /**
     * A descrição amigável da especialidade médica.
     */
    private final String descricao;

    Especialidade(final String pDescricao) {
        this.descricao = pDescricao;
    }

    /**
     * Retorna a descrição amigável da especialidade.
     *
     * @return A descrição da especialidade.
     */
    public String getDescricao() {
        return descricao;
    }
}
