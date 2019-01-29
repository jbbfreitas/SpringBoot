package br.com.abim.springboot.primeiro.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Olá eu sou o seu primeiro Spring Boot com Rest!";
    }

}