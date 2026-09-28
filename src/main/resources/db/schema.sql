CREATE TABLE IF NOT EXISTS pokemons (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_pokedex INT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo_primario VARCHAR(60) NOT NULL,
    tipo_secundario VARCHAR(60),
    descricao VARCHAR(1000),
    PRIMARY KEY (id),
    CONSTRAINT uk_pokemon_numero UNIQUE (numero_pokedex)
);

CREATE TABLE IF NOT EXISTS treinadores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_treinador_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS equipes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(500),
    treinador_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_equipe_treinador FOREIGN KEY (treinador_id)
        REFERENCES treinadores (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS equipe_pokemon (
    id BIGINT NOT NULL AUTO_INCREMENT,
    equipe_id BIGINT NOT NULL,
    pokemon_id BIGINT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT uk_equipe_pokemon UNIQUE (equipe_id, pokemon_id),
    CONSTRAINT ck_equipe_pokemon_quantidade CHECK (quantidade > 0),
    CONSTRAINT fk_equipe_pokemon_equipe FOREIGN KEY (equipe_id)
        REFERENCES equipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_equipe_pokemon_pokemon FOREIGN KEY (pokemon_id)
        REFERENCES pokemons (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS equipe_treinadores (
    equipe_id BIGINT NOT NULL,
    treinador_id BIGINT NOT NULL,
    PRIMARY KEY (equipe_id, treinador_id),
    CONSTRAINT fk_equipe_treinadores_equipe FOREIGN KEY (equipe_id)
        REFERENCES equipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_equipe_treinadores_treinador FOREIGN KEY (treinador_id)
        REFERENCES treinadores (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estatisticas_equipe (
    id BIGINT NOT NULL AUTO_INCREMENT,
    equipe_id BIGINT NOT NULL,
    vitorias INT NOT NULL DEFAULT 0,
    derrotas INT NOT NULL DEFAULT 0,
    empates INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_estatistica_equipe UNIQUE (equipe_id),
    CONSTRAINT ck_estatistica_vitorias CHECK (vitorias >= 0),
    CONSTRAINT ck_estatistica_derrotas CHECK (derrotas >= 0),
    CONSTRAINT ck_estatistica_empates CHECK (empates >= 0),
    CONSTRAINT fk_estatistica_equipe FOREIGN KEY (equipe_id)
        REFERENCES equipes (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS equipe_participacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    equipe_id BIGINT NOT NULL,
    treinador_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    PRIMARY KEY (id),
    CONSTRAINT uk_equipe_participacao UNIQUE (equipe_id, treinador_id, tipo),
    CONSTRAINT ck_participacao_tipo CHECK (tipo IN ('CONVITE', 'SOLICITACAO')),
    CONSTRAINT ck_participacao_status CHECK (status IN ('PENDENTE', 'ACEITA', 'RECUSADA')),
    CONSTRAINT fk_participacao_equipe FOREIGN KEY (equipe_id)
        REFERENCES equipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_participacao_treinador FOREIGN KEY (treinador_id)
        REFERENCES treinadores (id) ON DELETE CASCADE
);
