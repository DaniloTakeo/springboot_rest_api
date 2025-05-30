package com.example.clinicapi.exception;

import java.io.Serializable;

public final class ErrorResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7484466542301213938L;

    /**
     * O código de erro da resposta, geralmente um código de status HTTP
     * ou um código de erro interno da aplicação.
     */

    private String code;
    /**
     * A mensagem de erro detalhada,
     * fornecendo mais informações sobre o problema.
     */

    private String message;

    /**
     * Construtor para criar uma nova instância de ErrorResponse.
     *
     * @param pCode    O código de erro.
     * @param pMessage A mensagem de erro.
     */

    public ErrorResponse(final String pCode, final String pMessage) {
        this.code = pCode;
        this.message = pMessage;
    }

    /**
     * Retorna o código de erro.
     *
     * @return O código de erro.
     */

    public String getCode() {
        return code;
    }

    /**
     * Define o código de erro.
     *
     * @param pCode O novo código de erro.
     */

    public void setCode(final String pCode) {
        this.code = pCode;
    }

    /**
     * Retorna a mensagem de erro.
     *
     * @return A mensagem de erro.
     */

    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem de erro.
     *
     * @param pMessage A nova mensagem de erro.
     */

    public void setMessage(final String pMessage) {
        this.message = pMessage;
    }
}
