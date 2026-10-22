-- ISEL 24/25 - Sistemas de Bases de Dados - LEIM51D
-- Código realizado pelo grupo:
-- João Ramos - A50730
-- Fábio Pestana - A50756
-- Rafael Dias - A50773
-- Este código tem como objetivo a criação do modelo físico.

CREATE DATABASE SBD_TP1;
USE SBD_TP1;

-- Criação das tabelas das entidades, segundo o modelo relacional apresentado no relatório

-- Tabela do Parque de Estacionamento
CREATE TABLE ParqueEstacionamento (
    Morada VARCHAR(150) NOT NULL PRIMARY KEY,
    CoordenadasGeograficas VARCHAR(25) NOT NULL
);

-- Tabela do Tipo de veículo
-- Utiliza-se o CHECK para se verificar se a categoria encontra-se entre os seguintes valores:
-- familiar, comercial, motociclo, transporte de mercadorias, desportivo
CREATE TABLE TipoVeiculo (
    Categoria VARCHAR(30) NOT NULL PRIMARY KEY CHECK (Categoria IN ('familiar', 'comercial', 'motociclo', 'transporte de mercadorias', 'desportivo')),
    TempoMinCartaConducao INT NOT NULL,
    CapacidadeCarga INT NOT NULL,
    IdadeMinCondutor INT NOT NULL,
    Tarifa INT NOT NULL
);

-- Tabela do Veículo
-- Utiliza-se o CHECK para se verificar se o tipo de motor encontra-se entre os seguintes valores:
-- gasolina, gasóleo, GPL, elétrico, híbrido. Também se verifica se o estado do veiculo é um dos seguintes:
-- disponível, alugado ou manutenção
CREATE TABLE Veiculo (
    Matricula VARCHAR(10) NOT NULL PRIMARY KEY,
    Cor VARCHAR(20) NOT NULL,
    Marca VARCHAR(25) NOT NULL,
    Modelo VARCHAR(20) NOT NULL,
    NumeroLugares INT NOT NULL,
    NumeroPortas INT NOT NULL,
    PotenciaMotor INT NOT NULL,
    TipoMotor VARCHAR(15) NOT NULL CHECK (TipoMotor IN ('gasolina', 'gasóleo', 'GPL', 'elétrico', 'híbrido')),
    Estado VARCHAR(15) NOT NULL CHECK (Estado IN ('disponível', 'alugado', 'manutenção')),
    FotosVideos BLOB NULL,
    RegistoKm FLOAT NOT NULL,
    Categoria VARCHAR(30) NOT NULL
);

-- Tabela do Veículo
-- Utiliza-se o CHECK para se verificar se o tipo de intervenção encontra-se entre os seguintes valores:
-- reparação, manutenção
CREATE TABLE Intervencao (
    DataHoraIntervencao DATETIME NOT NULL,
    Custo FLOAT NOT NULL,
    Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('reparação', 'manutenção')),
    Matricula VARCHAR(10) NOT NULL,
    PRIMARY KEY (DataHoraIntervencao, Matricula)
);

-- Tabela do Cliente
-- Utiliza-se o CHECK para se verificar se o tipo de cliente encontra-se entre os seguintes valores:
-- empresa, pessoa
CREATE TABLE Cliente (
    NIF VARCHAR(12) NOT NULL PRIMARY KEY,
    Tipo VARCHAR(10) NOT NULL CHECK (Tipo IN ('empresa', 'pessoa')),
    Nome VARCHAR(35) NOT NULL,
    Distrito VARCHAR(20) NULL,
    Concelho VARCHAR(35) NULL,
    Freguesia VARCHAR(35) NULL,
    Mail VARCHAR(70) NOT NULL,
    Telemovel VARCHAR(15) NOT NULL,
    CapitalSocial INT NULL,
    Lingua VARCHAR(20) NOT NULL,
    PreferenciaCultural VARCHAR(50) NULL,
    Pontos INT NOT NULL
);

