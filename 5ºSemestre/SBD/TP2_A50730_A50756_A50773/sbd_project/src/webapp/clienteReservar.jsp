<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>
<%@page import="pt.isel.leim.sbd.Stub"%>
<%@page import="pt.isel.leim.sbd.Cliente"%>


<% 
String msg = (String) request.getAttribute("mensagem");

String nif = (String) session.getAttribute("nif");

Cliente cliente = new Cliente(nif);
String[][] resultados = Auxiliar.listarParquesWeb(cliente.getStub());
String[][] resultadosM = cliente.listarModelosDisponiveis();
String[][] resultadosV = cliente.listarCategoriasVeiculosDisponiveis();


// Converte o resultado em uma String de opções para a lista
String parques = Auxiliar.gerarOpcoes(resultados, 0);
String modelos = Auxiliar.gerarOpcoes(resultadosM, 1);
String veiculos = Auxiliar.gerarOpcoes(resultadosV, 0);
%>
<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <title>My Rent-a-Car</title>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Cliente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Reservar Veículo</h1>
    </header>
    <main>
		<form method="POST" action="Cliente1Servlet" onsubmit="return validarFormulario()">
        	<label for="Parque">Selecione o Parque de Estacionamento:</label>
            <select id="Parque" name="Parque" required>
    			<option value="" selected disabled>Escolha um Parque</option>
                <%= parques != null ? parques : "" %> 
            </select>
            
            <label for="TipoVeiculo">Selecione o Tipo de Veículo:</label>
            <select id="TipoVeiculo" name="TipoVeiculo">
    			<option value="" selected disabled>Escolha uma opção</option>
                <%= veiculos != null ? veiculos : "" %> 
            </select>
            
            <label for="Modelo">Ou selecione o Modelo:</label>
            <select id="Modelo" name="Modelo">
    			<option value="" selected disabled>Escolha uma opção</option>
                <%= modelos != null ? modelos : "" %> 
            </select>
            
            <label for="DataInicio">Data de Início:</label>
            <input type="date" id="DataInicio" name="DataInicio" required>

            <label for="DataFim">Data de Fim:</label>
            <input type="date" id="DataFim" name="DataFim" required>
            
            <label for="UnidadeMonetaria">Selecione a Unidade Monetária:</label>
            <select id="UnidadeMonetaria" name="UnidadeMonetaria" required>
                <option value="" selected disabled>Escolha uma moeda</option>
                <option value="EUR">EUR</option>
                <option value="USD">USD</option>
                <option value="GBP">GBP</option>
                <option value="BRL">BRL</option>
                <option value="JPY">JPY</option>
                <option value="CHF">CHF</option>
                <option value="CNY">CNY</option>
            </select>
 
            <div class="wrap">
                <button type="submit">Reservar</button>
            </div>
        </form>
        <div>
             <%= msg != null ? msg : "" %>
        </div>
    </main>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
    <script>
	    function validarFormulario() {
	        const tipoVeiculo = document.getElementById('TipoVeiculo').value;
	        const modelo = document.getElementById('Modelo').value;
	
	        if (tipoVeiculo == "" && modelo == "") {
	            alert("Deve selecionar pelo menos um: Tipo de Veículo ou Modelo.");
	            return false;
	        }
	
	        return true; 
	    }
        const tipoVeiculo = document.getElementById('TipoVeiculo');
        const modelo = document.getElementById('Modelo');
        const dataInicio = document.getElementById('DataInicio');
        const dataFim = document.getElementById('DataFim');

        tipoVeiculo.addEventListener('change', function () {
            if (tipoVeiculo.value) {
                modelo.value = "";
            }
        });
        modelo.addEventListener('change', function () {
            if (modelo.value) {
                tipoVeiculo.value = "";
            }
        });

        dataFim.addEventListener('change', function () {
            const inicio = new Date(dataInicio.value);
            const fim = new Date(dataFim.value);
            if (inicio && fim) {
                const diffTime = Math.abs(fim - inicio);
                const diffDays = diffTime / (1000 * 60 * 60 * 24);
                if (diffDays > 14) {
                    alert('A diferença entre as datas não pode ser maior que 14 dias.');
                    dataFim.value = "";
                }
            }
        });
    </script>
    
</body>
</html>