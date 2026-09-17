package br.uel.pokedex.model;

import java.util.Map;

public record CatalogPokemon(
        Integer numero,
        String nome,
        String tipoPrimario,
        String tipoSecundario,
        String imagemUrl,
        Map<String, Integer> atributos) {

    public String tiposLabel() {
        return tipoSecundario == null ? tipoPrimario : tipoPrimario + "," + tipoSecundario;
    }
}