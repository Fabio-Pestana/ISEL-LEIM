<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
            <a class="button-link" href="Administrador.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Exportar Dados de Veículos</h1>
    </header>
    
    
    <main>
         <h2>Exportar Dados</h2>
            <form id="exportForm">
                <label for="Matricula">Insira a Matrícula:</label>
                <input type="text" id="Matricula" name="Matricula" placeholder="Matrícula" required>

                <label for="startDate">Data de Início:</label>
                <input type="datetime-local" id="startDate" name="startDate" required>

                <label for="endDate">Data de Fim:</label>
                <input type="datetime-local" id="endDate" name="endDate" required>

                <div class="wrap">
                    <button type="submit">Exportar</button>
                </div>
            </form>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>

    <script>
        document.getElementById('exportForm').addEventListener('submit', function (event) {
            event.preventDefault(); 

            const matricula = document.getElementById('Matricula').value;
            let startDate = document.getElementById('startDate').value;
            let endDate = document.getElementById('endDate').value;
            
            startDate = startDate.replace('T', ' ');
            endDate = endDate.replace('T', ' ');

            var xhr = new XMLHttpRequest();
            var url = "Admin7Servlet"; 
            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

            var data = "Matricula=" + encodeURIComponent(matricula) + 
                       "&startDate=" + encodeURIComponent(startDate) + 
                       "&endDate=" + encodeURIComponent(endDate);
            
            xhr.onload = function () {
	        if (xhr.status === 200) {
	            const blob = new Blob([xhr.responseText], { type: 'application/json' });
	            const link = document.createElement('a');
	            link.href = URL.createObjectURL(blob);
	            link.download = 'dados_veiculo.json';  
	            link.click();  
	        } else {
	            alert('Erro ao exportar os dados.');
	        }
	    };
	
	    xhr.send(data); 
        });
        
        var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth() + 1; 
		var yyyy = today.getFullYear();
			
		if (dd < 10) {
			dd = '0' + dd;
		}
			
		if (mm < 10) {
			mm = '0' + mm;
		} 
			        
		today = yyyy + '-' + mm + '-' + dd +"T23:59";
		document.getElementById("startDate").setAttribute("max", today);
		document.getElementById("endDate").setAttribute("max", today);
    </script>
</body>

</html>