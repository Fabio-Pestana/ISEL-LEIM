<%@page import="client.Jogador"%>
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
    <link rel="stylesheet" href="css/changeColor.css">
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
        
        .action-button {
		    padding: 15px;
		    border-radius: 10px;
		    border: none;
		    color: white;
		    cursor: pointer;
		    background-color: <%=Buttons%>;
		    font-size: 16px;
   			margin: 5px;
		}
		
		
		.menu-link-container {
            text-align: center;
        }

        .menu-link-container a {
            color: <%=Buttons%>;
            text-decoration: none;
        }

        .menu-link-container a:hover {
            text-decoration: underline;
        }
    </style>
    <title>Othello</title>
</head>

<body>
    <header>
        <h1 class="text-play">Personalizar</h1>
    </header>

    <main class="color-picker-container">
    <form id="pictureForm" method="POST" action="ColorServlet">
        <div class="color-picker-section">
            <label for="color-picker">
            Escolher Cor: 
            </label>
            <input type="color" id="color-picker"  name="color-picker">
        </div>

        <div class="button-section">
            <button type="submit" id="submit-button" class="action-button">Submit</button>
            <button id="reset-button" class="action-button">Reset</button>
        </div>
        </form>
        <div class="menu-link-container">
                <div>Voltar para o 
                    <a href="menu.jsp" class="account-link">Menu</a>
                </div>
            </div>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & João Ramos & Miguel Alcobia
        </div>
    </footer>

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
	    
        document.addEventListener('DOMContentLoaded', function () {
            const resetButton = document.getElementById('reset-button');
            const submitButton = document.getElementById('submit-button');
            const colorPicker = document.getElementById('color-picker');
            const originalBackgroundColor = "<%=color%>";

            colorPicker.addEventListener('input', function () {
                document.body.style.backgroundColor = colorPicker.value;
                resetButton.style.backgroundColor = colorPicker.value;
                submitButton.style.backgroundColor = colorPicker.value;
            });

            resetButton.addEventListener('click', function () {
                document.body.style.backgroundColor = originalBackgroundColor;
                resetButton.style.backgroundColor = originalBackgroundColor;
                submitButton.style.backgroundColor = originalBackgroundColor;
            });
        });
    </script>
</body>

</html>
