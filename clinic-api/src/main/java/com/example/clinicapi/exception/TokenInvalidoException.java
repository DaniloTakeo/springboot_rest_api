package com.example.clinicapi.exception;

/**
 * Exceção lançada quando há falha ao processar ou validar um token JWT.
 */
public class TokenInvalidoException extends RuntimeException {

    /**
     * O serialVersionUID é um identificador de
     * versão para uma classe Serializable.
     * Necessário para garantir a compatibilidade
     * durante a serialização/desserialização.
     */
    private static final long serialVersionUID = -1785916822636487410L;

    /**
     * Construtor que cria uma nova instância da exceção
     * {@code TokenInvalidoException} com uma mensagem de erro
     * descritiva e a causa original da exceção.
     *
     * @param mensagem A mensagem detalhada que descreve o motivo da exceção.
     * @param causa A causa raiz que provocou
     * esta exceção (pode ser {@code null}).
     */
    public TokenInvalidoException(final String mensagem,
            final Throwable causa) {
        super(mensagem, causa);
    }
}
