package br.uel.pokedex.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipes")
@Getter
@Setter
@NoArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "treinador_id")
    private Treinador treinador;

        @ManyToMany
        @jakarta.persistence.JoinTable(name = "equipe_pokemon",
            joinColumns = @JoinColumn(name = "equipe_id"),
            inverseJoinColumns = @JoinColumn(name = "pokemon_id"))
        private Set<Pokemon> pokemons = new HashSet<>();

        @ManyToMany
        @jakarta.persistence.JoinTable(name = "equipe_treinadores",
            joinColumns = @JoinColumn(name = "equipe_id"),
            inverseJoinColumns = @JoinColumn(name = "treinador_id"))
        @JsonIgnore
        private Set<Treinador> membros = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private EstatisticaEquipe estatisticas;
}
