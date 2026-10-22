<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Gerente"%>

<% 
String html = (String) request.getAttribute("resultados");
String extra = (String) request.getAttribute("extra");
Gerente gerente = new Gerente();
String option = gerente.listarFreguesiasWeb();


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
            <a class="button-link" href="Gerente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Ranking de Clientes</h1>
    </header>
    <main>
        <form id="ranking-form" method="POST" action="Gerente5Servlet">
            <label for="freguesia">Filtrar por Freguesia:</label>
            <select id="freguesia" name="freguesia" required>
    			<option value="" selected disabled>Selecione uma Freguesia</option>
                <%= option != null ? option : "" %> <!-- Aqui você insere as opções geradas pelo Servlet -->
            </select>
            <button type="submit">Gerar Ranking</button>
        </form>
		<div>
             <%= extra != null ? extra : "" %>
        </div>
        <div>
             <%= html != null ? html : "" %>
        </div>
    </main>
     <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>