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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate" />
    <link rel="stylesheet" href="css/menu.css">
    <script src="js/ColorChange.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.8.1/font/bootstrap-icons.min.css">
    <style>
        body {
            background-color: <%=color%>;
            display: flex;
			align-items: center;
			justify-content: center;
		    font-family: 'Play', sans-serif;
			line-height: 1.5;
			min-height: 100vh;
			flex-direction: column;
			margin: 0;
			position: relative;
        }
        
        .button-link {
			padding: 15px;
			border-radius: 10px;
			margin-top: 15px;
			margin-bottom: 15px;
			border: none;
			color: white;
			cursor: pointer;
			background-color: <%=Buttons%>;
			width: 100%;
			font-size: 16px;
			text-align: center;
			display: inline-block;
			text-decoration: none;
		}
		.logout-button {
    		padding: 10px 20px;
    		border-radius: 5px;
    		border: none;
    		color: <%=Buttons%>;
    		background-color: white;
    		cursor: pointer;
    		font-size: 16px;
    		text-align: center;
    		display: inline-block;
    		text-decoration: none;
    		position: fixed;
    		bottom: 70px;
    		right: 20px;
    		width: 100px;
		}
		
		.logout-button:hover {
            background-color: #afb0b3;
        }
    </style>
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
    <title>Othello</title>
</head>
<body>
    <header>
        <h1 class="text-play">Hello, <%=name%> welcome to the Menu!</h1>
        <hr>
    </header>
    
<main class="menu">
    <div class="wrap">
        <a class="button-link" href="perfil.jsp"> Ver Perfil 
            <i class="bi bi-person-circle" style="float: right"></i>
        </a>
    </div>
    
    <div class="wrap">
        <a class="button-link" href="search.jsp"> Procurar Jogadores
            <i class="bi bi-search" style="float: right"></i>
        </a>
    </div>

    <div class="wrap">
        <a class="button-link" href="changePass.jsp"> Alterar Senha 
            <i class="bi bi-lock" style="float: right"></i>
        </a>
    </div>

    <div class="wrap">
        <a class="button-link" href="changePic.jsp"> Alterar Foto 
            <i class="bi bi-file-image" style="float: right"></i>
        </a>
    </div>
    
    <div class="wrap">
        <a class="button-link" href="changeColor.jsp"> Alterar Cor do Fundo 
            <i class="bi bi-droplet-half" style="float: right"></i>
        </a>
    </div>
    
    <div class="wrap">
        <a class="button-link" href="ranking.jsp"> Ver Rankings 
            <i class="bi bi-trophy" style="float: right"></i>
        </a>
    </div>

    <div class="wrap">
        <a class="button-link" href="othello.jsp"> Jogar Othello 
            <i class="bi bi-controller" style="float: right"></i>
        </a>
    </div>
</main>

<footer>
    <div class="footer-text">
        &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos
    </div>
</footer>

<a href="LogoutServlet" class="logout-button" id="logoutbutton" href="">Log Out
	<i class="bi bi-box-arrow-left" style="float: right"></i>
</a>

</body>
</html>
