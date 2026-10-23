# Desenvolvimento de Aplicações Móveis - Trabalhos Práticos e Projeto

Trabalhos da unidade curricular de **Desenvolvimento de Aplicações Móveis** (DAM) de LEIM, no ISEL, ano letivo 2024/2025.

- **Fábio Pestana** — 50756
- **Turma:** 61D
- **Docente responsável:** Pedro Fazenda

---

## Descrição

Esta pasta reúne uma seleção dos trabalhos da unidade curricular:

- **Laboratório 1 (L1) e Laboratório 2 (L2):** exercícios de **Kotlin** (programação orientada a objetos, ciclos e sequências, genéricos, *lambdas*, DSL e sobrecarga de operadores).
- **Projeto final:** **GymWiki**, uma aplicação **Android** para pesquisar exercícios de ginásio, marcar favoritos e criar listas de treino personalizadas, desenvolvida com **Jetpack Compose** e **Firebase**.

> Nota: o repositório não inclui todos os exercícios práticos da unidade curricular, apenas os dois laboratórios de Kotlin e o projeto final.

---

## Estrutura do repositório

```
DAM/
├── DAM_L1_A50756/                          # Laboratório 1 - Kotlin
│   ├── exer_07/                            # Animal, Bird, Mammal e Zoo
│   │   ├── Animal.kt
│   │   ├── Bird.kt
│   │   ├── Mammal.kt
│   │   └── Zoo.kt
│   ├── exer_21/exer_21.kt                  # Sequência de Fibonacci
│   ├── exer_22/exer_22.kt                  # Calculadora
│   └── exer_23/exer_23.kt                  # Sequências com generateSequence
├── DAM_L2_A50756/                          # Laboratório 2 - Kotlin
│   ├── exer_11/exer_11.kt                  # Ordenação de livros (fun interface)
│   ├── exerc_12/exer_12.kt                 # MagicBox<T> (genéricos)
│   ├── exerc_13/exer_13.kt                 # Receita (DSL e plusAssign)
│   └── exerc_14/exer_14.kt                 # BitNumbers (números de 512 bits)
├── DAM_PRJ_A50756/                         # Projeto final - aplicação Android GymWiki
│   ├── app/
│   │   ├── build.gradle.kts
│   │   └── src/main/
│   │       ├── AndroidManifest.xml
│   │       ├── assets/                     # JSON dos exercícios por grupo muscular
│   │       ├── res/                        # Imagens e ícones
│   │       └── java/dam_A50756/gymwiki/
│   │           ├── MainActivity.kt
│   │           ├── auxiliar/ExerciciosFirestore.kt
│   │           ├── data/                   # MyFirebase, ExerciseData, UserData
│   │           ├── ui/                     # Ecrãs em Compose e tema
│   │           └── viewmodel/SearchViewModel.kt
│   ├── gradle/libs.versions.toml
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── Project Proposal - A50756 - Gym Log.pdf # Proposta do projeto
└── DAM_PRJ_FinalPhase_A50756.pdf           # Relatório final do projeto
```

---

## Laboratório 1 — Kotlin

| Exercício | Descrição |
|---|---|
| `exer_07` | Hierarquia de classes: a classe abstrata `Animal` (nome, idade com validação, espécie e função abstrata `reproduce`) é estendida por `Bird` (com atributo `canFly`) e `Mammal` (com cor do pelo). A classe `Zoo` guarda uma lista de animais, permite adicioná-los, listá-los e fazê-los reproduzir. |
| `exer_21` | Cálculo dos 100 primeiros números de Fibonacci de duas formas, pela fórmula de Binet numa só linha e por iteração, comparando depois os resultados e indicando o índice onde deixam de coincidir (devido à precisão de vírgula flutuante). |
| `exer_22` | Calculadora em consola: lê dois números (com validação da entrada), suporta `+`, `-`, `*`, `/`, `&&`, `||`, `!`, `shl` e `shr`, e apresenta o resultado em decimal, hexadecimal e binário. |
| `exer_23` | Utilização de `generateSequence`, `filter`, `take` e `toList`: gera uma sequência de palavras a partir de "hello", acrescentando letras aleatórias, e mantém as de comprimento par. O código do enunciado (números primos) está comentado no ficheiro. |

