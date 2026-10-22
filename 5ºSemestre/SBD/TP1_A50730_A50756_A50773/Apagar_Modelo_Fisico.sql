-- ISEL 24/25 - Sistemas de Bases de Dados - LEIM51D
-- Código realizado pelo grupo:
-- João Ramos - A50730
-- Fábio Pestana - A50756
-- Rafael Dias - A50773
-- Este código tem como objetivo remover o modelo fisico da base de dados, elaborado no código Modelo_Fisico.sql.

USE SBD_TP1;

-- Remover as chaves estrangeiras
ALTER TABLE Veiculo DROP CONSTRAINT FK_Veiculo_Categoria;
ALTER TABLE Intervencao DROP CONSTRAINT FK_Intervencao_Matricula;
ALTER TABLE Aluguer DROP CONSTRAINT FK_Aluguer_Matricula;
ALTER TABLE Aluguer DROP CONSTRAINT FK_Aluguer_NIF;
ALTER TABLE Avaliacao DROP CONSTRAINT FK_Avaliacao_ID;
ALTER TABLE ParqueEstacionamento_Veiculo DROP CONSTRAINT FK_PE_Morada;
ALTER TABLE ParqueEstacionamento_Veiculo DROP CONSTRAINT FK_PE_Matricula;
ALTER TABLE ParqueEstacionamento_TipoVeiculo DROP CONSTRAINT FK_PETV_Morada;
ALTER TABLE ParqueEstacionamento_TipoVeiculo DROP CONSTRAINT FK_PETV_Categoria;
ALTER TABLE Desconto_Aluguer DROP CONSTRAINT FK_AD_ID;
ALTER TABLE Desconto_Aluguer DROP CONSTRAINT FK_AD_Codigo;
ALTER TABLE Cliente_Condutor DROP CONSTRAINT FK_CC_NIF;
ALTER TABLE Cliente_Desconto DROP CONSTRAINT FK_CD_NIF;
ALTER TABLE Cliente_Desconto DROP CONSTRAINT FK_CD_Codigo;

-- Apagar as tabelas das associações
DROP TABLE IF EXISTS Cliente_Desconto;
DROP TABLE IF EXISTS Cliente_Condutor;
DROP TABLE IF EXISTS Desconto_Aluguer;
DROP TABLE IF EXISTS ParqueEstacionamento_TipoVeiculo;
DROP TABLE IF EXISTS ParqueEstacionamento_Veiculo;

-- Apagar as tabelas das entidades
DROP TABLE IF EXISTS Avaliacao;
DROP TABLE IF EXISTS Desconto;
DROP TABLE IF EXISTS Aluguer;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS Intervencao;
DROP TABLE IF EXISTS Veiculo;
DROP TABLE IF EXISTS TipoVeiculo;
DROP TABLE IF EXISTS ParqueEstacionamento;

-- Eliminar a Base de Dados
DROP DATABASE IF EXISTS sbd_tp1;