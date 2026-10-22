<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Funcionario"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>


<% 
Funcionario func = new Funcionario();

String html = (String) request.getAttribute("resultados");
String reserva = (String) request.getAttribute("reserva");
String opcoes = (String) request.getAttribute("opcoes");

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
            <a class="button-link" href="ReservasPendentes.jsp">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Atribuir Veiculo à Reserva: <%= reserva != null ? reserva : "" %></h1>
    </header>
    <main>
    	<div>
             <%= html != null ? html : "" %>
        </div>
        <form id="ranking-form" method="POST" action="Funcionario1Servlet">
            <label for="matricula">Selecione um Veiculo:</label>
            <select id="matricula" name="matricula" required>
    			<option value="" selected disabled>Selecione uma Matricula</option>
                <%= opcoes != null ? opcoes : "" %> 
            </select>
            <input type="hidden" name="reserva" value="<%= reserva != null ? reserva : "" %>">
            <button type="submit">Atribuir Veiculo</button>
        </form>
    </main>
     <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>