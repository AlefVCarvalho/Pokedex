# Alteracoes

## Diagnostico

- O teste de contexto falhava porque a aplicacao dependia de um MySQL disponivel para que o Hibernate descobrisse o dialeto.
- `spring.jpa.hibernate.ddl-auto=none` nao criava nem validava tabelas, apesar de o CRUD depender delas.
- O projeto tinha apenas o modelo e o CRUD web de `Pokemon`; nao havia persistencia para treinadores, equipes, membros de equipes ou estatisticas.
- A Home usava o numero fixo de 9 regioes e nao havia testes de regras de negocio ou dos endpoints.

## Implementado

- Adicionado `src/main/resources/db/schema.sql` com tabelas, chaves estrangeiras, restricoes de unicidade e regras de quantidade.
- Configurada a inicializacao do schema SQL e a validacao do mapeamento JPA em `application.properties`.
- Criados os modelos `Treinador`, `Equipe`, `EquipePokemon` e `EstatisticaEquipe`.
- Criados repositorios e servicos com validacao de campos, conflitos de email, referencias inexistentes e estatisticas nao negativas.
- Criados endpoints REST:
  - `GET/POST/PUT/DELETE /api/treinadores`
  - `GET/POST/PUT/DELETE /api/equipes`
  - `GET/PUT /api/equipes/{id}/estatisticas`
- O payload de equipe aceita `nome`, `descricao`, `treinadorId` e `pokemonIds`, permitindo associar os Pokemon ja cadastrados.
- Adicionado H2 somente no escopo de testes, isolando o teste de contexto de um MySQL local.

## Melhorias recomendadas

- Criar telas Thymeleaf para consumir os endpoints de treinadores e equipes.
- Adicionar autenticacao/autorizacao antes de disponibilizar os endpoints publicamente.
- Trocar o schema manual por migrations versionadas com Flyway ou Liquibase quando houver deploy continuo.
- Adicionar testes unitarios dos servicos e testes MockMvc para os contratos REST.
- Substituir a estatistica fixa de regioes por uma tabela ou configuracao persistida.
- Mover credenciais padrao do banco para variaveis de ambiente ou um gerenciador de segredos.

## Interface de treinadores e equipes

- Criadas as páginas `/signin` e `/signup` com autenticação temporária por sessão HTTP.
- A Home agora possui ações `Sign in`, `Sign up` e acesso às equipes.
- Criado o módulo web de equipes com busca, criação, edição, exclusão, detalhes e seleção de Pokémon.
- Adicionados convites para treinadores e solicitações para entrar em equipes, com aceitar/recusar.
- Adicionada a busca de treinadores por nome ou email.
- Integrada a PokeAPI no formulário de Pokémon para listar os primeiros registros, preencher nome/tipos e usar imagens oficiais.
- Criado `src/main/resources/db/seed.sql` com Pokémon, treinadores, equipes, estatísticas, convite e solicitação de exemplo.
- Criado `src/main/resources/db/migration.sql` para atualizar bancos existentes com a coluna `treinadores.senha` e a tabela de participações.
- Corrigida a renderização das equipes com `@EntityGraph`, carregando as relações necessárias antes de fechar a sessão JPA.
- Ajustado o contraste do nome logado na Home e o estilo do botão de criação de conta.
- Refatorado o domínio de equipes: Pokémon agora usam relação direta `Equipe`-`Pokemon`, e treinadores membros usam relação direta `Equipe`-`Treinador`; `EquipeParticipacao` ficou reservada aos fluxos de convite e solicitação.
- Mesclados os endpoints de estatísticas em `EquipeController` e `EquipeService`; removidos o controller, service e repository exclusivos de estatísticas.
- Adicionadas consultas de equipes gerenciadas e equipes em que o treinador participa, além de convite com escolha da equipe.
- Criada seção de equipes participantes, busca de treinadores e cards arredondados com melhor espaçamento entre nome e email.
- A página de equipes agora oculta equipes em que o usuário já participa, marca solicitações pendentes como “Pedido feito”, separa buscas de equipes e treinadores, pagina até 10 equipes e 25 treinadores, e mostra as equipes do usuário como cards.

> A autenticação é propositalmente temporária para o protótipo: as senhas ficam no banco sem hash e a sessão é a única proteção. Antes de produção, usar Spring Security, hash de senha e controle de autorização completo.