-- Tabela do Aluguer
-- Utiliza-se o CHECK para se verificar se a unidade monetária encontra-se entre os seguintes valores:
-- EUR, USD, GBP, BRL, JPY, CHF, CNY. Também se utiliza o Check, para garantir que o valor da avaliação 
-- dada ao condutor pela empresa é um valor entre 1 e 5, e também se utiliza para garantir que a data final do aluguer
-- é superior á de inicio
CREATE TABLE Aluguer (
    ID VARCHAR(10) NOT NULL PRIMARY KEY,
    Tarifa INT NOT NULL,
    UnidadeMonetaria VARCHAR(10) NOT NULL CHECK (UnidadeMonetaria IN ('EUR', 'USD', 'GBP', 'BRL', 'JPY', 'CHF','CNY')),
    DataHoraInicio DATETIME NOT NULL,
    DataHoraFinal DATETIME NOT NULL,
    CustoFinal FLOAT NOT NULL,
    AvaliacaoCondutor INT NOT NULL CHECK (AvaliacaoCondutor BETWEEN 1 AND 5),
    Matricula VARCHAR(10) NOT NULL,
    NIF VARCHAR(12) NOT NULL,
    CHECK (DataHoraFinal > DataHoraInicio)
);

-- Tabela do Desconto
CREATE TABLE Desconto (
    Codigo VARCHAR(12) NOT NULL PRIMARY KEY,
    NumeroUtilizacoes INT NOT NULL,
    DataExpiracao DATE NOT NULL,
    ValorPercentual INT NOT NULL
);

-- Tabela da Avaliação
CREATE TABLE Avaliacao (
	ID VARCHAR(10) NOT NULL PRIMARY KEY,
    Nota ENUM('Não vou voltar', 'Gostei', 'Adorei') NOT NULL,
    Comentario VARCHAR(500)
);

-- Criação das tabelas das associações entre entidades

-- Tabela da associação entre o parque de estacionamento e o veículo
CREATE TABLE ParqueEstacionamento_Veiculo (
    Morada VARCHAR(150)  NOT NULL,
    Piso INT NOT NULL,
    Fila INT NOT NULL,
    Numero INT NOT NULL,
    Matricula VARCHAR(10) NOT NULL,
    PRIMARY KEY (Matricula)
);

-- Tabela da associação entre o parque de estacionamento e o tipo de veículo
CREATE TABLE ParqueEstacionamento_TipoVeiculo (
    Morada VARCHAR(150) NOT NULL,
    Categoria VARCHAR(30) NOT NULL,
    PRIMARY KEY (Morada, Categoria)
);

-- Tabela da associação entre o desconto e o aluguer
CREATE TABLE Desconto_Aluguer (
    ID VARCHAR(10) NOT NULL,
    Codigo VARCHAR(12) NOT NULL,
    PRIMARY KEY (ID, Codigo)
);

-- Tabela da associação entre o cliente e o condutor
-- Utiliza-se o CHECK para se verificar se o valor da reputação está entre 1 e 5
CREATE TABLE Cliente_Condutor (
    NIF VARCHAR(12) NOT NULL,
    NumeroCartaConducao VARCHAR(12) NOT NULL,
    Nome VARCHAR(35) NOT NULL,
    DataEmissaoCarta DATE NOT NULL,
    DataNascimento DATE NOT NULL,
    ValidadeCarta DATE NOT NULL,
	Reputacao FLOAT NOT NULL CHECK (Reputacao BETWEEN 1 AND 5),
    PRIMARY KEY (NIF, NumeroCartaConducao)
);

-- Tabela da associação entre o cliente e o desconto
CREATE TABLE Cliente_Desconto (
    NIF VARCHAR(12) NOT NULL,
    Codigo VARCHAR(12) NOT NULL,
    PRIMARY KEY (NIF, Codigo)
);

-- Adiciona-se ás entidades as suas respetivas chaves estrangeiras
ALTER TABLE Veiculo
ADD CONSTRAINT FK_Veiculo_Categoria FOREIGN KEY (Categoria) REFERENCES TipoVeiculo(Categoria);

ALTER TABLE Intervencao
ADD CONSTRAINT FK_Intervencao_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula);

ALTER TABLE Aluguer
ADD CONSTRAINT FK_Aluguer_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula),
ADD CONSTRAINT FK_Aluguer_NIF FOREIGN KEY (NIF) REFERENCES Cliente(NIF);

ALTER TABLE Avaliacao
ADD CONSTRAINT FK_Avaliacao_ID FOREIGN KEY (ID) REFERENCES Aluguer(ID);

