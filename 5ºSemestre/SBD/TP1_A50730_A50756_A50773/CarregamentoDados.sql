-- ISEL 24/25 - Sistemas de Bases de Dados - LEIM51D
-- Código realizado pelo grupo:
-- João Ramos - A50730
-- Fábio Pestana - A50756
-- Rafael Dias - A50773
-- Este código tem como objetivo a adicionar dados de teste, para assim testar o modelo elaborado no código Modelo_Fisico.sql.

USE SBD_TP1;

-- Inserir dados nas tabelas das entidades

-- Inserir dados na tabela do Parque de Estacionamento
INSERT INTO ParqueEstacionamento (Morada, CoordenadasGeograficas) 
VALUES 
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', '38.8831,-9.0383'),
    ('Avenida Marginal, Cascais, 2750-334', '38.6961,-9.4217'),
    ('Rua Elias Garcia, Amadora, 2700-327', '38.7522,-9.2308');
    
-- Inserir dados na tabela do Tipo de Veículo
INSERT INTO TipoVeiculo (Categoria, TempoMinCartaConducao, CapacidadeCarga, IdadeMinCondutor, Tarifa)
VALUES
    ('familiar', 2, 500, 21, 30),
    ('comercial', 3, 1250, 23, 40),
    ('motociclo', 1, 50, 18, 20),
    ('transporte de mercadorias', 4, 2000, 25, 50),
    ('desportivo', 3, 300, 21, 70);
    
-- Inserir dados na tabela do Veículo
INSERT INTO Veiculo (Matricula, Cor, Marca, Modelo, NumeroLugares, NumeroPortas, PotenciaMotor, TipoMotor, Estado, FotosVideos, RegistoKm, Categoria)
VALUES  
    ('AA-01-AA', 'Vermelho', 'Toyota', 'Corolla', 5, 4, 132, 'gasolina', 'disponível', NULL, 12345.6, 'familiar'),
	('AA-02-AA', 'Preto', 'Mercedes', 'A200', 5, 4, 163, 'gasolina', 'disponível', NULL, 5000.5, 'familiar'),
	('AA-03-AA', 'Azul', 'Toyota', 'Corolla', 5, 4, 132, 'gasolina', 'manutenção', NULL, 12345.6, 'familiar'),
	('AA-04-AA', 'Branco', 'Mercedes', 'A200', 5, 4, 163, 'gasolina', 'alugado', NULL, 11200.0, 'familiar'),
    ('BB-02-BB', 'Preto', 'Honda', 'Civic', 5, 4, 158, 'gasolina', 'alugado', NULL, 7890.2, 'familiar'),
    ('CC-03-CC', 'Azul', 'Ford', 'Focus', 5, 5, 120, 'gasóleo', 'manutenção', NULL, 5678.9, 'familiar'),
    ('DD-04-DD', 'Prata', 'BMW', '320i', 5, 4, 184, 'híbrido', 'disponível', NULL, 20000.0, 'desportivo'),
    ('EE-05-EE', 'Branco', 'Mercedes', 'A200', 5, 4, 163, 'gasolina', 'alugado', NULL, 15000.5, 'familiar'),
    ('FF-06-FF', 'Cinza', 'Volkswagen', 'Golf', 5, 4, 110, 'gasóleo', 'disponível', NULL, 8000.0, 'familiar'),
    ('GG-07-GG', 'Verde', 'Chevrolet', 'Cruze', 5, 4, 140, 'GPL', 'disponível', NULL, 9500.0, 'familiar'),
    ('HH-08-HH', 'Amarelo', 'Tesla', 'Model 3', 5, 4, 300, 'elétrico', 'disponível', NULL, 3000.0, 'familiar'),
    ('RR-18-RR', 'Amarelo', 'Porsche', '911', 2, 2, 450, 'gasolina', 'disponível', NULL, 5000.0, 'desportivo'),
    ('SS-19-SS', 'Amarelo', 'Chevrolet', 'Camaro', 2, 2, 460, 'gasolina', 'disponível', NULL, 1500.0, 'desportivo');
    
