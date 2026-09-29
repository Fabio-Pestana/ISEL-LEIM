# Modelação e Simulação de Sistemas Naturais — Trabalhos Práticos

Conjunto de **4 trabalhos práticos e 1 projeto final** desenvolvidos na unidade curricular
**Modelação e Simulação de Sistemas Naturais** (MSSN) de LEIM, no **ISEL**,
ano letivo 2023/24.

- **Aluno:** Fábio Pestana — 50756
- **Aluno:** Miguel Alcobia — 50746
- **Grupo:** 30
- **Docente responsável:** Paulo Vieira

---

## Descrição

Trabalhos práticos desenvolvidos em **Java com Processing 3** (e ferramentas de simulação como
**Silico** e **Loopy**) que abordam a **modelação e simulação de sistemas naturais**, incluindo:

- Diagramas de ciclos causais e modelação com *Stocks & Flows*
- Autómatos celulares (Jogo da Vida) e agregação limitada por difusão (DLA)
- Queda livre com atrito, sistemas solares e sistemas de partículas
- Agentes autónomos: comportamentos individuais e de grupo (*Boids* e *Flocking*)
- Teoria do caos, função logística e Jogo do Caos
- Fractais: gramáticas de Lindenmayer (L-Systems) e conjuntos de Mandelbrot e Julia
- Geração procedimental de terrenos
- Simulação de ecossistemas e seleção natural

---

## TP0 — Ciclos causais e introdução ao Processing

- Interpretação de dois diagramas de ciclos causais (*reinforcing* e *balancing loops*)
- Diagrama de ciclos causais próprio, feito no **Loopy**, sobre ir às compras
- Logótipo da Pepsi desenhado em Processing (classe `PepsiLogo`), com movimento periódico,
  com *easing* e interativo com o rato
- Face *cartoon* (estrela) que muda de expressão e de cor de fundo conforme a posição do rato

## TP1 — Jogo da Vida e DLA

- **Jogo da Vida de Conway:** classes `Cell`, `GameOfLife` e `TesteCA`, com vizinhança e
  conjunto de regras configuráveis
- **DLA (*Diffusion-Limited Aggregation*):** classes `Walker` e `DLA`, com e sem *stickiness*
- Dois ensaios: origem dos autómatos celulares e o Jogo da Vida, e o DLA (definição, história
  e relação com a densidade urbana)

## TP2 — Física e agentes autónomos

- **Queda livre com atrito (Silico):** diagramas de *feedback*, tempo de queda e velocidade
- **Sistema solar e sistema de partículas:** planetas em órbita, zoom e variação da constante G
- **Agentes autónomos, comportamentos individuais:** *boid* com acelerador e travão,
  *wander* e comutação de comportamento
- **Agentes autónomos, comportamentos de grupo:** predador a perseguir uma presa,
  *flock* com liderança e *debugging*

## TP3 — Caos e fractais

- **Função logística** e efeito borboleta (em Python)
- **Jogo do Caos:** versão simples e versão com número arbitrário de pontos fixos
- **Gramáticas de Lindenmayer:** objetos com L-Systems e árvores de fruto
- **Conjuntos de Mandelbrot e Julia,** com gradiente de cor

## Projeto Final — Ecossistema da savana

Jogo/simulação de uma savana africana que integra os conteúdos da cadeira:

- **Terreno** gerado por autómato celular 2D (vazio, obstáculo, fértil, alimento)
- **Leões (predadores) e zebras (presas),** com energia, reprodução e penalização ao
  atravessar obstáculos
- **Herói** controlado pelo jogador, que defende os animais dos **caçadores**
- **Comportamentos:** `Wander`, `Pursuit`, `Seek`, `Evade` e `AvoidObstacles`
- **Genética:** classes `DNA` e `Eye`
- **Simulação prévia** do ecossistema em Silico (*Stocks & Flows*)

---

## Tecnologias utilizadas

- Java e Processing 3
- Silico (simulação de sistemas dinâmicos)
- Loopy (diagramas de ciclos causais)
- UML
