# Fundamentos de Sistemas Operativos — Trabalhos Práticos

Conjunto de **2 trabalhos práticos** desenvolvidos na unidade curricular
**Fundamentos de Sistemas Operativos** (FSO) de LEIM, no **ISEL**, ano letivo 2023/24.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** João Ramos — 50730
- **Aluno:** Miguel Alcobia — 50746
- **Grupo:** 4
- **Docente responsável:** Jorge Pais

---

## Descrição

Trabalhos práticos em **Java** que abordam **programação concorrente e comunicação entre
processos e tarefas**, aplicados ao controlo de um robô **LEGO EV3** no jogo *"O Rei manda"*,
incluindo:

- Aplicações **multiprocesso** com comunicação por memória partilhada (`MappedByteBuffer`)
- Aplicações **multitarefa** (*threads*)
- Sincronização com **semáforos** e **monitores**
- **Buffer circular** para troca de mensagens
- Modelação com **diagramas de classes** e **diagramas de atividades** (MagicDraw)
- Implementação de comportamentos como **autómatos** (`switch`/`case`)
- Interfaces gráficas com **Swing** (WindowBuilder no Eclipse)
- Leitura e escrita de ficheiros (`ObjectInputStream` / `ObjectOutputStream`)

---

## Trabalho Prático 1 — Aplicação multiprocesso "O Rei manda"

Aplicação composta por **dois processos Java**:

- **Rei:** envia ordens (comandos aleatórios ou controlados pelo utilizador)
- **Súbdito:** recebe as ordens e executa-as no robô, através da API do robô EV3

A comunicação entre os dois processos é feita por **memória partilhada**
(`MappedByteBuffer` sobre um ficheiro).

## Trabalho Prático 2 — Aplicação multitarefa REI_SUBDITO

Um único processo **REI_SUBDITO** que lança e controla **três tarefas**:

- **REI:** gera e envia os comandos
- **SUBDITO:** executa os comandos no robô
- **GRAVAR:** grava movimentos do robô num ficheiro e reproduz os movimentos gravados
  (durante a reprodução, os outros comportamentos não podem enviar comandos)

---

## Tecnologias utilizadas

- Java (Swing, `java.util.concurrent`, `java.nio`)
- Eclipse com WindowBuilder
- MagicDraw (diagramas UML)
- Robô LEGO EV3 (via API fornecida pelo docente)
