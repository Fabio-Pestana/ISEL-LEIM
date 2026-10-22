<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Cliente"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>


<% 
String nif = (String) session.getAttribute("nif");

Cliente cliente = new Cliente(nif);
String[][] resultados = cliente.clienteConsultarReservas();
String html = "<p> Não existem reservas pendentes! </p>";

if(resultados.length != 0 && resultados[0].length != 0){
	String[] titulos = {"Data e hora de inicio", "Data e hora de fim", "Estado Reserva", "Custo Final Previsto",
			"Custo Final Efetivo", "Unidade Monetária", "Avaliação Condutor", "Marca do Veículo", "Modelo"};
	html = Auxiliar.generateHtmlTable(resultados, titulos);
}
%>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Rent-a-Car</title>
</head>
<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Cliente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Consultar Estado das Reservas</h1>
    </header>
    <main style="width: 800px">
    	<div>
             <%= html %>
        </div>
    </main>
     <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>