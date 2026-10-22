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
        <h1 class="text-play">Importar Dados de Veículos</h1>
    </header>

    <main class="cliente">
        <h2>Importar Dados</h2>
        <form id="importForm" method="POST" action="Admin8Servlet" onsubmit="return handleFormSubmit(event)">
            <label for="importFile">Selecione o ficheiro (JSON):</label>
            <input type="file" id="importFile" name="importFile" accept=".json" required>

            <div class="wrap">
                <button type="submit">Importar</button>
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

    <script>
        function handleFormSubmit(event) {
            event.preventDefault(); 

            const fileInput = document.getElementById('importFile');
            const file = fileInput.files[0];

            if (file) {
                const reader = new FileReader();
                
                reader.onload = function(e) {
                    const fileContent = e.target.result;

                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'jsonData';
                    hiddenInput.value = fileContent;

                    document.getElementById('importForm').appendChild(hiddenInput);

                    document.getElementById('importForm').submit();
                };

                reader.readAsText(file);
            }
        }
    </script>

</body>

</html>

