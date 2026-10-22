<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page errorPage="error.jsp" %>  


<% String html = (String) request.getAttribute("resultados"); %>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <title>My Rent-a-Car</title>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Gerente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Ranking de Marcas de Menor Lucro</h1>
    </header>
    <main>
        <form id="ranking-form" method="POST" action="Gerente2Servlet">
            <label for="num-entries">Número de Entradas:</label>
            <input type="number" id="num-entries" name="num-entries" placeholder="Insira um número" min="1" required>
            <button type="submit">Gerar Ranking</button>
        </form>
        <div>
            <%= html = (html!=null) ? html: "" %>
        </div>
    </main>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
        
</body>

</html>

