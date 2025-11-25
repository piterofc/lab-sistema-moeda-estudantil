package com.universidade.moedaestudantil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoedaEstudantilApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoedaEstudantilApplication.class, args);
    }
}