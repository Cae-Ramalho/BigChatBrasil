package br.com.bcb.BigChatBrasil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilita execução de workers em background
 */
@SpringBootApplication
@EnableScheduling
public class BigChatBrasilApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigChatBrasilApplication.class, args);
    }
}
