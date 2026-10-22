<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
String morada = (String) request.getAttribute("morada");
String pisos = (String) request.getAttribute("pisos");
String filas = (String) request.getAttribute("filas");
String numeros = (String) request.getAttribute("numeros");
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/double_info_page.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css" />
    <style>
    	main {
            max-height: 650px;
        }
        .form-section {
            margin: 20px 0;
        }

        .form-section label {
            display: block;
            margin: 10px 0 5px;
        }

        .form-section select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        
        .form-section textarea {
		    width: 100%;
		    height: 50px;
		    padding: 8px;
		    margin-bottom: 10px;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    resize: none; 
		}
    </style>
    
    <title>My Rent-a-Car</title>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="condutorEntregarVeiculo.jsp">
                Voltar
            </a>
        </div>
        <h1 class="text-play">Entregar Veículo</h1>
    </header>
        <main>
            <h2>Indique onde estacionou o veículo</h2>
            <form method="POST" action="Condutor3Servlet">
                <input type="hidden" name="morada" value="<%= morada %>">
            <div class="form-section">
            	<label for="floor">Selecione um Piso:</label>
		        <select id="floor" name="floor" required>
		    		<option value="" selected disabled>Selecione um Piso</option>
		            <%= pisos != null ? pisos : "" %> 
		        </select>
       		</div>
       		<div class="form-section">
                <label for="row">Selecione uma Fila:</label>
		        <select id="row" name="row" required>
		    		<option value="" selected disabled>Selecione uma Fila</option>
		            <%= filas != null ? filas : "" %> 
		        </select>
		   </div>
		   
		   <div class="form-section">    
        		<label for="spot">Selecione um Número:</label>
		        <select id="spot" name="spot" required>
		    		<option value="" selected disabled>Selecione um Número</option>
		            <%= numeros != null ? numeros : "" %> 
		        </select>
		   </div>   
           <div class="form-section">        
                <label for="rating">Nota ao serviço (opcional):</label>
                <select id="rating" name="rating">
                	<option value="">Nenhuma</option>
                    <option value="1">Não vou voltar</option>
                    <option value="2">Gostei</option>
                    <option value="3">Adorei</option>
                </select>
            </div>   
        	<div class="form-section">
                <label for="comment">Comentário (opcional):</label>
                <textarea id="comment" name="comment" maxlength="500" placeholder="Deixe seu comentário aqui..."></textarea>
			</div>
            <button type="submit">Entregar Veículo</button>
            </form>
        </main>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>

</html>