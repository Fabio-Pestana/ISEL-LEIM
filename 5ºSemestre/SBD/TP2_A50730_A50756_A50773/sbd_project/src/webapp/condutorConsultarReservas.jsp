<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Condutor"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>


<% 
String msg = (String) request.getAttribute("mensagem");

String nif = (String) session.getAttribute("nif");

Condutor condutor = new Condutor();
String[][] resultados = condutor.consultarReservasPendentes(nif);
String html = "<p> Não existem reservas pendentes! </p>";

if(resultados.length != 0 && resultados[0].length != 0){
	String[] titulos = {"Id Aluguer", "Matricula", "Marca", "Modelo", "Cor", "MoradaParque", "Piso", "Fila", "Número"};
	html = Auxiliar.generateHtmlTable(resultados, titulos);
}

// Converte o resultado em uma String de opções para a lista
StringBuilder options = new StringBuilder();
for (String[] row : resultados) {
    String matricula = row[1];
    options.append("<option value=\"").append(matricula).append("\">").append(matricula).append("</option>");
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
            <a class="button-link" href="Condutor.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Levantar Veículo</h1>
    </header>
    <main style="width: 800px">
	    <div>
             <%= msg != null ? msg : "" %>
        </div>
    	<div>
             <%= html != null ? html : "" %>
        </div>
        <form id="ranking-form" method="POST" action="Condutor1Servlet">
            <label for="matricula">Selecione uma Matricula:</label>
            <select id="matricula" name="matricula" required>
    			<option value="" selected disabled>Selecione uma Matricula</option>
                <%= option != null ? option : "" %> 
            </select>
            <button type="submit">Levantar Veiculo</button>
        </form>
    </main>
     <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>