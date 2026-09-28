SET @senha_existe = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'treinadores'
      AND COLUMN_NAME = 'senha'
);

SET @comando_senha = IF(
    @senha_existe = 0,
    'ALTER TABLE treinadores ADD COLUMN senha VARCHAR(255) NOT NULL DEFAULT ''1234'' AFTER email',
    'SELECT 1'
);

PREPARE adicionar_senha FROM @comando_senha;
EXECUTE adicionar_senha;
DEALLOCATE PREPARE adicionar_senha;

CREATE TABLE IF NOT EXISTS equipe_treinadores (
    equipe_id BIGINT NOT NULL,
    treinador_id BIGINT NOT NULL,
    PRIMARY KEY (equipe_id, treinador_id),
    CONSTRAINT fk_equipe_treinadores_equipe FOREIGN KEY (equipe_id)
        REFERENCES equipes (id) ON DELETE CASCADE,
    CONSTRAINT fk_equipe_treinadores_treinador FOREIGN KEY (treinador_id)
        REFERENCES treinadores (id) ON DELETE CASCADE
);

    INSERT IGNORE INTO equipe_treinadores (equipe_id, treinador_id)
    SELECT id, treinador_id FROM equipes WHERE treinador_id IS NOT NULL;

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
