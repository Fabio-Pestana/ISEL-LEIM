# Sistemas Multimédia para a Internet - Projeto

Trabalho prático da unidade curricular de **Sistemas Multimédia para a Internet** (SMI) de LEIM, no ISEL, ano letivo 2024/2025.

- **Fábio Pestana** — 50756
- **João Ramos** — 50730
- **Turma:** 61D
- **Docente responsável:** João Ventura

---

## Descrição

O trabalho consiste no estudo e desenvolvimento de um **Content Management System (CMS)** em **PHP**, para consolidar os conceitos da linguagem. O grupo criou o **NexusWiki**, um sistema de gestão e criação de conteúdos para wikis inspirado no *Fandom* (que usa o MediaWiki), centrado na partilha de informação e de conteúdos multimédia.

O trabalho foi entregue em três fases:

| Fase | Conteúdo |
|---|---|
| Fase 1 | Análise do problema: tipo de CMS, módulos, funcionalidades e estruturas de dados |
| Fase 2 | Esboço do sistema: modelo entidade-associação, *wireframes*, *mockups* e gestão de utilizadores |
| Fase 3 | Implementação do sistema (NexusWiki) |

---

## Funcionalidades

O sistema tem quatro perfis de utilizador:

| Perfil | Permissões |
|---|---|
| **Convidado** | Ver wikis e páginas públicas, pesquisar wikis por nome e filtrar por categoria (jogos, séries, filmes, clubes, desportos, etc.) |
| **Utilizador** | Tudo o do convidado, subscrever wikis (sino) para acompanhar atualizações, gerir a conta e alterar a password |
| **Simpatizante** | Tudo o do utilizador, criar e editar páginas e conteúdos nas wikis existentes, associar imagens e alterar a visibilidade das páginas (públicas ou privadas) |
| **Administrador** | Tudo o dos perfis anteriores, criar e eliminar wikis (com vídeo do YouTube associado), gestão de utilizadores, histórico de edições, tabela de criadores de conteúdos e definições do sistema |

**Outras funcionalidades**
- **Registo e autenticação:** formulário com nome, email, password (com confirmação) e **CAPTCHA** e as passwords são guardadas com *hash*.
- **Página principal:** grelha responsiva de wikis, com filtro por categoria e pesquisa por nome.
- **Página de uma wiki:** nome, descrição, imagem, vídeo do YouTube (via `iframe` de *embed*), lista de páginas e botão de subscrição.
- **Menu do administrador:**
  - **Gestão de utilizadores:** alterar o nome, subir ou descer o perfil, eliminar utilizadores (sem auto-exclusão) e filtrar por tipo.
  - **Histórico de edições:** quem editou ou apagou conteúdos, com comparação entre o conteúdo antigo e o novo.
  - **Tabela de criadores:** autores das páginas e dos conteúdos.
- **Serviço externo:** integração com o YouTube (o link é validado e guarda-se apenas o identificador do vídeo).

---

## Modelo de dados

Base de dados MySQL **NexusWiki**:

- `User` (Email, Password, Name, Tipo: Utilizador, Simpatizante ou Administrador)
- `Category` (Name)
- `Wiki` (Name, Category, Image em base64, Description, YoutubeId)
- `Page` (Name, Description, isVisible, CreationDateTime, Creator, Wiki, Img)
- `Information` (Name, Description, CreationDateTime, Creator, Page, Wiki, Img): os tópicos de cada página
- `Edition` (Editor, EditionDateTime, TypeOfChange, OldContent, NewContent e campos do conteúdo apagado)
- Tabelas de relação: `Edition_Information`, `Edition_Page` e `User_Wiki` (subscrições)

As imagens são guardadas em **base64** (`LONGTEXT`) na base de dados. Os convidados não são guardados.

---

## Estrutura do repositório

```
SMI/
├── README.md
├── Fase 1/
│   └── A50730_A50756.pdf                 # Relatório da Fase 1
├── Fase 2/
│   └── A50730_A50756.pdf                 # Relatório da Fase 2 (inclui MEA, wireframes e mockups)
└── Fase 3/
    ├── Relatorio_SMI_TP_G23_T61D.pdf     # Relatório final
    ├── readmetxt.txt                     # Instruções de execução e contas de teste
    ├── NexusWiki.sql                     # Base de dados com dados de teste (≈ 11 MB)
    └── Codigos_SMI_TP_G23_T61D/          # Código PHP do NexusWiki
        ├── login.php, regist.php, captcha.php, logout.php
        ├── mainPage.php, mainWikiPage.php, wikiPage.php, profile.php
        ├── createWiki.php, createWikiInformation.php, createWikiPage.php
        ├── editPage.php, editInformation.php, edition.php, editHistory.php
        ├── deleteWiki.php, deletePage.php, deleteInformation.php, deleteUser.php, contentDeleted.php
        ├── userManagement.php, changeUserSettings.php, adminSettings.php, tableOfCreators.php
        ├── db.php                        # Ligação à base de dados
        ├── Css/                          # Folhas de estilo
        ├── Images/                       # Logótipo
        └── assets/fonts/                 # Tipo de letra usado no CAPTCHA
```

---

## Como executar

O projeto corre num ambiente **PHP + MySQL** através do **Docker** (contentor com servidor web e phpMyAdmin).

**1. Carregar a base de dados**
1. Iniciar o contentor de SMI no Docker.
2. Abrir o browser em `localhost` e entrar no **phpMyAdmin**.
3. Criar uma base de dados chamada **NexusWiki**.
4. Selecionar a `NexusWiki`, clicar em **Importar**, escolher `Fase 3/NexusWiki.sql` e executar.

**2. Executar o NexusWiki**
1. Garantir que a pasta `Codigos_SMI_TP_G23_T61D` está acessível no servidor web do contentor.
2. Abrir `localhost`, entrar na pasta do projeto e abrir `login.php`.
3. Ligação à base de dados: configurada em `db.php` (`localhost`, porta 3306, utilizador `root` e base de dados `NexusWiki`). Ajustar se o ambiente for diferente.

**Contas de teste:** existem contas dos perfis Administrador, Simpatizante e Utilizador na base de dados de teste (as credenciais estão em `Fase 3/readmetxt.txt`). Para entrar como **Convidado**, basta escolher essa opção no `login.php`.

---

## Limitações

- **Email:** não foi concluída a ligação a um serviço externo de correio eletrónico, necessária para enviar notificações aos utilizadores subscritos e para validar o registo por email. A subscrição de wikis existe, mas não envia emails.
- **Eliminação de utilizadores:** ao apagar um utilizador, são também apagados os registos associados (edições, informações e páginas). O grupo propõe, como alternativa, marcar o criador desses conteúdos como anónimo.

---

## Tecnologias utilizadas

- **PHP** (`mysqli`, sessões)
- **MySQL** e **phpMyAdmin**
- **HTML** e **CSS** (páginas responsivas)
- **Docker**
- **API de *embed* do YouTube** (serviço externo)
