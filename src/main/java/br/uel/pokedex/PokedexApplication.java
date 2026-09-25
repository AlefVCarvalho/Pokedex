/*
 * Ponto de entrada da aplicação.
 * Este arquivo inicia o Spring Boot e deixa o restante da configuração por conta do framework.
 */
package br.uel.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokedexApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokedexApplication.class, args);
    }
}
