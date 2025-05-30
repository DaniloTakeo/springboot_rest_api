package com.example.clinicapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public final class ClinicApiApplication {

    private ClinicApiApplication() {

    }

    /**
     * Ponto de entrada principal da aplicação Spring Boot.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(final String[] args) {
        SpringApplication.run(ClinicApiApplication.class, args);
    }
}
