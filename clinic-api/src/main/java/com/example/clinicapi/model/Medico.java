package com.example.clinicapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "medicos")
public class Medico {

	/**
     * O identificador único do médico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome completo do médico.
     */
    private String nome;

    /**
     * O número do Conselho Regional de Medicina (CRM) do médico.
     */
    private String crm;

    /**
     * A especialidade médica do profissional.
     */
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    /**
     * O endereço de email do médico.
     */
    private String email;

    /**
     * O número de telefone do médico.
     */
    private String telefone;

    /**
     * Indica se o cadastro do médico está ativo no sistema.
     */
    private boolean ativo;

    /**
     * Construtor para criar uma nova instância de Medico.
     * Define o status como ativo por padrão.
     *
     * @param pNome          O nome completo do médico.
     * @param pCrm           O número do CRM do médico.
     * @param pEspecialidade A especialidade do médico.
     * @param pEmail         O endereço de email do médico.
     * @param pTelefone      O número de telefone do médico.
     */
    public Medico(
    		final String pNome,
    		final String pCrm,
    		final Especialidade pEspecialidade,
    		final String pEmail,
    		final String pTelefone) {
        this.nome = pNome;
        this.crm = pCrm;
        this.especialidade = pEspecialidade;
        this.email = pEmail;
        this.telefone = pTelefone;
        this.ativo = true;
    }
}
