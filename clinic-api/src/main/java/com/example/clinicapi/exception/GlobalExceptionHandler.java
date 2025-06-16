package com.example.clinicapi.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Esta classe é um manipulador global de exceções para a API.
 * Ela centraliza o tratamento de erros para
 * fornecer respostas padronizadas ao cliente.
 */
@ControllerAdvice
public final class GlobalExceptionHandler {

    /**
     * Logger estático utilizado para registrar mensagens de log relacionadas à
     * execução da {@link GlobalExceptionHandler}, como requisições recebidas,
     * operações bem-sucedidas, falhas e outras informações relevantes
     * durante o ciclo de vida da requisição.
     * <p>Utiliza a implementação do SLF4J fornecida pelo Logback.</p>
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Manipula exceções gerais e inesperadas,
     * retornando uma resposta de erro interno do servidor.
     *
     * @param ex A exceção capturada.
     * @return Uma ResponseEntity contendo um ErrorResponse
     * com status 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            final Exception ex) {
        LOGGER.error("Erro interno inesperado: {}", ex.getMessage(), ex);
        final ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR", ex.getMessage());
        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Manipula exceções quando um recurso solicitado não é encontrado.
     *
     * @param ex A exceção ResourceNotFoundException capturada.
     * @return Uma ResponseEntity contendo um
     * ErrorResponse com status 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            final ResourceNotFoundException ex) {
        LOGGER.warn("Recurso não encontrado: {}", ex.getMessage());
        final ErrorResponse error = new ErrorResponse("RESOURCE_NOT_FOUND",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Manipula exceções para argumentos ilegais ou inválidos.
     *
     * @param ex A exceção IllegalArgumentException capturada.
     * @return Uma ResponseEntity contendo um
     * ErrorResponse com status 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            final IllegalArgumentException ex) {
        LOGGER.warn("Argumento inválido: {}", ex.getMessage());
        final ErrorResponse error = new ErrorResponse("BAD_REQUEST",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Manipula erros de validação de dados provenientes
     * de anotações @Valid em corpos de requisição.
     *
     * @param ex A exceção MethodArgumentNotValidException capturada.
     * @return Uma ResponseEntity contendo um mapa de
     * erros de campo com status 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(),
                        error.getDefaultMessage()));
        LOGGER.warn("Erro de validação: {} campo(s) inválido(s): {}",
                errors.size(), errors.keySet());

        return ResponseEntity.badRequest().body(errors);
    }
}
