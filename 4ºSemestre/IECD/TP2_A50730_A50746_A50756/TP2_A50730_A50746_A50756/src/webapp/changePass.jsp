<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName"%>
<%
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
    <link rel="stylesheet" href="css/login.css">
    <script src="js/ColorChange.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
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
        }
        
        button {
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
		}
    </style>
    <title>Othello</title>
    <script>
	    <%
	    if(!color.equals("#676e7c")){%>
		    window.onload = function() {
		            let buttons = document.querySelectorAll('button');
		            buttons.forEach(function(button) {
		                applyButtonColor(button, "<%=color%>");
		            });
		        };
	    <% } %>
    
        function validatePasswords() {
            var newPassword = document.getElementById('new-password').value;
            var confirmPassword = document.getElementById('confirm-password').value;
            if (newPassword !== confirmPassword) {
                alert('As novas senhas não coincidem. Por favor, tente novamente.');
                return false;
            }
            return true;
        }
    </script>
</head>

<body>
    <header>
        <h1 class="text-play">Alterar Senha</h1>
    </header>

    <main class="login">
        <form method="POST" action="PassServlet" onsubmit="return validatePasswords()">
            <label for="old-password">Senha Antiga:</label>
            <input type="password" id="old-password" name="old-password" placeholder="Digite sua senha antiga" required>

            <label for="new-password">Nova Senha:</label>
            <input type="password" id="new-password" name="new-password" placeholder="Digite sua nova senha" required>

            <label for="confirm-password">Confirmar Nova Senha:</label>
            <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirme sua nova senha" required>

            <div class="wrap">
                <button type="submit">Alterar Senha</button>
                <button type="reset">Reset</button>
            </div>
        </form>

        <div>Voltar para o
            <a href="menu.jsp" class="account-link">Menu</a>
        </div>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos
        </div>
    </footer>
</body>

</html>