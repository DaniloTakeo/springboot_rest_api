package com.example.clinicapi.dto;

/**
 * Representa a resposta de autenticação contendo tokens JWT.
 *
 * @param accessToken  token JWT de acesso
 * @param refreshToken token de atualização
 */
public record TokenResponse(String accessToken, String refreshToken) { }
