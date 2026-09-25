/*
 * Representa um Pokémon salvo no banco.
 * A imagem não é armazenada: sua URL é montada automaticamente usando o número da Pokédex.
 */
package br.uel.pokedex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pokemons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    private static final String ARTWORK_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Este é o ID informado no cadastro e corresponde ao número oficial do Pokémon.
    @Column(name = "numero_pokedex", nullable = false, unique = true)
    private Integer numeroPokedex;

    @Column(nullable = false)
    private String nome;

    @Column(name = "tipo_primario", nullable = false)
    private String tipoPrimario;

    @Column(name = "tipo_secundario")
    private String tipoSecundario;

    @Column(length = 1000)
    private String descricao;

    @Transient
    public String getImagemUrl() {
        if (numeroPokedex == null) {
            return "";
        }

        return ARTWORK_URL + numeroPokedex + ".png";
    }
}
