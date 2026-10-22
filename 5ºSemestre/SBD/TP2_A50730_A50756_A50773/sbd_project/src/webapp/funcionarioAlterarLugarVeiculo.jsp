<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>
<%@page import="pt.isel.leim.sbd.Stub"%>

<% 
String msg = (String) request.getAttribute("mensagem");

String[][] resultados = Auxiliar.listarParquesWeb(new Stub());

// Converte o resultado em uma String de opções para a lista
StringBuilder options = new StringBuilder();
for (String[] row : resultados) {
    String parque = row[0];
    options.append("<option value=\"").append(parque).append("\">").append(parque).append("</option>");
}

String option =  options.toString();
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
            <a class="button-link" href="Funcionario.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Alterar lugar de um veículo</h1>
    </header>
    <main>
        <form id="vehicle-form" method="POST" action="Funcionario7Servlet">
        	<label for="parque">Selecione um Parque:</label>
            <select id="parque" name="parque" required>
    			<option value="" selected disabled>Selecione um Parque</option>
                <%= option != null ? option : "" %> 
            </select>
            <label for="license-plate">Insira a matrícula:</label>
            <input type="text" id="license-plate" name="license-plate" placeholder="Matricula (XX-XX-XX)" required>
            
            <label for="piso">Insira o piso:</label>
            <input type="number" id="piso" name="piso" placeholder="Insira o piso do veiculo" required>
            
            <label for="fila">Insira a fila:</label>
            <input type="number" id="fila" name="fila" placeholder="Insira a fila do veiculo" required>
            
            <label for="numero">Insira o número:</label>
            <input type="number" id="numero" name="numero" placeholder="Insira o número do veiculo" required>
            
            <button type="submit">Alterar lugar</button>
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
    
</body>
</html>