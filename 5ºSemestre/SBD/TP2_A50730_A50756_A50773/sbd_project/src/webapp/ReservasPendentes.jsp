<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Funcionario"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>


<% 
String msg = (String) request.getAttribute("mensagem");

Funcionario func = new Funcionario();
String[][] resultados = func.consultarReservasPendentesWeb();
String html = "<p> Não existem reservas pendentes! </p>";

if(resultados.length != 0 && resultados[0].length != 0){
	String[] titulos = {"ID", "Data e hora de inicio", "tipoVeiculo", "Modelo", "Parque"};
	html = Auxiliar.generateHtmlTable(resultados, titulos);
}

// Converte o resultado em uma String de opções para a lista
StringBuilder options = new StringBuilder();
for (String[] row : resultados) {
    String freguesia = row[0];
    options.append("<option value=\"").append(freguesia).append("\">").append(freguesia).append("</option>");
}

String option =  options.toString();
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
            <a class="button-link" href="Funcionario.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Reservas Pendentes</h1>
    </header>
    <main>
	    <div>
		    <% if (msg != null) { %>
		        <p style="color: <%= msg.startsWith("Sucesso") ? "green" : "red" %>;">
		            <%= msg %>
		        </p>
		    <% } %>
		</div>
    	<div>
             <%= html != null ? html : "" %>
        </div>
        <form id="ranking-form" method="POST" action="FuncionarioReservaServlet">
            <label for="reserva">Selecione uma Reserva:</label>
            <select id="reserva" name="reserva" required>
    			<option value="" selected disabled>Selecione uma Reserva</option>
                <%= option != null ? option : "" %> 
            </select>
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