-- ISEL 24/25 - SBD
-- João Ramos - A50730
-- Fábio Pestana - A50756
-- Rafael Dias - A50773

CREATE DATABASE SBD;
USE SBD;

-- Criação das tabelas das entidades
CREATE TABLE ParqueEstacionamento (
    Morada VARCHAR(150) NOT NULL PRIMARY KEY,
    Latitude VARCHAR(25) NOT NULL,
    Longitude VARCHAR(25) NOT NULL
);

CREATE TABLE LugarEstacionamento (
    Morada VARCHAR(150)  NOT NULL,
    Piso INT NOT NULL,
    Fila INT NOT NULL,
    Numero INT NOT NULL,
    Estado VARCHAR(15) NOT NULL CHECK (Estado IN ('vago', 'ocupado')),
    PRIMARY KEY (Morada, Piso, Fila, Numero)
);

CREATE TABLE TipoVeiculo (
    Categoria VARCHAR(30) NOT NULL PRIMARY KEY CHECK (Categoria IN ('familiar', 'comercial', 'motociclo', 'transporte de mercadorias', 'desportivo')),
    TempoMinCartaConducao INT NOT NULL,
    CapacidadeCarga INT NOT NULL,
    IdadeMinCondutor INT NOT NULL,
    Tarifa INT NOT NULL
);

CREATE TABLE Veiculo (
    Matricula VARCHAR(10) NOT NULL PRIMARY KEY,
    Cor VARCHAR(20) NOT NULL,
    Marca VARCHAR(25) NOT NULL,
    Modelo VARCHAR(20) NOT NULL,
    NumeroLugares INT NOT NULL,
    NumeroPortas INT NOT NULL,
    PotenciaMotor INT NOT NULL,
    TipoMotor VARCHAR(15) NOT NULL CHECK (TipoMotor IN ('gasolina', 'gasóleo', 'GPL', 'elétrico', 'híbrido')),
    Estado VARCHAR(15) NOT NULL CHECK (Estado IN ('disponível', 'alugado', 'manutenção', 'reservado')),
    FotosVideos BLOB NULL,
    RegistoKm FLOAT NOT NULL,
    Categoria VARCHAR(30) NOT NULL
);

CREATE TABLE Intervencao (
    DataHoraIntervencao DATETIME NOT NULL,
    Custo FLOAT NOT NULL,
    Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('reparação', 'manutenção')),
    Matricula VARCHAR(10) NOT NULL,
    PRIMARY KEY (DataHoraIntervencao, Matricula)
);

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
    PreferenciaCultural VARCHAR(50) NULL
);

CREATE TABLE Aluguer (
    ID VARCHAR(10) NOT NULL,
    CustoFinalEfetivo FLOAT,
	AvaliacaoCondutor INT CHECK (AvaliacaoCondutor BETWEEN 1 AND 5 OR AvaliacaoCondutor IS NULL),
    Matricula VARCHAR(10) NOT NULL,
	KmPercorridos FLOAT, 
    IDReserva VARCHAR(10) NOT NULL,
    PRIMARY KEY (ID, IDReserva)
);

CREATE TABLE Reserva (
    ID VARCHAR(10) NOT NULL PRIMARY KEY,
    Tarifa INT NOT NULL,
    UnidadeMonetaria VARCHAR(10) NOT NULL CHECK (UnidadeMonetaria IN ('EUR', 'USD', 'GBP', 'BRL', 'JPY', 'CHF','CNY')),
    DataHoraInicio DATETIME NOT NULL,
    DataHoraFinal DATETIME NOT NULL,
    CustoFinalPrevisto FLOAT NOT NULL,
    NIF VARCHAR(12) NOT NULL,
	TipoVeiculo VARCHAR(30),
    Modelo VARCHAR(20),
	ParqueEstacionamento VARCHAR(150) NOT NULL,
    CHECK (DataHoraFinal > DataHoraInicio)
);

CREATE TABLE Desconto (
    Codigo VARCHAR(12) NOT NULL PRIMARY KEY,
    NumeroUtilizacoes INT NOT NULL,
    DataExpiracao DATE NOT NULL,
    ValorPercentual INT NOT NULL
);

CREATE TABLE Avaliacao (
	ID VARCHAR(10) NOT NULL PRIMARY KEY,
    Nota ENUM('Não vou voltar', 'Gostei', 'Adorei') NOT NULL,
    Comentario VARCHAR(500)
);

-- Criação das tabelas das associações entre entidades
CREATE TABLE LugarEstacionamento_Veiculo (
	Morada VARCHAR(150)  NOT NULL,
    Piso INT NOT NULL,
    Fila INT NOT NULL,
    Numero INT NOT NULL,
    Matricula VARCHAR(10) NOT NULL,
	PRIMARY KEY (Morada, Piso, Fila, Numero, Matricula)
);

CREATE TABLE ParqueEstacionamento_TipoVeiculo (
    Morada VARCHAR(150) NOT NULL,
    Categoria VARCHAR(30) NOT NULL,
    PRIMARY KEY (Morada, Categoria)
);

