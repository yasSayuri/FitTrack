# 🏋️‍♂️ FitTrack

O **FitTrack** é um aplicativo móvel Android nativo em Java, desenvolvido para auxiliar os usuários na organização, acompanhamento e controle de suas atividades físicas e treinos (academia, crossfit, corrida, etc). O sistema busca oferecer uma plataforma simples e intuitiva onde o usuário pode registrar treinos e acompanhar sua evolução, contribuindo para a disciplina e motivação.

## 🎨 Design e UI/UX

O aplicativo foi construído com foco em uma experiência premium e minimalista, adotando o **Dark Mode** como padrão. A paleta de cores é composta por tons de preto, cinza escuro (chumbo) e destaques dinâmicos em roxo (`#7C4DFF`).

* 
**Dashboard Moderno:** Os painéis e gráficos utilizam `MaterialCardView` com efeitos nativos de sombra e brilho/neon roxo (`outlineAmbientShadowColor`) para destacar as informações sem poluir a tela.


* 
**Navegação Fluida:** Utilização de `Fragments` associados a uma `BottomNavigationView` com design "flat" (plano), permitindo a troca entre abas sem recarregar ou piscar a tela principal.



## 🛠️ Tecnologias Utilizadas

* 
**Plataforma:** Android Nativo 


* 
**Linguagem:** Java 


* 
**Interface:** XML (com Material Components) 


* 
**Banco de Dados Local:** SQLite (via Room Persistence Library) 



## 💾 Banco de Dados

Foi utilizado o banco de dados **SQLite** através da biblioteca **Room** porque ele é o padrão oficial para armazenamento local no Android, sendo uma opção mais leve que economiza a bateria do celular por não exigir conexões constantes de rede.
O banco de dados encontra-se estruturado de forma relacional com as seguintes entidades principais:

* 
**User:** Armazena credenciais, @usuario, foto e dados biométricos.


* 
**TreinoPlano:** Gerencia as rotinas e planos fixos criados pelo usuário.


* 
**Treino:** Registra o histórico dinâmico de atividades concluídas.



## ✅ O Que Já Foi Implementado

* 
**Autenticação de Usuário (Login/Cadastro):** Telas funcionais com validação de dados, integradas com máscaras automáticas (Data de Nascimento, CPF) que contam com travas de segurança rigorosas no `TextWatcher` para prevenção de vazamento de memória e loop infinito.


* 
**Onboarding Personalizado:** Tela exclusiva para o primeiro acesso de novos cadastros, responsável por captar as configurações biométricas (idade, peso, altura) e as metas (objetivo, frequência), gerenciadas por `SharedPreferences` independentes para cada usuário logado.


* 
**Home & Dashboard Analítico:** Tela principal recebendo o usuário pelo nome cadastrado (em destaque colorido) e painéis em grid (Layout Weights) para visualizar horas treinadas, divisão por tipo de exercícios e métricas de consistência.


* 
**Gestão de Planos ("Meus Treinos"):** Tela que permite criar e salvar rotinas personalizadas, contendo informações de exercícios, séries e repetições, com injeção automática de cartões na interface de leitura.


* 
**Histórico de Atividades:** Sistema em que o "Novo Treino" registrado é salvo no banco de dados, sendo renderizado de imediato usando componentes clonados (inflação dinâmica de XML) na tela de histórico do usuário.


* **Edição de Perfil Interativa:** Painel do usuário puxando os dados do banco para criar uma bio estilizada e um arroba único (`@username`). Conta também com a invocação de um pop-up (Custom Dialog) para atualizar informações biométricas simultaneamente e com sincronia ao banco.
