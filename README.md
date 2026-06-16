# FitTrack

Aplicativo Android nativo desenvolvido em Java para organização, registro e acompanhamento de treinos físicos. O FitTrack permite que o usuário crie rotinas personalizadas, registre treinos realizados, acompanhe estatísticas de desempenho e mantenha um perfil com informações básicas de evolução.

O projeto foi desenvolvido com foco em uma experiência simples, visual moderna e funcionamento local, utilizando banco de dados interno com Room.

---

## Visão geral

O FitTrack foi criado para ajudar usuários a manterem controle sobre sua rotina de treinos. A aplicação reúne funcionalidades de autenticação, onboarding personalizado, criação de planos de treino, histórico de atividades e painel de estatísticas.

Entre os principais objetivos do aplicativo estão:

- facilitar o registro de treinos;
- permitir a criação de rotinas com exercícios, séries e repetições;
- acompanhar quantidade de treinos, duração total e dias treinados;
- armazenar dados do usuário localmente;
- oferecer uma interface escura, moderna e intuitiva.

---

## Tecnologias utilizadas

- Android Nativo
- Java
- XML
- Room Persistence Library
- SQLite
- AppCompat
- Material Components
- Gradle Kotlin DSL

---

## Funcionalidades

### Autenticação

O aplicativo possui fluxo de cadastro e login com validações básicas.

No cadastro, o usuário informa:

- nome completo;
- nome de usuário;
- e-mail;
- CPF;
- data de nascimento;
- senha.

O sistema realiza validação de e-mail, CPF e tamanho mínimo de senha. A idade do usuário é calculada automaticamente a partir da data de nascimento.

Após o cadastro, o usuário é direcionado ao onboarding.

---

### Onboarding personalizado

Após criar uma conta, o usuário passa por um fluxo inicial de configuração do perfil.

O onboarding coleta informações como:

- gênero;
- peso;
- altura;
- objetivo;
- nível de atividade.

Essas informações são usadas para compor os dados do perfil e personalizar a experiência inicial do usuário.

---

### Home e dashboard

A tela inicial apresenta uma saudação personalizada com o nome do usuário e um painel de estatísticas.

O dashboard exibe informações como:

- total de treinos cadastrados;
- tempo total treinado;
- quantidade de dias treinados;
- distribuição visual dos dias da semana com treino.

---

### Histórico de treinos

O usuário pode registrar treinos realizados informando:

- tipo de treino;
- duração;
- data;
- calorias;
- descrição.

Os treinos ficam salvos no banco local e são exibidos no histórico. Cada card do histórico pode ser aberto para visualizar mais detalhes do treino registrado.

---

### Planos de treino

A aplicação permite criar planos ou rotinas de treino personalizadas.

Cada plano possui:

- nome do treino;
- lista de exercícios;
- séries;
- repetições.

Os exercícios são salvos de forma relacionada ao plano de treino, permitindo visualizar cada exercício com suas respectivas séries e repetições.

---

### Detalhes e edição de planos

Ao abrir um plano de treino, o usuário consegue visualizar os exercícios de forma organizada.

Exemplo:

```text
Agachamento
Séries: 4          Repetições: 12
