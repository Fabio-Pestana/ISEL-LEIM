<%@page import="client.Jogador"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName"%>
<%
String color = CheckName.getColor(request, response);
Jogador jogador = CheckName.getJogador(request, response);

String symbol = (String) session.getAttribute("simbolo");
String gameSt = request.getParameter("gameState");
int gameState = Integer.parseInt(gameSt);
boolean simbolo = symbol.equals("X ");
String mensagem = null;

if((gameState == 1 && simbolo) || (gameState == 2 && !simbolo)){
	mensagem = "Game Over";
}else if((gameState == 1 && !simbolo)|| (gameState == 2 && simbolo)){
	mensagem = "You Won!";
}else if(gameState == 1){
	mensagem = "Draw";
}

String colorPlayer = "#fff";
String colorOpps = "#000000";
if(symbol.equals("X")){
	colorPlayer = "#000000";
	colorOpps = "#fff";
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Game Options</title>
    <style>
        body {
            background-color: <%=color%>;
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .container h1 {
            margin-bottom: 20px;
        }
        .container button {
            background-color: #0056b3;
            border: none;
            color: white;
            padding: 10px 20px;
            margin: 10px;
            border-radius: 5px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #003d80;
        }
        .note {
            margin-top: 20px;
            color: #555;
        }
        
        footer {
		    background-color: #494d57;
		    color: #fff;
		    padding: 20px 0;
		    text-align: center;
		    position: fixed;
		    bottom: 0;
		    width: 100%;
		}
		
		.footer-text {
		    text-transform: capitalize;
		    margin: 0;
		    padding: 0;
		}
    </style>
</head>
<body>
    <div class="container">
        <h1><%=mensagem %></h1>
        <form action="MenuServlet" method="post" style="display:inline;">
            <button type="submit">Back to the menu</button>
        </form>
        <form action="RestartGameServlet" method="post" style="display:inline;">
            <button type="submit">Restart Game</button>
        </form>
        <div class="note">
            Nota: mesmo que escolha Restart Game se o outro player recusar, voltará para o menu.
        </div>
    </div>
    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos
        </div>
    </footer>
</body>
</html>
