package com.example.clinicapi.exception;

public class MedicoNaoEncontradoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5184102683914735116L;

	public MedicoNaoEncontradoException() {
        super("Médico não encontrado");
    }

    public MedicoNaoEncontradoException(String message) {
        super(message);
    }
}