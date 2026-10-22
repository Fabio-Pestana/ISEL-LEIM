-- ISEL 24/25 - SBD
-- João Ramos - A50730
-- Fábio Pestana - A50756
-- Rafael Dias - A50773

USE SBD;

-- Remover as chaves estrangeiras
ALTER TABLE Veiculo DROP CONSTRAINT FK_Veiculo_Categoria;
ALTER TABLE LugarEstacionamento DROP CONSTRAINT FK_LugarEstacionamento_Morada;
ALTER TABLE Intervencao DROP CONSTRAINT FK_Intervencao_Matricula;
ALTER TABLE Aluguer DROP CONSTRAINT FK_Aluguer_Matricula;
ALTER TABLE Aluguer DROP CONSTRAINT FK_Aluguer_IDReserva;
ALTER TABLE Avaliacao DROP CONSTRAINT FK_Avaliacao_ID;
ALTER TABLE LugarEstacionamento_Veiculo DROP CONSTRAINT FK_LugarEstacionamento_Composite;
ALTER TABLE LugarEstacionamento_Veiculo DROP CONSTRAINT FK_PE_Matricula;
ALTER TABLE ParqueEstacionamento_TipoVeiculo DROP CONSTRAINT FK_PETV_Morada;
ALTER TABLE ParqueEstacionamento_TipoVeiculo DROP CONSTRAINT FK_PETV_Categoria;
ALTER TABLE Desconto_Reserva DROP CONSTRAINT FK_DR_ID;
ALTER TABLE Desconto_Reserva DROP CONSTRAINT FK_DR_Codigo;
ALTER TABLE Cliente_Condutor DROP CONSTRAINT FK_CC_NIF;

-- Apagar as tabelas das associações
DROP TABLE IF EXISTS Cliente_Condutor;
DROP TABLE IF EXISTS Desconto_Reserva;
DROP TABLE IF EXISTS ParqueEstacionamento_TipoVeiculo;
DROP TABLE IF EXISTS LugarEstacionamento_Veiculo;

-- Apagar as tabelas das entidades
DROP TABLE IF EXISTS Avaliacao;
DROP TABLE IF EXISTS Desconto;
DROP TABLE IF EXISTS Reserva;
DROP TABLE IF EXISTS Aluguer;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS Intervencao;
DROP TABLE IF EXISTS Veiculo;
DROP TABLE IF EXISTS TipoVeiculo;
DROP TABLE IF EXISTS LugarEstacionamento;
DROP TABLE IF EXISTS ParqueEstacionamento;

-- Eliminar a Base de Dados
DROP DATABASE IF EXISTS sbd;