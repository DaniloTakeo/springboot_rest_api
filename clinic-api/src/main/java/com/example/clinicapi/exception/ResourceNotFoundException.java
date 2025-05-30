package com.example.clinicapi.exception;

/**
 * Exceção lançada quando um recurso específico (entidade)
 * não é encontrado no sistema.
 * Estende RuntimeException para ser uma exceção não verificada.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * O serialVersionUID é um identificador de
     * versão para uma classe Serializable.
     * Necessário para garantir a compatibilidade
     * durante a serialização/desserialização.
     */
    private static final long serialVersionUID = -3617255505093784735L;

    /**
     * Construtor para ResourceNotFoundException que permite
     * uma mensagem customizada.
     *
     * @param message A mensagem detalhada da exceção.
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