-- Inserir dados na tabela das intervenções feitas a veículos
INSERT INTO Intervencao (DataHoraIntervencao, Custo, Tipo, Matricula)
VALUES    
    ('2020-01-10 08:30:00', 150.0, 'manutenção', 'AA-01-AA'),
    ('2021-02-15 14:45:00', 250.0, 'reparação', 'AA-01-AA'),
    ('2019-03-12 10:00:00', 300.0, 'reparação', 'BB-02-BB'),
    ('2022-05-20 09:30:00', 180.0, 'manutenção', 'BB-02-BB'),
    ('2024-06-05 11:15:00', 220.0, 'manutenção', 'CC-03-CC'),
    ('2022-08-25 16:00:00', 500.0, 'reparação', 'CC-03-CC'),
    ('2017-11-15 10:30:00', 420.0, 'reparação', 'DD-04-DD'),
    ('2024-09-10 13:45:00', 350.0, 'manutenção', 'DD-04-DD'),
    ('2023-04-18 14:00:00', 150.0, 'manutenção', 'EE-05-EE'),
    ('2024-01-12 09:00:00', 130.0, 'manutenção', 'FF-06-FF'),
	('2015-07-01 08:00:00', 700.0, 'reparação', 'GG-07-GG'),
    ('2024-04-22 14:00:00', 270.0, 'manutenção', 'GG-07-GG'),
    ('2018-10-05 15:00:00', 400.0, 'reparação', 'HH-08-HH'),
    ('2024-08-30 12:00:00', 180.0, 'manutenção', 'HH-08-HH'),
    ('2023-01-18 08:30:00', 500.0, 'manutenção', 'RR-18-RR'),
    ('2024-02-05 13:30:00', 450.0, 'manutenção', 'SS-19-SS');
    
-- Inserir dados na tabela dos Clientes
INSERT INTO Cliente (NIF, Tipo, Nome, Distrito, Concelho, Freguesia, Mail, Telemovel, CapitalSocial, Lingua, PreferenciaCultural, Pontos)
VALUES
    ('123456789012', 'pessoa', 'Ricardo Mendes', 'Lisboa', 'Lisboa', 'Areeiro', 'ricardo.mendes@gmail.com', '912345678', NULL, 'Português', NULL, 10),
    ('234567890123', 'empresa', 'Cláudia Martins', 'Lisboa', 'Lisboa', 'Alcântara', 'claudia.martins@empresa.com', '913456789', 5000, 'Português', NULL, 20),
    ('345678901234', 'pessoa', 'Pedro Silva', 'Porto', 'Porto', 'Boavista', 'pedro.silva@gmail.com', '914567890', NULL, 'Português', 'Cultura Portuguesa', 30),
    ('456789012345', 'empresa', 'Tech Solutions Ltd', NULL, NULL, NULL, 'ts.team@yahoo.com', '915678901', 10000, 'Inglês', NULL, 40),
    ('567890123456', 'pessoa', 'José Costa', 'Coimbra', 'Coimbra', 'Santo António dos Olivais', 'jose.costa@gmail.com', '916789012', NULL, 'Português', 'Islamismo', 50),
    ('678901234567', 'empresa', 'Windhoist Ltd', NULL, NULL, NULL, 'stephen.begg@windhoist.co.uk', '917890123', 40000, 'Inglês', 'Sustentabilidade', 60),
    ('789012345678', 'pessoa', 'Mariana Almeida', 'Faro', 'Faro', 'Almancil', 'mariana.almeida@hotmail.com', '918901234', NULL, 'Inglês', NULL, 70),
    ('890123456789', 'empresa', 'Empresa x', 'Lisboa', 'Lisboa', 'Benfica', 'empresaX@gmail.com', '919012345', 15000, 'Português', NULL, 80),
    ('901234567890', 'pessoa', 'Patrícia Rocha', 'Aveiro', 'Aveiro', 'Glória', 'patricia.rocha@gmail.com', '920123456', NULL, 'Português', 'Culinária', 90),
    ('012345678901', 'empresa', 'Empresa Y', 'Porto', 'Porto', 'Centro', 'empresaY@gmail.com', '921234567', 25000, 'Português', 'Inclusividade', 100);

