package br.com.abim.springboot.primeiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrimoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimoApplication.class, args);
        System.out.println("======================================");
        System.out.println("A aplicação está rodando na porta 8090");
        System.out.println("======================================");
    }
}