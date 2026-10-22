<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>
<%@page import="pt.isel.leim.sbd.Stub"%>
<%@page import="pt.isel.leim.sbd.Cliente"%>

<% 
String parque = (String) request.getAttribute("Parque");
String categoria = (String) request.getAttribute("TipoVeiculo");
String modelo = (String) request.getAttribute("Modelo");
String inicio = (String) request.getAttribute("DataInicio");
String fim = (String) request.getAttribute("DataFim");
String unidade = (String) request.getAttribute("UnidadeMonetaria");
double custo = (Double) request.getAttribute("Custo");
double tarifa = (Double) request.getAttribute("Tarifa");

String nif = (String) session.getAttribute("nif");

Cliente cliente = new Cliente(nif);
String[][] resultados = cliente.verDescontos();

StringBuilder options = new StringBuilder();

for (String[] row : resultados) {
    String codigo = row[0];
    String valorPercentual = row[2];
    options.append("<option value=\"").append(codigo).append("\">")
           .append(valorPercentual).append("</option>");
}

String option = options.toString();
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
            <a class="button-link" href="clienteReservar.jsp">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Reservar Veículo</h1>
    </header>
    <main>
        <form method="POST" action="Cliente2Servlet">
            <h2>Detalhes da Reserva:</h2>
                <p><strong>Parque: </strong><%= parque %></p>
                <p><strong>Tipo de Veículo: </strong><%= categoria %></p>
                <p><strong>Modelo: </strong><%= modelo %></p>
                <p><strong>Data de Início: </strong><%= inicio %></p>
                <p><strong>Data de Fim: </strong><%= fim %></p>
                <p><strong>Unidade Monetária: </strong><%= unidade %></p>
                <p><strong>Custo Inicial: </strong><%= custo %> <%= unidade %></p>
                
            <input type="hidden" name="parque" value="<%= parque %>">
            <input type="hidden" name="categoria" value="<%= categoria %>">
            <input type="hidden" name="modelo" value="<%= modelo %>">
            <input type="hidden" name="inicio" value="<%= inicio %>">
            <input type="hidden" name="fim" value="<%= fim %>">
            <input type="hidden" name="unidade" value="<%= unidade %>">
            <input type="hidden" name="custo" value="<%= custo %>">
            <input type="hidden" name="tarifa" value="<%= tarifa %>">
            <label for="discountCode">Se tiver um desconto, insira-o aqui:</label>
            <input type="text" id="discountCode" name="discountCode" placeholder="Código de desconto">

            <div class="wrap">
            	<button type="submit">Terminar</button>
            </div>
        </form>
    </main>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>