CREATE TABLE Desconto_Reserva (
    ID VARCHAR(10) NOT NULL,
    Codigo VARCHAR(12) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE Cliente_Condutor (
    NIF VARCHAR(12) NOT NULL,
    NumeroCartaConducao VARCHAR(12) NOT NULL,
    Nome VARCHAR(35) NOT NULL,
    DataEmissaoCarta DATE NOT NULL,
    DataNascimento DATE NOT NULL,
    ValidadeCarta DATE NOT NULL,
	Reputacao FLOAT CHECK (Reputacao BETWEEN 1 AND 5),
    PRIMARY KEY (NIF, NumeroCartaConducao)
);

-- Adicionando chaves estrangeiras
ALTER TABLE Veiculo
ADD CONSTRAINT FK_Veiculo_Categoria FOREIGN KEY (Categoria) REFERENCES TipoVeiculo(Categoria);

ALTER TABLE LugarEstacionamento
ADD CONSTRAINT FK_LugarEstacionamento_Morada FOREIGN KEY (Morada) REFERENCES ParqueEstacionamento(Morada);

ALTER TABLE Intervencao
ADD CONSTRAINT FK_Intervencao_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula);

ALTER TABLE Aluguer
ADD CONSTRAINT FK_Aluguer_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula),
ADD CONSTRAINT FK_Aluguer_IDReserva FOREIGN KEY (IDReserva) REFERENCES Reserva(ID);

ALTER TABLE Avaliacao
ADD CONSTRAINT FK_Avaliacao_ID FOREIGN KEY (ID) REFERENCES Aluguer(ID);

ALTER TABLE Reserva
ADD CONSTRAINT FK_Reserva_NIF FOREIGN KEY (NIF) REFERENCES Cliente(NIF);

ALTER TABLE LugarEstacionamento_Veiculo
ADD CONSTRAINT FK_LugarEstacionamento_Composite FOREIGN KEY (Morada, Piso, Fila, Numero)
REFERENCES LugarEstacionamento(Morada, Piso, Fila, Numero),
ADD CONSTRAINT FK_PE_Matricula FOREIGN KEY (Matricula) REFERENCES Veiculo(Matricula);

ALTER TABLE ParqueEstacionamento_TipoVeiculo
ADD CONSTRAINT FK_PETV_Morada FOREIGN KEY (Morada) REFERENCES ParqueEstacionamento(Morada),
ADD CONSTRAINT FK_PETV_Categoria FOREIGN KEY (Categoria) REFERENCES TipoVeiculo(Categoria);

ALTER TABLE Desconto_Reserva
ADD CONSTRAINT FK_DR_ID FOREIGN KEY (ID) REFERENCES Reserva(ID),
ADD CONSTRAINT FK_DR_Codigo FOREIGN KEY (Codigo) REFERENCES Desconto(Codigo);

ALTER TABLE Cliente_Condutor
ADD CONSTRAINT FK_CC_NIF FOREIGN KEY (NIF) REFERENCES Cliente(NIF);

-- Vistas Criadas

CREATE VIEW AlugueresDetalhados AS
SELECT 
    A.ID,
    R.DataHoraInicio,
    R.DataHoraFinal,
    R.CustoFinalPrevisto AS CustoFinal,
    A.AvaliacaoCondutor,
    C.Nome AS NomeCliente,
    C.Mail AS EmailCliente,
    V.Marca AS MarcaVeiculo,
    V.Modelo AS ModeloVeiculo,
    V.Matricula
FROM Aluguer A
JOIN Reserva R ON A.IDReserva = R.ID
JOIN Cliente C ON R.NIF = C.NIF
JOIN Veiculo V ON A.Matricula = V.Matricula;

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

CREATE VIEW ResumoDosVeiculos AS
SELECT 
    V.Matricula,
    V.Marca,
    V.Modelo,
    COUNT(A.ID) AS TotalAlugueres,
    COALESCE(SUM(R.CustoFinalPrevisto), 0) AS ReceitaTotal
FROM Veiculo V
LEFT JOIN Aluguer A ON V.Matricula = A.Matricula
LEFT JOIN Reserva R ON A.IDReserva = R.ID
GROUP BY V.Matricula, V.Marca, V.Modelo;


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

CREATE VIEW AlugueresEIntervencoes AS
SELECT 
    V.Matricula,
    V.Marca,
    V.Modelo,
    'em serviço' AS TipoIntervencao,
    A.ID AS AluguerID,
    R.DataHoraInicio AS DataHoraIntervencao,
    R.DataHoraFinal AS DataHoraFimIntervencao,
    R.CustoFinalPrevisto AS Custo,
    R.UnidadeMonetaria AS UnidadeMonetaria
FROM Veiculo V
JOIN Aluguer A ON V.Matricula = A.Matricula
JOIN Reserva R ON A.IDReserva = R.ID

UNION ALL