ALTER TABLE ParqueEstacionamento_Veiculo
ADD CONSTRAINT FK_PE_Morada FOREIGN KEY (Morada) REFERENCES ParqueEstacionamento(Morada),
ADD CONSTRAINT FK_PE_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula);

ALTER TABLE ParqueEstacionamento_TipoVeiculo
ADD CONSTRAINT FK_PETV_Morada FOREIGN KEY (Morada) REFERENCES ParqueEstacionamento(Morada),
ADD CONSTRAINT FK_PETV_Categoria FOREIGN KEY (Categoria) REFERENCES TipoVeiculo(Categoria);

ALTER TABLE Desconto_Aluguer
ADD CONSTRAINT FK_AD_ID FOREIGN KEY (ID) REFERENCES Aluguer(ID),
ADD CONSTRAINT FK_AD_Codigo FOREIGN KEY (Codigo) REFERENCES Desconto(Codigo);

ALTER TABLE Cliente_Condutor
ADD CONSTRAINT FK_CC_NIF FOREIGN KEY (NIF) REFERENCES Cliente(NIF);

ALTER TABLE Cliente_Desconto
ADD CONSTRAINT FK_CD_NIF FOREIGN KEY (NIF) REFERENCES Cliente(NIF),
ADD CONSTRAINT FK_CD_Codigo FOREIGN KEY (Codigo) REFERENCES Desconto(Codigo);

-- Criação de vistas para facilitar consultas e análises de dados

-- Vista que detalha os alugueres realizados, indicando informações sobre o 
-- cliente, condutor e veículo associados ao aluguer.
CREATE VIEW AlugueresDetalhados AS
SELECT 
    A.ID,
    A.DataHoraInicio,
    A.DataHoraFinal,
    A.CustoFinal,
    A.AvaliacaoCondutor,
    C.Nome AS NomeCliente,
    C.Mail AS EmailCliente,
    CC.Nome AS NomeCondutor,
    CC.NumeroCartaConducao AS CartaDeConducaoCondutor,
    V.Marca AS MarcaVeiculo,
    V.Modelo AS ModeloVeiculo,
    V.Matricula
FROM Aluguer A
JOIN Cliente C ON A.NIF = C.NIF
JOIN Veiculo V ON A.Matricula = V.Matricula
JOIN Cliente_Condutor CC ON A.NIF = CC.NIF;

-- Select que permite a visualização da vista
SELECT * FROM AlugueresDetalhados;

-- Vista que apresenta o histórico das intervenções realizadas nos veículos, 
-- com informações de custo e tipo de intervenção feita.
CREATE VIEW HistoricoDasIntervencoes AS
SELECT 
    V.Matricula,
    V.Marca,
    V.Modelo,
    I.DataHoraIntervencao,
    I.Tipo AS TipoIntervencao,
    I.Custo
FROM Veiculo V
JOIN Intervencao I ON V.Matricula = I.Matricula;

-- Select que permite a visualização da vista
SELECT * FROM HistoricoDasIntervencoes;

-- Vista que resume informações sobre veículos, inclui o número de vezes que estes 
-- foram alugados e as receitas totais geradas por veículo
CREATE VIEW ResumoDosVeiculos AS
SELECT 
    V.Matricula,
    V.Marca,
    V.Modelo,
    COUNT(A.ID) AS TotalAlugueres,
    COALESCE(SUM(A.CustoFinal), 0) AS ReceitaTotal
FROM Veiculo V
LEFT JOIN Aluguer A ON V.Matricula = A.Matricula
GROUP BY V.Matricula, V.Marca, V.Modelo;

-- Select que permite a visualização da vista
SELECT * FROM ResumoDosVeiculos;

-- Vista que ajuda na interpretação dos descontos, pois substitui 
-- o número de utilizações com valor -1 pelo valor "Ilimitado".
CREATE VIEW InterpretacaoDosDescontos AS
SELECT 
    Codigo,
    CASE 
        WHEN NumeroUtilizacoes = -1 THEN 'Ilimitado'
        ELSE NumeroUtilizacoes
    END AS NumeroUtilizacoes,
    DataExpiracao,
    ValorPercentual
FROM Desconto;

-- Select que permite a visualização da vista
SELECT * FROM InterpretacaoDosDescontos;

