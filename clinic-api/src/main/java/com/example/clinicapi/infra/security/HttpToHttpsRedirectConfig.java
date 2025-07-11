package com.example.clinicapi.infra.security;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração responsável por redirecionar automaticamente
 * requisições HTTP (porta 8080) para HTTPS (porta 8443).
 * <p>
 * Essa classe adiciona um conector Tomcat extra que escuta requisições
 * HTTP na porta 8080 e redireciona todas elas para a porta segura (HTTPS).
 *
 * <p>
 * Útil em ambientes locais ou de produção onde você quer garantir
 * que todas as comunicações com a API ocorram de forma segura
 * via protocolo TLS/SSL.
 */
@Configuration
public class HttpToHttpsRedirectConfig {

    /**
     * Customiza a fábrica de servidor embutido do Spring Boot,
     * adicionando um conector HTTP extra que redireciona para HTTPS.
     *
     * @return um customizador da fábrica Tomcat para incluir o redirecionamento.
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return server -> server.addAdditionalTomcatConnectors(httpConnector());
    }

    /**
     * Cria um conector HTTP (porta 8080) com redirecionamento automático para HTTPS (porta 8443).
     *
     * @return o conector HTTP configurado para redirecionar.
     */
    private Connector httpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
}
