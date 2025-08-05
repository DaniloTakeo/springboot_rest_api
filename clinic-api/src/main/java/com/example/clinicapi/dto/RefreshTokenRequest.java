package com.example.clinicapi.dto;

/**
 * Representa o corpo da requisição de refresh de token.
 *
 * @param refreshToken o token que será usado para renovar o access token
 */
public record RefreshTokenRequest(String refreshToken) { }