SELECT 
    V.Matricula,
    V.Marca,
    V.Modelo,
    I.Tipo AS TipoIntervencao,
    NULL AS AluguerID,
    I.DataHoraIntervencao AS DataHoraIntervencao,
    NULL AS DataHoraFimIntervencao,
    I.Custo AS Custo,
    'EUR' AS UnidadeMonetaria
FROM Veiculo V
JOIN Intervencao I ON V.Matricula = I.Matricula

ORDER BY DataHoraIntervencao;

CREATE VIEW LucroTotalPorMarca AS
SELECT 
    V.Marca, 
    SUM(
        CASE 
            WHEN R.UnidadeMonetaria = 'EUR' THEN COALESCE(A.CustoFinalEfetivo, 0)
            WHEN R.UnidadeMonetaria = 'USD' THEN COALESCE(A.CustoFinalEfetivo, 0) / 1.04
            WHEN R.UnidadeMonetaria = 'GBP' THEN COALESCE(A.CustoFinalEfetivo, 0) / 0.83
            WHEN R.UnidadeMonetaria = 'BRL' THEN COALESCE(A.CustoFinalEfetivo, 0) / 6.43
            WHEN R.UnidadeMonetaria = 'JPY' THEN COALESCE(A.CustoFinalEfetivo, 0) / 164
            WHEN R.UnidadeMonetaria = 'CHF' THEN COALESCE(A.CustoFinalEfetivo, 0) / 0.94
            WHEN R.UnidadeMonetaria = 'CNY' THEN COALESCE(A.CustoFinalEfetivo, 0) / 7.59
            ELSE 0
        END
    ) AS LucroTotalEmEuros
FROM Veiculo V
LEFT JOIN Aluguer A ON V.Matricula = A.Matricula
LEFT JOIN Reserva R ON A.IDReserva = R.ID
GROUP BY V.Marca
ORDER BY LucroTotalEmEuros ASC;


CREATE VIEW EstadoReservas AS
SELECT 
	R.NIF,
    R.DataHoraInicio,
    R.DataHoraFinal,
    CASE 
        WHEN A.ID IS NULL THEN 'Reserva Pendente'  -- Se não houver aluguer, a reserva está pendente
        WHEN R.DataHoraFinal < NOW() THEN 'Aluguer Passado'  -- Se a data final da reserva já passou
        ELSE 'Aluguer Validado'  -- Se houver aluguer e a data final não passou
    END AS EstadoReserva,
    R.CustoFinalPrevisto AS CustoFinalPrevisto,
    COALESCE(A.CustoFinalEfetivo, 0) AS CustoFinalEfetivo,
    R.UnidadeMonetaria,
    A.AvaliacaoCondutor,
    V.Marca AS MarcaVeiculo,
    V.Modelo AS ModeloVeiculo
FROM 
    Reserva R
LEFT JOIN Aluguer A ON R.ID = A.IDReserva  
LEFT JOIN Veiculo V ON A.Matricula = V.Matricula
ORDER BY R.DataHoraInicio;

CREATE VIEW DetalhesAlugueresCondutor AS
SELECT 
    A.ID AS IDAluguer,
    A.Matricula,
    CC.NIF,
    R.CustoFinalPrevisto AS CustoPrevistoReserva,
    CC.Nome AS NomeCondutor,
    CC.Reputacao AS ReputacaoCondutor
FROM Aluguer A
JOIN Reserva R ON A.IDReserva = R.ID
JOIN Cliente_Condutor CC ON R.NIF = CC.NIF;

CREATE VIEW AlugueresIncompletos AS
SELECT
	R.NIF AS NIFCliente,
    A.ID AS IDAluguer,
    V.Marca,
    V.Modelo,
    V.Cor,
    V.Matricula,
    L.Morada AS MoradaParque,
    L.Piso,
    L.Fila,
    L.Numero
FROM Aluguer A
JOIN Veiculo V ON A.Matricula = V.Matricula
LEFT JOIN LugarEstacionamento_Veiculo L ON V.Matricula = L.Matricula
JOIN Reserva R ON A.IDReserva = R.ID
WHERE A.KmPercorridos IS NULL;


CREATE VIEW ReservasComAluguer AS
SELECT
    R.ID AS IDReserva,
    R.Tarifa AS Tarifa, 
    R.UnidadeMonetaria as UnidadeMonetaria,
    R.DataHoraInicio,
    R.DataHoraFinal,
    R.CustoFinalPrevisto AS CustoPrevisto,
    R.NIF,
    R.TipoVeiculo,
    R.Modelo AS ModeloVeiculo,
    R.ParqueEstacionamento AS ParqueDoLevantamento,
	A.ID AS IDAluguer,
    A.CustoFinalEfetivo AS CustoFinal,
    A.AvaliacaoCondutor,
    A.Matricula,
    A.KmPercorridos
FROM Reserva R
JOIN Aluguer A ON R.ID = A.IDReserva
ORDER BY R.DataHoraInicio;



