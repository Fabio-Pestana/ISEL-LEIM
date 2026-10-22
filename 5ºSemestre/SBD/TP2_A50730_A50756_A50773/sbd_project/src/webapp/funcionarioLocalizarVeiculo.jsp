<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String html = (String) request.getAttribute("resultados");
String extra = (String) request.getAttribute("extra");
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
        <h1 class="text-play">Localizar um veículo</h1>
    </header>
    <main>
        <form id="vehicle-form" method="POST" action="Funcionario2Servlet">
            <label for="license-plate">Insira a matrícula:</label>
            <input type="text" id="license-plate" name="license-plate" placeholder="Matricula (XX-XX-XX)" required>
            <button type="submit">Localizar</button>
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