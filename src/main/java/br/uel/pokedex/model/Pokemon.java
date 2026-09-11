package br.uel.pokedex.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pokemons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número oficial do Pokémon na Pokédex.
    @Column(name = "numero_pokedex", nullable = false, unique = true)
    private Integer numeroPokedex;

    @Column(nullable = false)
    private String nome;

    @Column(name = "tipo_primario", nullable = false)
    private String tipoPrimario;

    @Column(name = "tipo_secundario")
    private String tipoSecundario;

    private Integer nivel;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;
}
