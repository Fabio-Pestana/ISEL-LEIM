<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
String msg = (String) request.getAttribute("resultados");
%>
<!DOCTYPE html>
<html>

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
            <a class="button-link" href="My_Rent_a_Car.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Login Condutor</h1>
    </header>

    <main>
        <form id="login-form" method="POST" action="CondutorLogServlet">
            <label for="NIF_search">Insira o seu NIF:</label>
            <input type="text" id="NIF_search" name="NIF_search" placeholder="NIF" required>
            <label for="CC_search">Insira o seu número da carta de condução:</label>
            <input type="text" id="CC_search" name="CC_search" placeholder="Carta de Condução" required>
            <button type="submit">Entrar</button>
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