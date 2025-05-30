package com.example.clinicapi.exception;

/**
 * Exceção lançada quando um médico específico não é encontrado no sistema.
 * Estende RuntimeException para ser uma exceção não verificada.
 */
public class MedicoNaoEncontradoException extends RuntimeException {
    /**
     * O serialVersionUID é um identificador de
     * versão para uma classe Serializable.
     * Necessário para garantir a compatibilidade
     * durante a serialização/desserialização.
     */
    private static final long serialVersionUID = -5184102683914735116L;

    /**
     * Construtor padrão para MedicoNaoEncontradoException.
     * Utiliza uma mensagem padrão indicando que o médico não foi encontrado.
     */
    public MedicoNaoEncontradoException() {
        super("Médico não encontrado");
    }

    /**
     * Construtor para MedicoNaoEncontradoException
     * que permite uma mensagem customizada.
     *
     * @param message A mensagem detalhada da exceção.
     */
    public MedicoNaoEncontradoException(final String message) {
        super(message);
    }
}
