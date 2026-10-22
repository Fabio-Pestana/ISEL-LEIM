# Infraestruturas Computacionais Distribuídas — Trabalhos Práticos

Conjunto de **2 trabalhos práticos** desenvolvidos na unidade curricular **Infraestruturas Computacionais Distribuídas** (IECD) de LEIM, no **ISEL**, ano letivo 2023/24.

Os dois trabalhos formam um único projeto: uma infraestrutura distribuída para **jogar Othello em rede** entre jogadores registados. A 1ª parte funciona em consola (*sockets*) e a 2ª parte acrescenta uma aplicação **Web** (JSP e *Servlets*).

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** João Ramos — 50730
- **Aluno:** Miguel Alcobia — 50746
- **Turma:** 42D
- **Docente responsável:** Porfírio Filipe

---

## Descrição

Um jogador faz o **auto-registo** (nickname, senha, nacionalidade, data de nascimento e foto) e joga Othello contra outro jogador ligado ao sistema. Para cada jogador são mantidas as **vitórias, derrotas, número de jogos e tempo gasto**.

Conceitos abordados:

- Comunicação entre processos com **sockets TCP e UDP**
- Servidor **TCP concorrente** (uma *thread* por jogo) e fila de espera **FIFO** de jogadores
- Arquiteturas **peer-to-peer** e ***thin client***
- Protocolo ao nível da aplicação baseado em **XML**, com validação por **XSD**
- Persistência de dados em **XML** (utilizadores, países e bandeiras)
- Aplicações Web com **JavaServer Pages** e ***Servlets***

---

## Estrutura

Os dois trabalhos são **projetos Eclipse**. Nesta pasta estão o código-fonte (`src`) e o relatório de cada trabalho.

```
IECD/
├── README.md
├── Enunciado/
│   ├── IECD-TP01-2324SV.pdf
│   └── IECD-TP02-2324SV.pdf
├── TP1_A50730_A50746_A50756/
│   ├── TP1_A50730_A50746_A50756_Relatorio.pdf
│   └── jogoOthelloPeer/
│       └── src/
│           ├── java/          # Código Java (client, peer, server, user, util)
│           └── webapp/        # XML, XSD, fotos e ficheiros de registo
└── TP2_A50730_A50746_A50756/
    ├── TP2_A50730_A50746_A50756_Relatorio.pdf
    └── TP2_A50730_A50746_A50756/
        └── src/
            ├── java/          # Servlets e código Java (client, db, server, user, util, validate)
            └── webapp/        # Páginas JSP/HTML, CSS, JavaScript, XML, XSD e imagens
```

- **`src/java`:** classes Java do projeto
- **`src/webapp`:** conteúdo Web e dados do projeto (JSP, HTML, CSS, JavaScript, imagens e ficheiros XML/XSD)

---

## TP1 — Othello em rede (consola)

**Arquitetura:** pretendia-se uma arquitetura *thin client* mas, devido a dificuldades de implementação, o grupo optou por uma arquitetura **peer-to-peer**, com os respetivos prós e contras analisados no relatório.

**Protocolos de transporte**

| Protocolo | Porto | Utilização |
|-----------|-------|------------|
| TCP | 5025 | Servidor concorrente: gestão dos jogos e comunicação com os jogadores |
| UDP | 5026 | Descoberta de jogadores (*broadcast*) |

**Componentes**

- **Servidor** (`server`): fila FIFO de jogadores, emparelhamento e gestão de cada jogo numa *thread* (`Servidor`, `ServidorJogo`, `Othello`, `BoardGame`)
- **Cliente** (`client`): `Jogador` e `CliStub`, que enviam as mensagens de autenticação, de perfil e as jogadas
- **Peer** (`peer`): `P2P` e `Peer`, com a descoberta de jogadores por UDP
- **Base de dados em XML** (`user`, `util`): `User`, `XMLDoc`, `OthelloXML` e `MyImage`

**Dados e mensagens**

- `users.xml` com nickname, senha (**SHA-256**), nacionalidade, foto (**Base64**), vitórias, derrotas, jogos e tempos
- `paises.xml` com as nacionalidades, por género
- Todos validados por XSD: `users.xsd`, `paises.xsd`, `othello-metodos-cli.xsd`, `othello-metodos-srv.xsd` e `discover.xsd`
- Mensagens em XML, por exemplo `<metodo><iniciar nickname="..."/></metodo>`. Os métodos incluem `login`, `user`, `perfil`, `changePass`, `changePic`, `updateUser`, `iniciar`, `obter` e `jogar`

## TP2 — Othello na Web (JSP e Servlets)

Extensão do TP1 para funcionar sobre a Web, em simultâneo com a versão de consola. Passou a usar a arquitetura ***thin client***, em que o *browser* trata apenas da apresentação e a lógica fica no servidor.

**Aplicação Web**

- **Servlets:** `LoginServlet`, `RegisterServlet`, `MenuServlet`, `OthelloServlet`, `PassServlet`, `PictureServlet`, `ColorServlet`, `FinishGameServlet`, `RestartGameServlet`, `LogoutServlet` e `GetHTML`
- **Páginas JSP:** menu, jogo, perfil, pesquisa de jogadores, ranking, alteração de cor, de senha e de foto
- **Estilos e *scripts*:** CSS por página e JavaScript para a cor de fundo

**Requisitos acrescentados**

- **Perfil** editável, com a **cor de fundo** preferida
- **Pesquisa de jogadores com *autocomplete*** (`<datalist>` e `GetHTML`, com JavaScript a cada tecla)
- **Limite de 30 s por jogada** (*timeout* nos *sockets* do servidor)
- **Quadro de honra (ranking)** ordenado por vitórias e, em caso de empate, pelo tempo médio de jogo, com bandeiras das nacionalidades (`bandeiras.xml`, em Base64)
- **Cópias de segurança do ranking** (`rankingTable_<data-hora>.xml`), guardadas só quando o ranking muda
- **Desforra** (novo jogo contra o mesmo adversário), *logout* e ranking dos 5 melhores também na consola
- **Compatibilidade com browsers** (Chrome, Safari, Firefox e Edge), analisada no relatório

---

## Como executar

Os projetos foram realizados no **Eclipse**. Para que duas ou mais máquinas comuniquem entre si foi necessário ter um **servidor Apache Tomcat** a correr: **uma máquina corre o servidor** e as **restantes são clientes**.

**Requisitos:** Java, Eclipse (com suporte para projetos Web) e **Apache Tomcat 10 ou superior** (as *servlets* usam `jakarta.servlet`).

1. **Importar** o projeto (`jogoOthelloPeer` ou `TP2_A50730_A50746_A50756`) no Eclipse.
2. **Configurar o servidor Tomcat** no Eclipse e executar o projeto nesse servidor, na máquina que faz de servidor.
3. **Consola (TP1 e TP2):** na máquina servidora, executar `server.Servidor`. Em cada cliente, indicar na classe `Jogador` o **endereço IP da máquina servidora** e executar `client.Jogador`. O porto TCP por omissão é o **5025**.
4. **Web (TP2):** nos clientes, abrir no *browser* o endereço da aplicação no servidor, `http://<IP-do-servidor>:<porto-do-Tomcat>/<nome-da-aplicação>` (o porto por omissão do Tomcat é o 8080).

---

## Tecnologias utilizadas

- Java
- Eclipse
- Apache Tomcat
- Sockets TCP e UDP
- JavaServer Pages e Servlets
- XML e XSD
- HTML, CSS e JavaScript
