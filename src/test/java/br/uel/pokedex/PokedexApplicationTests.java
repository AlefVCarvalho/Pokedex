/*
 * Teste básico de inicialização da aplicação.
 * Verifica se o contexto do Spring Boot consegue ser carregado sem erros.
 */
package br.uel.pokedex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:pokedex;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.open-in-view=false",
    "spring.sql.init.mode=never"
})
class PokedexApplicationTests {

    @Test
    void contextLoads() {
    }

}
