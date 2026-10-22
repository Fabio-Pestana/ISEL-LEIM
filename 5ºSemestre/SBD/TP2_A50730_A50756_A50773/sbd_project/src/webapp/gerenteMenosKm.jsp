<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
String html = (String) request.getAttribute("resultados");
%> 

<!DOCTYPE html>
<html lang="en">
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
        <h1 class="text-play">Ranking de Veículos que Percorreram Menos Quilómetros</h1>
    </header>
    <main>
        <form id="ranking-form" method="POST" action="Gerente4Servlet">
            <label for="entries">Número de entradas no ranking:</label>
            <input type="number" id="entries" name="entries" min="1" placeholder="Insira o número de veiculos" required>
            <button type="submit">Gerar Ranking</button>
        </form>
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