# Modelação e Programação — Trabalhos Práticos

Conjunto de **5 trabalhos práticos** (TP1, TP2, TP3 e TP4 em duas partes) desenvolvidos
na unidade curricular **Modelação e Programação** (MoP) de LEIM, no
**ISEL**, ano letivo 2022/23.

- **Aluno:** Miguel Alcobia — 50746
- **Aluno:** Fábio Pestana — 50756
- **Turma:** 22D
- **Docente responsável:** João Ventura

> **Nota:** o TP1, o TP2 e o TP3 foram realizados **em grupo** (Miguel Alcobia e Fábio Pestana).
> O TP4 (Partes A e B), acompanhado de relatório, foi realizado **individualmente**
> (Fábio Pestana).

---

## Descrição

Trabalhos práticos em **Java** que abordam os conceitos de **programação orientada a
objetos**, **herança e interfaces**, **XML/DTD/XPath** e **interfaces gráficas**, incluindo:

- Revisões de Java: ciclos, arrays, strings e classes/objetos
- Modelação de classes a partir de diagramas **UML**
- Coleções de objetos geridas com arrays
- Herança, classes abstratas e interfaces (`Obra`/`IObra`, `Evento`)
- Leitura e escrita de ficheiros **XML** (`javax.xml`, `org.w3c.dom`)
- Definição de gramáticas **DTD** e consultas **XPath**
- Interface gráfica com **Java Swing**
- Documentação do código com **Javadoc**

---

## TP4 — Projeto Final: Gestão de um Ginásio

Aplicação de gestão de um ginásio, com dados persistidos em **XML** (com a respetiva
gramática **DTD**) e menus adaptados a cada tipo de utilizador (Cliente,
Personal Trainer, Auxiliar de limpeza e Gestor).

**Classes principais:** `Person` (abstrata), `Client`, `Employee`, `PersonalTrainer`,
`Pack`, `Equipamento`, `Build_Classes` e `Ginasio`.

**Funcionalidades:**
- Login com validação de credenciais e registo de novos clientes
- Gestão de clientes, funcionários, personal trainers e equipamentos
- Atribuição de clientes (com o pack 2) a personal trainers
- Bónus salarial para personal trainers com muitos clientes
- Cálculo de necessidades calóricas dos clientes
- Consulta das finanças do ginásio
- Guardar e carregar dados em XML (`Gymdados.xml` / `GinasioBd.xml`)

### Parte A — Aplicação em consola
- Menus em modo texto geridos pela classe `AppMenus`

### Parte B — Interface gráfica
- Interface em **Java Swing** (`JPanel`, `JTable`, `JTextField`, pop-ups)
  organizada na classe `MenuInterface` e em vários painéis (Login, Perfil,
  registos, tabelas, menus por cargo, etc.)
- Imagens de fundo e ícone desenhados em **Canva**

---

## Tecnologias utilizadas

- Java
- Java Swing
- XML, DTD e XPath
- UML
- Canva (recursos gráficos do TP4)
