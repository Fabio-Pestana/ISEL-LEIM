<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% String html = (String) request.getAttribute("resultados"); %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Rent-a-Car</title>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Play', sans-serif;
            line-height: 1.5;
            min-height: 100vh;
            background: #676e7c;
            flex-direction: column;
            margin: 0;
        }

        main {
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
            padding: 20px;
            transition: transform 0.2s;
            width: 500px;
            text-align: center;
        }

        label {
            display: block;
            width: 100%;
            margin-top: 10px;
            margin-bottom: 5px;
            text-align: left;
            color: #555;
            font-weight: bold;
        }

        input {
            display: block;
            width: 100%;
            margin-bottom: 15px;
            padding: 10px;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        button {
            padding: 15px;
            border-radius: 10px;
            margin-top: 15px;
            margin-bottom: 15px;
            border: none;
            color: white;
            cursor: pointer;
            background-color: #0056b3;
            width: 100%;
            font-size: 16px;
            text-align: center;
        }

        button:hover {
            background-color: #003976;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
        }

        th {
            background-color: #f4f4f4;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Gerente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Ranking de Modelos - Melhor Avaliação</h1>
    </header>
    <main>
        <form id="ranking-form" method="POST" action="Gerente3Servlet">
            <label for="entries">Número de Modelos:</label>
            <input type="number" id="entries" name="entries" min="1" placeholder="Insira o número de modelos" required>
            <label for="entries">Número de Semanas:</label>
            <input type="number" id="semanas" name="semanas" min="1" placeholder="Insira o número de semanas" required>
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