-- Dados demonstrativos para MySQL. Senha temporaria de todas as contas: 1234

INSERT IGNORE INTO pokemons (numero_pokedex, nome, tipo_primario, tipo_secundario, descricao) VALUES
(1, 'Bulbasaur', 'grass', 'poison', 'Uma semente misteriosa cresce em suas costas.'),
(4, 'Charmander', 'fire', NULL, 'A chama em sua cauda indica sua energia vital.'),
(7, 'Squirtle', 'water', NULL, 'Usa sua carapaca para se proteger e nadar.'),
(25, 'Pikachu', 'electric', NULL, 'Armazena eletricidade em suas bochechas.'),
(39, 'Jigglypuff', 'fairy', 'normal', 'Sua voz suave coloca os adversarios para dormir.'),
(65, 'Alakazam', 'psychic', NULL, 'Seu poder mental cresce com cada batalha.');

INSERT IGNORE INTO treinadores (nome, email, senha) VALUES
('Ash Ketchum', 'ash@pokedex.local', '1234'),
('Misty Waterflower', 'misty@pokedex.local', '1234'),
('Brock Stone', 'brock@pokedex.local', '1234'),
('May Maple', 'may@pokedex.local', '1234');

INSERT IGNORE INTO equipes (nome, descricao, treinador_id)
SELECT 'Insignias de Kanto', 'Uma equipe equilibrada para explorar a primeira regiao.', id
FROM treinadores WHERE email = 'ash@pokedex.local';

INSERT IGNORE INTO equipes (nome, descricao, treinador_id)
SELECT 'Mare Alta', 'Especialistas em estrategias de agua.', id
FROM treinadores WHERE email = 'misty@pokedex.local';

INSERT IGNORE INTO equipe_pokemon (equipe_id, pokemon_id, quantidade)
SELECT e.id, p.id, 1 FROM equipes e CROSS JOIN pokemons p
WHERE e.nome = 'Insignias de Kanto' AND p.numero_pokedex IN (1, 4, 25);

INSERT IGNORE INTO equipe_pokemon (equipe_id, pokemon_id, quantidade)
SELECT e.id, p.id, 1 FROM equipes e CROSS JOIN pokemons p
WHERE e.nome = 'Mare Alta' AND p.numero_pokedex IN (7, 25, 39);

INSERT IGNORE INTO equipe_treinadores (equipe_id, treinador_id)
SELECT e.id, t.id FROM equipes e JOIN treinadores t ON e.treinador_id = t.id;

INSERT IGNORE INTO estatisticas_equipe (equipe_id, vitorias, derrotas, empates)
SELECT id, 12, 3, 1 FROM equipes WHERE nome = 'Insignias de Kanto';

INSERT IGNORE INTO estatisticas_equipe (equipe_id, vitorias, derrotas, empates)
SELECT id, 8, 4, 2 FROM equipes WHERE nome = 'Mare Alta';

INSERT IGNORE INTO equipe_participacoes (equipe_id, treinador_id, tipo, status)
SELECT e.id, t.id, 'CONVITE', 'PENDENTE'
FROM equipes e CROSS JOIN treinadores t
WHERE e.nome = 'Insignias de Kanto' AND t.email = 'may@pokedex.local';

INSERT IGNORE INTO equipe_participacoes (equipe_id, treinador_id, tipo, status)
SELECT e.id, t.id, 'SOLICITACAO', 'PENDENTE'
FROM equipes e CROSS JOIN treinadores t
WHERE e.nome = 'Mare Alta' AND t.email = 'brock@pokedex.local';