-- Inserir dados na tabela dos Alugueres
INSERT INTO Aluguer (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinal, AvaliacaoCondutor, Matricula, NIF)
VALUES
    ('ALUG00001', 30, 'EUR', '2018-11-01 08:00:00', '2018-11-03 18:11:00', 45.00, 5, 'AA-01-AA', '123456789012'),
    ('ALUG00002', 30, 'EUR', '2021-09-01 08:00:00', '2021-09-13 18:11:00', 390.00, 3, 'AA-01-AA', '567890123456'),
    ('ALUG00003', 30, 'EUR', '2024-10-01 08:00:00', '2024-10-07 18:11:00', 180.00, 4, 'AA-01-AA', '012345678901'),
    ('ALUG00004', 30, 'USD', '2024-03-02 09:00:00', '2024-03-04 19:54:00', 36.00, 4, 'BB-02-BB', '234567890123'),
    ('ALUG00005', 30, 'GBP', '2020-07-03 10:00:00', '2020-07-07 20:23:00', 54.00, 3, 'CC-03-CC', '345678901234'),
    ('ALUG00006', 70, 'BRL', '2021-02-04 11:00:00', '2021-02-14 21:36:00', 700.00, 2, 'DD-04-DD', '456789012345'),
    ('ALUG00007', 30, 'JPY', '2019-01-05 12:00:00', '2019-01-08 22:39:00', 72.00, 5, 'EE-05-EE', '567890123456'),
    ('ALUG00008', 30, 'CHF', '2018-12-06 13:00:00', '2018-12-08 23:42:00', 40.50, 4, 'FF-06-FF', '678901234567');

-- Inserir dados na tabela do Desconto
INSERT INTO Desconto (Codigo, NumeroUtilizacoes, DataExpiracao, ValorPercentual)
VALUES
    ('NATAL2024', -1, '2025-01-06', 20), 
    ('PASCOA2024', 150, '2024-04-07', 15),
    ('DIAPAI2024', 200, '2024-03-20', 10),
    ('NAMORADOS24', 75, '2024-02-17', 25),
    ('ANIVEMPRESA', 50, '2024-11-18', 30), 
    ('NATAL2018', -1, '2019-01-06', 20),
    ('BLKFRIDAY', -1, '2018-11-30', 35),
	('NATAL2017', -1, '2018-01-06', 20),
    ('N1js3a2w24', 1, '2024-10-01', 15),
    ('D5eB4x8y10', 1, '2025-01-12', 15),
    ('1Wes23dq8e', 1, '2023-10-21', 15),
    ('Z2DAW1eDa2', 1, '2019-04-18', 15);
    
-- Inserir dados na tabela das Avaliações feitas pelos Clientes
INSERT INTO Avaliacao (ID, Nota, Comentario)
VALUES
    ('ALUG00001', 'Adorei', Null),
    ('ALUG00002', 'Gostei', 'O veículo estava bom, mas poderia ser mais limpo.'),
    ('ALUG00003', 'Não vou voltar', 'O veículo estava em mau estado e a experiência de condução foi desagradável.'),
    ('ALUG00004', 'Adorei', 'Excelente serviço, o veículo estava limpo e bem mantido.'),
    ('ALUG00005', 'Gostei', 'O veículo estava razoável, mas o processo de aluguer poderia ser mais simples.');


-- Inserir dados nas tabelas das associações entre entidades

-- Inserir dados na tabela da associação entre o Parque de Estacionamento e o Veículo
-- Encontram comentados os veiculos que não se encontram no parque
INSERT INTO ParqueEstacionamento_Veiculo (Morada, Piso, Fila, Numero, Matricula)
VALUES
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 1, 1, 1, 'DD-04-DD'),
    -- ('Rua das Flores, Alverca do Ribatejo, 2615-123', 1, 1, 2, 'EE-05-EE'), Alugado
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 1, 2, 1, 'GG-07-GG'),
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 1, 2, 2, 'HH-08-HH'),
    -- ('Avenida Marginal, Cascais, 2750-334', 1, 2, 2, 'AA-04-AA'), Alugado
    ('Avenida Marginal, Cascais, 2750-334', 1, 1, 1, 'AA-02-AA'),
    ('Avenida Marginal, Cascais, 2750-334', 1, 1, 2, 'FF-06-FF'),
    ('Avenida Marginal, Cascais, 2750-334', 1, 2, 1, 'RR-18-RR'),
    ('Avenida Marginal, Cascais, 2750-334', 1, 2, 2, 'SS-19-SS'),
    ('Rua Elias Garcia, Amadora, 2700-327', 1, 1, 1, 'AA-01-AA');
    -- ('Rua Elias Garcia, Amadora, 2700-327', 1, 2, 1, 'AA-03-AA'), Manutenção
    -- ('Rua Elias Garcia, Amadora, 2700-327', 2, 1, 1, 'BB-02-BB'), Alugado
    -- ('Rua Elias Garcia, Amadora, 2700-327', 2, 2, 1, 'CC-03-CC'); Manutenção
   
