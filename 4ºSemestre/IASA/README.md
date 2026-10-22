# Inteligência Artificial para Sistemas Autónomos — Projeto

Projeto desenvolvido na unidade curricular **Inteligência Artificial para Sistemas Autónomos** (IASA) de LEIM, no **ISEL**, ano letivo 2023/24.

O projeto foi desenvolvido ao longo do semestre, em **4 partes** entregues em aula, e culmina num agente autónomo que explora um ambiente virtual, recolhe alvos e evita obstáculos, primeiro com um **controlo reativo** e depois com um **controlo deliberativo** (procura em espaço de estados e processos de decisão de Markov).

- **Aluno:** Fábio Pestana — 50756
- **Turma:** 42D
- **Docente responsável:** Luís Morgado

---

## Descrição

O projeto tem por base uma arquitetura de apoio fornecida pelos docentes. Cada parte acrescenta uma camada ao sistema anterior:

| Parte | Tema | Linguagem |
|-------|------|-----------|
| 1 | Engenharia de software: agente reativo num jogo de consola | Java |
| 2 | Agente reativo: comportamentos, reações e seleção de ações | Python |
| 3 | Procura em espaço de estados | Python |
| 4 | Agente deliberativo: planeamento com procura (PEE) e com PDM | Python |

Os conceitos teóricos de cada parte estão descritos no **relatório** (`iasa_50756.pdf`).

---

## Estrutura

```
IASA/
├── README.md
├── iasa_50756.pdf             # Relatório final
├── iasa-info-proj.pdf         # Formato das entregas do projeto
├── iasa-info-relat.pdf        # Organização do relatório
└── iasa50756/
    ├── iasa_jogo/             # Parte 1 (Java)
    │   └── src/
    │       ├── agente/        # Agente, Controlo, Percepcao, Accao
    │       ├── ambiente/      # Ambiente, Evento, Comando
    │       ├── jogo/          # Jogo, AmbienteJogo, Personagem, ...
    │       └── maqest/        # Máquina de estados
    └── iasa_agente/           # Partes 2, 3 e 4 (Python)
        ├── consola.bat        # Abre uma consola com o PYTHONPATH configurado
        └── src/
            ├── agente/        # Agentes reativo e deliberativos
            ├── contagem/      # Problema da contagem (parte 3)
            └── lib/           # Bibliotecas: ecr, mod, pee, plan, pdm, sae
```

---

## Parte 1 — Jogo com agente reativo (Java)

Jogo de consola em que uma personagem (agente) **fotografa animais**. O jogador simula os eventos do ambiente através do teclado e a personagem reage-lhes segundo o ciclo **perceção–processamento–ação**.

- **Arquitetura base** com interfaces `Ambiente`, `Evento`, `Comando` e `Controlo`, e classes `Agente`, `Percepcao` e `Accao`
- **Jogo:** `AmbienteJogo`, `EventoJogo` e `ComandoJogo` (*enums*), e `Personagem` com `ControloPersonagem`
- **Máquina de estados** (`maqest`: `Estado`, `Transicao`, `MaquinaEstados`), que representa as funções de transição (δ) e de saída (λ)
- **Dicionário** (`HashMap`) que liga os inputs do jogador aos eventos do jogo:

| Input | `s` | `r` | `a` | `f` | `o` | `t` |
|-------|-----|-----|-----|-----|-----|-----|
| Evento | `SILENCIO` | `RUIDO` | `ANIMAL` | `FUGA` | `FOTOGRAFIA` | `TERMINAR` |

## Parte 2 — Agente reativo (Python)

Agente **autónomo** que explora o ambiente, recolhe alvos e evita obstáculos, usando esquemas comportamentais reativos (package `ecr`) sobre o simulador fornecido pelo docente (package `sae`).

- **Conceitos:** `Estimulo`, `Resposta`, `Reaccao`, `Comportamento` e `ComportComp`
- **Mecanismos de seleção de ação:** `Hierarquia` e `Prioridade`
- **Comportamentos:**
  - `AproximarAlvo` (prioridade), com `AproximarDir` e `EstimuloAlvo`
  - `EvitarObst` (hierarquia), com `EvitarDir` (com memória), `EstimuloObst` e `RespostaEvitar`
  - `Explorar`
  - `Recolher`: comportamento composto, em hierarquia, com `AproximarAlvo`, `EvitarObst` e `Explorar`

## Parte 3 — Procura em espaço de estados (Python)

Modelo de problemas e mecanismos de procura, testados em consola com o **problema da contagem** dado pelo docente.

- **Modelo do problema** (`mod`): `Estado`, `Operador` e `Problema`
- **Procura em espaço de estados** (`pee`): `Nó`, `Fronteira` e `MecanismoProcura`
  - **Profundidade:** normal, limitada e iterativa
  - **Largura**
  - **Melhor-primeiro:** custo uniforme, sôfrega e A*, com os respetivos avaliadores e heurística

## Parte 4 — Agente deliberativo (Python)

O agente deixa de reagir a estímulos e passa a **raciocinar**: planeia o caminho até aos alvos e desvia-se dos obstáculos.

- **Controlo deliberativo** (`controlo_delib`): `ControloDelib`, `MecDelib`, e o modelo do mundo (`EstadoAgente`, `ModeloMundo`, `OperadorMover`)
- **Planeamento com procura** (`plan_pee`): `PlaneadorPEE`, `PlanoPEE`, problema de planeamento e heurística de distância
- **Planeamento com Processos de Decisão de Markov** (`pdm` e `plan_pdm`): `PDM`, `MecUtil`, `ModeloPDMPlan`, `PlaneadorPDM` e `PlanoPDM`, com taxa de desconto γ = 0,98

---

## Como executar

### Parte 1 (Java)

Abrir a pasta `iasa50756` no IntelliJ IDEA (ou outro IDE Java) e executar a classe `Jogo`. Em alternativa, a partir da pasta `iasa50756`:

```bash
javac -d out $(find iasa_jogo -name "*.java")
java -cp out iasa_jogo.src.jogo.Jogo
```

### Partes 2, 3 e 4 (Python)

Requisitos: **Python 3** e **Pygame** (`pip install pygame`).

A partir da pasta `iasa50756/iasa_agente`, com o `PYTHONPATH` configurado (o `consola.bat` abre uma consola com isto feito):

```bat
set PYTHONPATH=src;src\lib

python src\agente\agente_reativo.py
python src\agente\agente_deliberativo.py
python src\agente\agente_deliberativo_pdm.py
python src\contagem\contagem.py
```

| Ficheiro | Descrição |
|----------|-----------|
| `agente_reativo.py` | Agente reativo (`Recolher`) no ambiente 1 |
| `agente_deliberativo.py` | Agente deliberativo com PEE no ambiente 4 |
| `agente_deliberativo_pdm.py` | Agente deliberativo com PDM no ambiente 4 |
| `agente_teste.py` | Agente de teste, com um comportamento simples |
| `contagem.py` | Resolução do problema da contagem com procura sôfrega |

> **Nota:** o package `lib/sae` (simulador de ambientes) foi fornecido pelo docente; os restantes packages foram desenvolvidos nas aulas.

---

## Tecnologias utilizadas

- Java
- Python 3
- Pygame (simulador gráfico)
- IntelliJ IDEA / PyCharm
