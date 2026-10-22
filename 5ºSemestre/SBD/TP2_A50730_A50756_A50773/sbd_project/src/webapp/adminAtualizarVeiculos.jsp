<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String msg = (String) request.getAttribute("mensagem");
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
            <a class="button-link" href="Administrador.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Atualizar ficha de Veículo</h1>
    </header>
        
    <main class="veiculo">
        <form method="POST" action="Admin3Servlet">
            <label for="Matricula">Insira a Matricula do veículo que quer atualizar: </label>
            <input type="text" id="Matricula" name="Matricula" placeholder="Matricula" required>

            <label for="field">Qual campo deseja trocar? </label>
            <select id="field" name="field" required>
                <option value="" disabled selected>Escolha uma opção</option>
                <option value="Marca">Marca</option>
                <option value="Categoria">Categoria</option>
                <option value="Modelo">Modelo</option>
                <option value="Cor">Cor</option>
                <option value="NumeroLugares">Número de Lugares</option>
                <option value="NumeroPortas">Número de Portas</option>
                <option value="PotenciaMotor">Potência do Motor</option>
                <option value="TipoMotor">Tipo de Motor</option>
                <option value="Estado">Estado</option>
                <option value="RegistoKm">Registo de Km</option>
            </select>

            <label for="newValue">Insira o novo valor: </label>
            <input type="text" id="newValue" name="newValue" placeholder="Novo valor" required>

            <div class="wrap">
                <button type="submit"> Submit </button>
            </div>
            <div>
		    	<%= msg != null ? msg : "" %>
		    </div>
        </form>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>

</html>