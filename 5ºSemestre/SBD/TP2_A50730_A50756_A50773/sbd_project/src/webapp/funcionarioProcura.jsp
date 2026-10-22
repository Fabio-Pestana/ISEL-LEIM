<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
String html = (String) request.getAttribute("resultados");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
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
        <h1 class="text-play">Procurar Ficha Cliente</h1>
    </header>
    <main>
        <form id="search-client-form" method="POST" action="Funcionario4Servlet">
            <label for="client-search">Nome do Cliente:</label>
           	<input autocomplete="off" name="client-search" type="text" list="itens" id="search-input" onkeyup="showState(this.value);" placeholder="Digite o nome do cliente que quer procurar...">
            <button type="submit">Procurar</button>
        </form>
        <datalist id="itens"> 
		<option>Loading... </option>
		</datalist>
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
<script>
	function getXHR() {
		var invocation = null;
		try {
			// Opera 8.0+, Firefox, Chrome, Safari
			invocation = new XMLHttpRequest();
		} catch (e) {
			// Internet Explorer Browsers
			try {
				invocation = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {

				try {
					invocation = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
					alert("Something went wrong!");
					return null;
				}
			}
		}
		return invocation;
	}
 
		var xmlHttp = getXHR();
		
		function showState(str) {
			if (event.key === 'Enter') {
				document.getElementById("search_input").value = document
						.getElementById("itens").options[0].value;
				return;
			}
			var url="FuncionarioAutocomplete";
			url += "?query=" +str+"&tag=option&nlt=0&nit=10";
			url = encodeURI(url);
			xmlHttp.onreadystatechange = function() {
				if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
					document.getElementById("itens").innerHTML=xmlHttp.responseText;
			}
			xmlHttp.open("GET", url, true);
			xmlHttp.send(null);
		}
	</script>
</html>