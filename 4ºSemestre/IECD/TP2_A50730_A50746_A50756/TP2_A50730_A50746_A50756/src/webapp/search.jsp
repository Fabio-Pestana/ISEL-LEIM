<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName"%>
<%
String name = CheckName.login(request, response);
String color = CheckName.getColor(request, response);
String Buttons = color;
if(color.equals("#676e7c")){
	Buttons = "#0056b3";
}
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/search.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.8.1/font/bootstrap-icons.min.css" rel="stylesheet">
    <title>Othello</title>
</head>
<style>
	body {
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    font-family: 'Play', sans-serif;
	    line-height: 1.5;
	    min-height: 100vh;
	    background-color: <%=color%>;
	    flex-direction: column;
	    margin: 0;
	}
	
	header {
	    text-align: center;
	    color: #fff;
	    margin-top: 20px;
	    margin-bottom: 50px;
	}
	
	#search-button:hover {
    	background-color: <%=Buttons%>;
	}
	
	#search-button {
    	background-color: <%=color%>;
    	border: none;
    	color: white;
    	padding: 10px 15px;
    	cursor: pointer;
    	border-radius: 50%;
    	display: flex;
    	align-items: center;
    	justify-content: center;
	}
	
	.menu-link-container {
    	text-align: center;
    	margin-top: 20px;
	}

	.menu-link-container a {
    	color: <%=Buttons%>;
    	text-decoration: none;
	}

	.menu-link-container a:hover {
    	text-decoration: underline;
	}
	
</style>
<body>
    <header>
        <h1 class="text-play">Pesquisar por Jogadores</h1>
    </header>

    <main class="search-container">
        <form class="search-bar" method="GET" action="perfil_Search.jsp">
            <input autocomplete="off" name="username" type="text" list="itens" id="search-input" onkeyup="showState(this.value);" placeholder="Digite o nome do jogador que quer procurar...">
            <button type="submit" id="search-button">
                <i class="bi bi-search"></i>
            </button>
        </form>
        <datalist id="itens"> 
		<option>Loading... </option>
		</datalist>
		    
    	<div class="menu-link-container">
        	<div>Voltar para o 
            	<a href="menu.jsp" class="account-link">Menu</a>
        	</div>
    	</div>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos
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
					// Something went wrong
					alert("Your browser broke!");
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
			var url="GetHTML";
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
	<script>
    <%
    if(!color.equals("#676e7c")){%>
	    window.onload = function() {
	            let buttons = document.querySelectorAll('.button-link');
	            buttons.forEach(function(button) {
	                applyButtonColor(button, "<%=color%>");
	            });
	        };
    <% } %>

    </script>

</html>