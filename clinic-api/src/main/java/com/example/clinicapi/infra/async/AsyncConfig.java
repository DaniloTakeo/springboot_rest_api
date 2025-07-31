package com.example.clinicapi.infra.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Classe de configuração responsável por habilitar o suporte à
 * execução assíncrona
 * de métodos na aplicação.
 *
 * <p>Ao anotar métodos com {@code @Async}, o Spring executará esses métodos
 * em uma thread separada, permitindo maior escalabilidade
 * e desempenho para tarefas
 * não bloqueantes, como envio de e-mails, chamadas externas, etc.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>
 * {@code
 * @Async
 * public void enviarNotificacao() {
 *     // executado de forma assíncrona
 * }
 * }
 * </pre>
 *
 * <p>Importante: para que o comportamento assíncrono funcione corretamente,
 * a anotação {@code @EnableAsync} deve estar presente em uma
 * classe de configuração
 * e os métodos anotados devem estar em beans gerenciados pelo Spring.</p>
 *
 * @author Danilo
 */
@Configuration
@EnableAsync
public class AsyncConfig {

}
