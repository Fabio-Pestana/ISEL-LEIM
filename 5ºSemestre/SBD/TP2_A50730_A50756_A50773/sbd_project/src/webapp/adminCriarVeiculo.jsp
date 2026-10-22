<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>
<%@page import="pt.isel.leim.sbd.Stub"%>
<% 
String msg = (String) request.getAttribute("mensagem");

Stub stub = new Stub();
String[][] resultados = Auxiliar.listarCategoriasWeb(stub);

String categorias = Auxiliar.gerarOpcoes(resultados, 0);

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
        <h1 class="text-play">Criar ficha de Veículo</h1>
    </header>

    <main class="veiculo">
        <form method="POST" action="Admin6Servlet">
        	<div>
		    	<%= msg != null ? msg : "" %>
		   	 </div>
            <label for="Matricula">Insira a Matricula: </label>
            <input type="text" id="Matricula" name="Matricula" placeholder="Matricula" required>

            <label for="Marca">Insira a Marca: </label>
            <input type="text" id="Marca" name="Marca" placeholder="Marca" required>

			<label for="Categoria">Selecione uma categoria:</label>
            <select id="Categoria" name="Categoria" required>
    			<option value="" selected disabled>Escolha uma categoria</option>
                <%= categorias != null ? categorias : "" %> 
            </select>
			
            <label for="Modelo">Insira o Modelo: </label>
            <input type="text" id="Modelo" name="Modelo" placeholder="Modelo" required>

            <label for="Cor">Insira a Cor: </label>
            <input type="text" id="Cor" name="Cor" placeholder="Cor" required>

            <label for="NumLugares">Insira o Número de Lugares: </label>
            <input type="number" id="NumLugares" name="NumLugares" placeholder="Nº de Lugares" required>

            <label for="NumPortas">Insira o Número de Portas: </label>
            <input type="number" id="NumPortas" name="NumPortas" placeholder="Nº de Portas" required>

            <label for="PotMotor">Insira a Potência do Motor: </label>
            <input type="number" id="PotMotor" name="PotMotor" placeholder="Potência do Motor" required>

			<label for="Tipo">Insira o Tipo de Motor: </label>
			<select id="TipoMotor" name="TipoMotor" required>
				<option value="" disabled selected>Escolha uma opção</option>
				<option value="gasolina">Gasolina</option>
				<option value="gasóleo">Gasóleo</option>	
				<option value="GPL">GPL</option>
				<option value="elétrico">Elétrico</option>
				<option value="híbrido">Híbrido</option>			                    
			</select>
			
			<label for="Estado">Selecione o Estado do veículo: </label>
			<select id="Estado" name="Estado" required>
				<option value="" disabled selected>Escolha uma opção</option>
				<option value="disponível">Disponível</option>
				<option value="alugado">Alugado</option>	
				<option value="manutenção">Manutenção</option>
				<option value="reservado">Reservado</option>
			</select>

            <label for="RegistoKm">Insira o Registo de Km: </label>
            <input type="number" id="RegistoKm" name="RegistoKm" step="0.01" placeholder="Registo de Km" required>

            <div class="wrap"> 
                <button type="submit"> Submit </button>
            </div>
        </form>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>

</html>