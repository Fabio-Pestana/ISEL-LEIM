<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
String html = (String) request.getAttribute("resultados");
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
        <h1 class="text-play">Registar Intervenção</h1>
    </header>
    <main>
        <form id="intervention-form" method="POST" action="Funcionario3Servlet">
            <label for="license-plate">Matrícula:</label>
            <input type="text" id="license-plate" name="license-plate" placeholder="Matricula" required>

            <label for="datetime">Data e Hora da Intervenção:</label>
            <input type="datetime-local" id="datetime" name="datetime" required>

            <label for="cost">Custo:</label>
            <input type="number" id="cost" name="cost" placeholder="custo em euros" step="0.01" required>

            <label for="type">Tipo de Intervenção:</label>
            <select id="type" name="type" required>
                <option value="" disabled selected>Escolha uma opção</option>
                <option value="reparacao">Reparação</option>
                <option value="manutencao">Manutenção</option>
            </select>

            <div class="wrap">
                <button type="submit">Submit</button>
            </div>
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