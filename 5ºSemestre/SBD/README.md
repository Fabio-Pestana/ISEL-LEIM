# Sistemas de Bases de Dados - Projeto

Projeto da unidade curricular de **Sistemas de Bases de Dados** (SBD) de LEIM, no ISEL, ano letivo 2024/2025.

- **Fábio Pestana** — 50756
- **João Ramos** — 50730
- **Rafael Dias** — 50773
- **Turma:** 51D
- **Docente responsável:** António Teófilo

---

## Descrição

O tema do projeto é o sistema de informação de uma **empresa de aluguer de veículos automóveis**, que opera em vários parques de estacionamento e serve clientes particulares e empresas. Este projeto foi dividido em duas partes:

- **TP1:** conceção e implementação da base de dados: modelo entidade-associação, modelo relacional, restrições de integridade aplicacional (RIA) e scripts SQL (criação, carregamento de dados de teste e eliminação do modelo físico).
- **TP2:** evolução do modelo da base de dados (reservas, lugares de estacionamento e novas vistas) e desenvolvimento de uma **aplicação web em Java** (JDBC, JSP e Servlets) que acede à base de dados MySQL.

---

## Estrutura do repositório

```
SBD/
├── TP1/
│   ├── TP1_A50730_A50756_A50773.pdf     # Relatório do TP1
│   ├── Modelo entidade-associação.png   # Modelo entidade-associação
│   ├── Modelo_Fisico.sql                # Criação da base de dados SBD_TP1 (tabelas, chaves e vistas)
│   ├── CarregamentoDados.sql            # Dados de teste
│   └── Apagar_Modelo_Fisico.sql         # Eliminação do modelo físico
└── TP2/
    ├── TP2_A50730_A50756_A50773.pdf     # Relatório do TP2
    ├── ModeloFisico.png                 # Modelo físico
    ├── Modelo_Fisico.sql                # Criação da base de dados SBD (tabelas, chaves e vistas)
    ├── CarregamentoDados.sql            # Dados de teste
    ├── Apagar_Modelo_Fisico.sql         # Eliminação do modelo físico
    ├── remote users.sql                 # Criação de utilizador para acesso remoto ao MySQL
    └── sbd_project/                      # Aplicação web
        └── src/
            ├── java
            │   ├── sbd/                 # Camada de dados e lógica de negócio
            │   │   ├── Stub.java            # Ligação JDBC e execução de SQL
            │   │   ├── Auxiliar.java        # Validações, formatação e geração de HTML
            │   │   ├── Administrador.java
            │   │   ├── Cliente.java
            │   │   ├── Condutor.java
            │   │   ├── Funcionario.java
            │   │   └── Gerente.java
            │   └── web/                 # Servlets (um ou mais por funcionalidade)
            └── webapp/                  # Páginas JSP/HTML e estilos CSS
```

---

## TP1 — Conceção e modelo físico

**Modelo de dados**

Entidades: Parque de Estacionamento, Tipo de Veículo, Veículo, Intervenção, Aluguer, Desconto, Avaliação, Cliente e Condutor (associado ao cliente).
Associações: Parque–Veículo (piso, fila e número do lugar), Parque–Tipo de Veículo, Desconto–Aluguer, Cliente–Condutor e Cliente–Desconto.

O relatório documenta, para cada entidade, os domínios dos atributos (tipos, valores permitidos e *nulls*), as chaves primárias, candidatas e estrangeiras, e os pressupostos assumidos (por exemplo, a morada como chave do parque).

**Restrições de integridade aplicacional (RIA)**

Regras implementadas na aplicação e não no modelo, como: decremento das utilizações de um desconto (`-1` = ilimitado), cálculo do custo final com desconto e conversão para a unidade monetária escolhida, geração automática do ID do aluguer, conversão da data de nascimento em idade, validação de que o aluguer não ultrapassa a validade da carta de condução, duração do aluguer entre 1 hora e 14 dias, cálculo da reputação do condutor (média das avaliações) e criação de descontos próprios com pontos acumulados.

**Scripts SQL**
- `Modelo_Fisico.sql`: `CREATE DATABASE SBD_TP1`, tabelas, chaves estrangeiras (adicionadas no fim com `ALTER TABLE`) e quatro vistas (`AlugueresDetalhados`, `HistoricoDasIntervencoes`, `ResumoDosVeiculos` e `InterpretacaoDosDescontos`).
- `CarregamentoDados.sql`: inserção de dados de teste.
- `Apagar_Modelo_Fisico.sql`: remoção das chaves estrangeiras, tabelas e base de dados.

---

## TP2 — Aplicação web "My Rent a Car"

**Alterações ao modelo da parte 1**
- Nova entidade fraca **Lugar de Estacionamento** (piso, fila, número e estado `vago`/`ocupado`).
- A entidade Aluguer foi dividida em **Reserva** (ainda não validada por um funcionário) e **Aluguer** (validado, em curso ou concluído). Uma reserva passa a aluguer quando um funcionário lhe atribui um veículo, o desconto passou a estar associado à reserva.
- Novas vistas para suportar a aplicação: `AlugueresEIntervencoes`, `LucroTotalPorMarca`, `EstadoReservas`, `DetalhesAlugueresCondutor`, `AlugueresIncompletos` e `ReservasComAluguer`.

**Perfis de utilizador e funcionalidades**

| Perfil | Funcionalidades |
|---|---|
| **Administrador** | Criar e atualizar clientes e condutores, criar e atualizar veículos, exportar para JSON os dados de um veículo (alugueres e intervenções num período) e importar dados a partir de JSON |
| **Cliente** | Reservar um veículo (por modelo ou categoria, com desconto e conversão de moeda), consultar o estado das reservas e consultar reputação e descontos |
| **Condutor** | Consultar reservas pendentes, levantar um veículo, procurar lugares disponíveis, entregar um veículo e avaliar o serviço |
| **Funcionário** | Atribuir um veículo a uma reserva (criando o aluguer), localizar um veículo, registar intervenções, procurar clientes (com *autocomplete*), finalizar um aluguer (custo final, avaliação do condutor, quilómetros), procurar o condutor de um veículo numa data e hora |
| **Gerente** | Histórico de um veículo, ranking de marcas por menor lucro, ranking de modelos por melhor avaliação, ranking de veículos com menos quilómetros nos últimos três meses, ranking de clientes por reputação numa freguesia |

**Arquitetura**
- **Camada de dados (`sbd`):** a classe `Stub` gere a ligação JDBC ao MySQL e a execução de consultas e atualizações, `Auxiliar` contém validações (nome, telefone, e-mail, NIF, matrícula, carta de condução) e geradores de HTML, há uma classe por perfil de utilizador.
- **Camada web (`web`):** as páginas JSP contêm os formulários e submetem-nos para Servlets, que instanciam as classes de negócio, validam os dados e devolvem mensagens de sucesso ou erro.

---

## Tecnologias utilizadas

- **MySQL** (SQL DDL e DML, vistas)
- **Java 8**, **JDBC** (`mysql-connector-j`)
- **JSP** e **Servlets** (Jakarta Servlet API), **HTML** e **CSS**
- **Maven** (packaging WAR) e **Apache Tomcat**
- Bibliotecas **Jackson** e **org.json** para importação e exportação de JSON
- **Eclipse** como ambiente de desenvolvimento