-- Inserir dados na tabela da associação entre o Parque de Estacionamento e o Tipo de veículo
INSERT INTO ParqueEstacionamento_TipoVeiculo (Morada, Categoria)
VALUES
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 'familiar'),
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 'comercial'),
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 'motociclo'),
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 'transporte de mercadorias'),
    ('Rua das Flores, Alverca do Ribatejo, 2615-123', 'desportivo'),
    ('Avenida Marginal, Cascais, 2750-334', 'familiar'),
    ('Avenida Marginal, Cascais, 2750-334', 'comercial'),
    ('Avenida Marginal, Cascais, 2750-334', 'motociclo'),
    ('Avenida Marginal, Cascais, 2750-334', 'transporte de mercadorias'),
    ('Avenida Marginal, Cascais, 2750-334', 'desportivo'),
    ('Rua Elias Garcia, Amadora, 2700-327', 'familiar'),
    ('Rua Elias Garcia, Amadora, 2700-327', 'comercial'),
    ('Rua Elias Garcia, Amadora, 2700-327', 'motociclo'),
    ('Rua Elias Garcia, Amadora, 2700-327', 'transporte de mercadorias');
    
-- Inserir dados na tabela da associação entre o Desconto e o Aluguer
INSERT INTO Desconto_Aluguer (ID, Codigo)
VALUES
    ('ALUG00001', 'BLKFRIDAY'),
    ('ALUG00007', 'NATAL2018'),
    ('ALUG00008', 'NATAL2017'),
    ('ALUG00003', 'N1js3a2w24');

-- Inserir dados na tabela da associação entre o Cliente e o Condutor
INSERT INTO Cliente_Condutor (NIF, NumeroCartaConducao, Nome, DataEmissaoCarta, DataNascimento, ValidadeCarta, Reputacao)
VALUES
    ('123456789012', 'CN1234567890', 'Ricardo Mendes', '2010-06-15', '1985-02-20', '2030-06-15', 4.5),
    ('345678901234', 'CN3456789012', 'Pedro Silva', '2012-07-10', '1990-05-12', '2032-07-10', 4.0),
    ('567890123456', 'CN5678901234', 'José Costa', '2008-03-22', '1980-01-05', '2028-03-22', 3.8),
    ('789012345678', 'CN7890123456', 'Mariana Almeida', '2015-09-08', '1995-08-18', '2035-09-08', 4.7),
    ('901234567890', 'CN9012345678', 'Patrícia Rocha', '2013-11-30', '1992-03-09', '2033-11-30', 4.3),
    ('234567890123', 'CN1122334455', 'Miguel Pais', '2005-01-15', '1985-02-20', '2025-01-15', 3.7),
    ('456789012345', 'CN2233445566', 'Tomás Morgado', '2011-04-25', '1990-05-12', '2031-04-25', 4.0),
    ('678901234567', 'CN3344556677', 'James Williams', '2009-06-20', '1980-01-05', '2029-06-20', 3.9),
    ('890123456789', 'CN4455667788', 'João Almeida', '2016-12-01', '1995-08-18', '2036-12-01', 4.8),
    ('012345678901', 'CN5566778899', 'André Rocha', '2014-05-17', '1992-03-09', '2034-05-17', 4.4);

-- Inserir dados na tabela da associação entre o Cliente e o Desconto
INSERT INTO Cliente_Desconto (NIF, Codigo)
VALUES
    ('012345678901', 'N1js3a2w24'),
    ('234567890123', 'D5eB4x8y10'),
    ('345678901234', '1Wes23dq8e'),
    ('456789012345', 'Z2DAW1eDa2');
