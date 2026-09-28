package br.uel.pokedex.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "treinadores")
@Getter
@Setter
@NoArgsConstructor
public class Treinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String senha;

    @Transient
    @JsonIgnore
    private String senhaConfirmacao;

    @JsonIgnore
    @OneToMany(mappedBy = "treinador")
    private List<Equipe> equipes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "membros")
    private Set<Equipe> equipesParticipante = new HashSet<>();
}
