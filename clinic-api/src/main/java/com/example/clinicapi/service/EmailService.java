package com.example.clinicapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por enviar e-mails de forma assíncrona.
 *
 * Esta classe utiliza o {@link JavaMailSender} do Spring Framework
 * para realizar o envio de mensagens de texto simples por e-mail.
 * Pode ser utilizada para notificações de eventos como agendamento
 * de consultas e atualizações de status.
 *
 * <p>Requisitos:
 * <ul>
 *   <li>Configuração do servidor SMTP no application.properties</li>
 *   <li>Dependência spring-boot-starter-mail no classpath</li>
 * </ul>
 *
 * <p>Exemplo de uso:
 * <pre>{@code
 * emailService.enviarEmail(
 *     "paciente@email.com",
 *     "Consulta agendada",
 *     "Sua consulta foi agendada com sucesso para 20/08 às 14h"
 * );
 * }</pre>
 *
 * @author Danilo
 */
@Service
public class EmailService {

    /**
     * Componente do Spring responsável por realizar o envio de e-mails.
     *
     * <p>É injetado automaticamente via construtor e utilizado internamente
     * pelo serviço {@code EmailService} para compor e enviar mensagens
     * de e-mail
     * usando o protocolo SMTP configurado.</p>
     */
    private final JavaMailSender mailSender;

    /**
     * Construtor que injeta o {@link JavaMailSender} configurado pelo Spring.
     *
     * @param pMailSender o componente responsável pelo envio dos e-mails
     */
    @Autowired
    public EmailService(final JavaMailSender pMailSender) {
        this.mailSender = pMailSender;
    }

    /**
     * Envia um e-mail de texto simples para o destinatário especificado.
     *
     * <p>Esse método é executado de forma assíncrona para não
     * bloquear a thread principal.</p>
     *
     * @param to destinatário do e-mail (ex: paciente)
     * @param subject assunto do e-mail
     * @param body corpo (mensagem) do e-mail
     */
    @Async
    public void enviarEmail(final String to, final String subject,
            final String body) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(to);
        mensagem.setSubject(subject);
        mensagem.setText(body);
        mailSender.send(mensagem);
    }
}