---

## Laboratório 2 — Kotlin

| Exercício | Descrição |
|---|---|
| `exer_11` | Ordenação de uma lista de `Book` (título, autor, ano) por título, ano e autor, através de uma `fun interface` (`BookComparator`) usada com expressões lambda. |
| `exer_12` | Classe genérica `MagicBox<T>` que guarda, devolve e transforma um valor (`store`, `retrieve` e `transform`), testada com `Int`, `String` e uma `data class`. |
| `exer_13` | Pequena DSL para construir uma receita: a classe `Recipe` sobrecarrega o operador `plusAssign` para adicionar passos (descrição e ação) e `buildRecipe` recebe um bloco com receiver. |
| `exer_14` | Classe `BitNumbers` que representa números de 512 bits (`ByteArray` de 64 bytes) com os operadores `plus` e `minus`, tratando o *carry* e o *borrow* byte a byte. |

---

## Projeto final — GymWiki

Aplicação Android para pessoas que gostam de fitness e de ginásios. Permite pesquisar exercícios, consultar informação detalhada (equipamento, dificuldade, instruções e vídeo de demonstração) e organizar treinos em listas personalizadas. A ideia inicial (proposta "Gym Log", com registo de séries, repetições e pesos) foi simplificada no desenvolvimento para uma aplicação de consulta e planeamento, com o nome **GymWiki**.

### Funcionalidades

- **Pesquisa de exercícios** por nome, com resultados em tempo real.
- **Categorização** por tipo de exercício (cardio, força, etc.) e por grupo muscular (peito, bíceps, abdominais, etc.).
- **Página de exercício** com equipamento, dificuldade (principiante, intermédio ou avançado), instruções e vídeo do YouTube.
- **Favoritos**, com ícone de coração animado.
- **Listas personalizadas:** criar, adicionar e remover exercícios e eliminar listas.
- **Autenticação** com Firebase: registo, login, logout e recuperação da palavra-passe por email.
- **Perfil do utilizador** com nome, email e foto (editáveis), favoritos e listas.

### Arquitetura

| Módulo | Responsabilidade |
|---|---|
| `auxiliar` | `ExerciciosFirestore.kt` importa os JSON da pasta `assets` para a coleção `exercicios` do Firestore |
| `data` | `MyFirebase` (autenticação e Firestore) e classes de dados (`ExerciseData`, `UserData`) |
| `ui` | Ecrãs em Jetpack Compose, navegação (`GymWikiApp.kt` com `NavHost`) e tema |
| `viewmodel` | `SearchViewModel` e `SearchUIState` (padrão MVVM na pesquisa) |

**Navegação:** o destino inicial depende do estado da autenticação (utilizador autenticado vai para a página principal, caso contrário para o login). Existem rotas estáticas (`login`, `profile`) e dinâmicas (`list/{categoryType}`, `exercise_page/{id}`).

**Base de dados (Firestore):**
- Coleção `exercicios`: nome, tipo, grupo muscular, equipamento, dificuldade, instruções e ID do vídeo do YouTube.
- Coleção `users`: foto de perfil (em Base64), IDs dos exercícios favoritos e listas personalizadas (nome e IDs dos exercícios). O nome e o email ficam na Firebase Authentication.

### Decisões e limitações

- Os dados dos exercícios estão numa base de dados própria no Firestore, porque a API considerada inicialmente (API Ninjas) tinha poucas chamadas gratuitas e não fornecia imagens nem vídeos.
- A foto de perfil é guardada em Base64 no Firestore, para evitar o uso do Firebase Storage.
- O serviço *premium* previsto na proposta (mais de 4 listas e mais de 5 resultados por pesquisa) **não foi implementado**.
- Os exercícios não têm imagens individuais, para evitar inconsistências visuais.

---

## Tecnologias utilizadas

- **Kotlin** 2.0 e **Android Studio**
- **Jetpack Compose** (Material 3) e **Navigation Compose**
- Arquitetura **MVVM** (`ViewModel`)
- **Firebase** Authentication e Cloud Firestore
- **Android YouTube Player** para os vídeos dos exercícios
- **Coil** e **Gson**
- **Gradle** (Kotlin DSL) com *version catalog*
- Android SDK mínimo 25, *target* e *compile* 35
