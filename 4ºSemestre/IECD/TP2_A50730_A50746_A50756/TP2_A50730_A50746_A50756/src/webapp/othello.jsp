<%@page import="client.Jogador"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName, org.w3c.dom.Document, org.w3c.dom.Element, java.time.LocalDateTime, java.time.Duration, org.w3c.dom.NodeList"%>
<%
String name = CheckName.login(request, response);
String color = CheckName.getColor(request, response);
Jogador jogador = CheckName.getJogador(request, response);
boolean gameEnded = false;

String symbol = (String) session.getAttribute("simbolo");
if (symbol == null) {
	symbol = jogador.getStub().iniciarWeb(name);
    session.setAttribute("simbolo", symbol);
}

Element tab = jogador.getStub().obterWeb(symbol);;
String tabuleiroHTML = tabuleiroToHTML(tab);
String estado=tab.getAttribute("estado");

if(!estado.equals("ND")) {
	if(!estado.equals("IV") && !estado.equals("SJ")) {
		int gameState = jogador.getStub().estadoFinal(estado);
		gameEnded = true;
	}
}

String colorPlayer = "#fff";
String colorOpps = "#000000";
if(symbol.equals("X ")){
	colorPlayer = "#000000";
	colorOpps = "#fff";
}

%>
<%!
String HtmlBase64 = "data:image/png;base64,";
String srcBlack = HtmlBase64 + "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAKzSURBVHhe7d1bTutAEADRwFJYDxJrRWI9bAWIFCtW4igPMv2YqvMDn7in3Pa9CHh5+3z/2Qnr9fBRUAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHCYnwv4/vg6fHabv7kcPpvblAHce9i3mjGKaQIYdeiXzBJD6wCiD/2SzjG0DKDKwZ/qGEKrAKoe/KlOIbQIoMvBn+oQQvn/B+h6+HsdvvayG6DzwW+pug1KboDZDn+v6jWVC2DGw19UvLZSAcx8+Itq11jiHYBw8FsqvBf43UC49ACod/9ehWtPDYB8+IvsGaQF4OEfZc4iJQAP/1zWTMID8PAvy5hN+kugcoUG4N1/XfSM3ABwYQF4998uclZuALiQALz77xc1MzdAYRERDA/Au782NwDc0AC8+/9v9AzdAHAGADcsANf/84ycpRsAzgDgDADOAOCGBOAL4PONmqkbAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDABuSAAdfkNmN6Nm6gaAMwA4A4AzALhhAfgi+DwjZ+kGgDMAuKEB+Bj4v9EzdAPAGQDc8AB8DDwuYnYhG8AI7hc1Mx8BcGEBuAVuFzkrNwBcaABugeuiZ+QGgAsPwC1wWcZsUjaAEZzLmknaI8AIjjJnkfoOYAT5M0h/CSRHUOHa0wNQrhJ/O3hB+d1ClbZeqQ1AeBxUu8Zyj4CZI6h4bSXfAWaMoOo1lXoH2NL9vaB6zOX/FdB5G3T42stvgLUu26BTtK0CWFQNoeO2ahnAokoIHQ9+0TqAtegYOh/62jQBrI2KYZZDX5sygC2PRDHjgZ/CBKBtfjcQzgDgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAbbf7BeCTvCjJxvl7AAAAAElFTkSuQmCC";
String srcWhite = HtmlBase64 + "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAKzSURBVHhe7d1bTutAEADRwFJgO8BegfWwFlCkWFjBUR5k+jFV5wc+cU+57XsR8PD0/vK9E9bj4aOgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4DA/F/D19nn47DLPH6+Hz+Y2ZQDXHvalZoximgBGHfops8TQOoDoQz+lcwwtA6hy8Mc6htAqgKoHf6xTCC0C6HLwxzqEUP7/Aboe/l6Hr73sBuh88FuqboOSG2C2w9+rek3lApjx8BcVr61UADMf/qLaNZZ4ByAc/JYK7wV+NxAuPQDq3b9X4dpTAyAf/iJ7BmkBePi/MmeREoCH/1fWTMID8PBPy5hN+kugcoUG4N1/XvSM3ABwYQF4918uclZuALiQALz7rxc1MzdAYRERDA/Au782NwDc0AC8+/9v9AzdAHAGADcsANf//YycpRsAzgDgDADOAOCGBOAL4P2NmqkbAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDABuSAAdf2dudaNm6gaAMwA4A4AzALhhAfgieD8jZ+kGgDMAuKEB+Bj4v9EzdAPAGQDc8AB8DNwuYnYhG8AIrhc1Mx8BcGEBuAUuFzkrNwBcaABugfOiZ+QGgAsPwC1wWsZsUjaAEfyVNZO0R4AR/MqcReo7gBHkzyD9JZAcQYVrTw9AuUr87eAF5XcLVdp6pTYA4XFQ7RrLPQJmjqDitZV8B5gxgqrXVOodYEv394LqMZf/V0DnbdDhay+/Ada6bINO0bYKYFE1hI7bqmUAiyohdH5MtQ5gLTqGzoe+Nk0Aa6NimOXQ16YMYMstUcx44McwAWib3w2EMwA4A4AzADgDgDMAOAOAMwA4A4AzADgDgDMAOAOAMwA4A4AzADgDgDMAOANA2+1+AB5IvCgVash+AAAAAElFTkSuQmCC";
String srcPossible = HtmlBase64 + "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAK4SURBVHhe7d3LTetQFEDRQCk0wIguaIVqqIQ6GFERKFIsrOAoH3LP5+69JjDE524f+z0EPDx9vH7vhPV4+CgoA4AzADgDgDMAOAOAMwA4A4AzADgDgDMAOAOAMwA4A4AzADgDgDMAOAOAMwA4A4AzADgDgDMAOMzPBby/fB0+u8zb5/Phs7lNGcC1h32pGaOYJoBRh37KLDG0DiD60E/pHEPLAKoc/LGOIbQKoOrBH+sUQosAuhz8sQ4hlP9/gK6Hv9fhay+7ATof/Jaq26DkBpjt8PeqXlO5AGY8/EXFaysVwMyHv6h2jSXeAQgHv6XCe4HfDYRLD4B69+9VuPbUAMiHv8ieQVoAHv6vzFmkBODh/5U1k/AAPPzTMmaT/hKoXKEBePefFz0jNwBcWADe/ZeLnJUbAC4kAO/+60XNzA1QWEQEwwPw7q/NDQA3NADv/v8bPUM3AJwBwA0LwPV/PyNn6QaAMwA4A4AzALghAfgCeH+jZuoGgDMAOAOAMwA4A4AzADgDgDMAOAOAMwA4A4AbEkDH35lb3aiZugHgDADOAOAMAG5YAL4I3s/IWboB4AwAbmgAPgb+b/QM3QBwBgA3PAAfA7eLmF3IBjCC60XNzEcAXFgAboHLRc7KDQAXGoBb4LzoGbkB4MIDcAucljGblA1gBH9lzSTtEWAEvzJnkfoOYAT5M0h/CSRHUOHa0wNQrhJ/O3hB+d1ClbZeqQ1AeBxUu8Zyj4CZI6h4bSXfAWaMoOo1lXoH2NL9vaB6zOX/FdB5G3T42stvgLUu26BTtK0CWFQNoeO2ahnAokoInR9TrQNYi46h86GvTRPA2qgYZjn0tSkD2HJLFDMe+DFMANrmdwPhDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDgDADOAOAMAM4A4AwAzgDQdrsfqH28KO+E/IEAAAAASUVORK5CYII=";
String srcEmpty = HtmlBase64 + "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAJqSURBVHhe7dxbbsIwFABRyFK6nm62KwTHCUS0zsNA6nBnjqD9aYWEx49EiPPXz/flJKxu/C0oA4Drzv1PNwGsLo99ruAv44hvcQtYikMxTAE405GmAJzpSF4FwBUDcDHgeAxgHPmL5wGMxwBuA+8SgFE+AxgARjkAtwCMcgC/uSKEtS0AV4SwtgWgsAwAzgDgDADOAOAMAG4IwMs8rCEAb/RguQXAGQCcAcBtD8BzQkhTAGtXAl4phDQF4AxH8gwAVxeA20A4dQG4TYTjFgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcOUAvOePUQ7Ae/4YbgFwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBXDsDPA2CUA/DzABhuAXAGAGcAcAYAZwBwBgBnAHDzAXgzCMEVAG4+AO8GIhgAnGcAOM8AcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcAYAZwBwBgBnAHAGAGcAcDsG0H/PXHoODx3UjgH03zOXnsNDB/W2APIsd6Q/zosBjMt8ksfetf7jvBhAP+yP094GPktlAOOMX1jq13aBHMjaH+nfVAYwzvgXpnkee5eJw5gPwFmKMB9AmqW3ieqEjWtxC7gtAi4GcVWeARRNkwDylrJ1WbmfGtPTpejtmgRwH9M15/RH91rSc8v/qMpBt4BxpC9O+b0dNICagW+1LKTXDdDnMQJ4+o3sB7/VKKTXDbAltQ+gfxOffiMDTMHG2gfgGDZ0Ol0BRaY1HhENomoAAAAASUVORK5CYII=";

// Mostra o tabuleiro atual.
public String tabuleiroToHTML(final Element tabuleiro) {
		StringBuilder out = new StringBuilder();
	    out.append("<table class='othello-board'>\n");
	    NodeList casas = tabuleiro.getElementsByTagName("casa");
	    int k = 0;
	    int BOARD_SIZE = 9;

	    for (int row = 1; row < BOARD_SIZE; row++) {
	        out.append("<tr>\n");
	        for (int col = 1; col < BOARD_SIZE; col++) {
	            String simbolo = ((Element) casas.item(k++)).getAttribute("simbolo");
	            String imgSrc = srcEmpty;
	            String jogada = " onclick='notPossible()'";
	            
	            if ("X ".equals(simbolo)) {
	                imgSrc = srcBlack;
	            } else if ("O ".equals(simbolo)) {
	                imgSrc = srcWhite;
	            }else if("p ".equals(simbolo)) {
	            	imgSrc = srcPossible;
	            	jogada = " onclick='jogada(" + row + ", " + col +")'";
	            }

	            out.append("<td data-row='").append(row).append("' data-col='").append(col).append("'")
	               .append(jogada).append(">")
	               .append("<img src='").append(imgSrc).append("' width='48' height='48' />")
	               .append("</td>\n");
	        }
	        out.append("</tr>\n");
	    }
	    out.append("</table>\n");

	    return out.toString();
}

public String mensagem(Jogador jogador, String estado){
	return jogador.getStub().estadoToTXT(estado);
}
%>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/othello.css">
    <link href="https://fonts.googleapis.com/css?family=Girassol" rel="stylesheet">
    <title>Othello</title>
    <script>
	    let startTime;
	
	    window.onload = function() {
	        startTime = Date.now();
	        checkStateAndReload();
	    }
	    
	    var click = false;
	    function jogada(row, col) {
	        if (!click) {
	        	const estado = "<%= estado %>";
	            if (estado !== "SJ") {
		            document.getElementById('msg').innerHTML = "<h2>Espera pelo oponente...</h2>";
		            document.title = 'Espera...';
		            document.getElementById('linha').value = row;
		            document.getElementById('coluna').value = col;
		            click = true;
		            submitForm();
		            document.getElementById('Jogar').submit();
	            }
	        }
	    }
        
        function submitForm() {
            const endTime = Date.now();
            const timeElapsed = endTime - startTime;

            document.getElementById('timeOfPlay').value = timeElapsed;
        }
	
	    function notPossible() {
	        alert("Movimento não permitido!");
	    }
	    
	    function checkStateAndReload() {
            const estado = "<%= estado %>";
            if (estado === "SJ") {
                setTimeout(function() {
                    location.reload();
                }, 500);
            }
        }
    </script>
    
    <style>
    body {
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    font-family: 'Girassol', sans-serif;
	    line-height: 1.5;
	    min-height: 100vh;
	    background: <%=color%>;
	    flex-direction: column;
	    margin: 0;
	}
	
	.othello-board {
	    border-collapse: collapse;
	    margin: 20px;
	    background-color: black;
	}
	
	.othello-board td {
	    width: 50px;
	    height: 50px;
	    text-align: center;
	    vertical-align: middle;
	    border: 1px solid black; 
	    background-color: green;
	    padding: 0;
	}
	
	.othello-board img {
	    width: 48px;
	    height: 48px;
	    display: block;
	    margin: auto;
	}
	
	.player1-info, .player2-info {
	    width: 100px;
	    height: 50px;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    margin: 0 20px;
	    text-align: center;
	    border-radius: 8px; /* Add border-radius for rounded corners */
	}
	
	.player1-info {
	    background-color: <%=colorPlayer%>;
	    color: <%=colorOpps%>;
	}
	
	.player2-info {
	    background-color: <%=colorOpps%>;
	    color: <%=colorPlayer%>;
	}
    </style>
</head>
<body>
    <header>
        <h1>Othello</h1>
    </header>

    <main class="game-container">
        <div class="player-info-container">
            <div class="player1-info" id="player1">Player 1 <%=name%></div>
        </div>
        <div id="othello-board-container">
            <%= tabuleiroHTML %>
        </div>
        <div class="player-info-container">
            <div class="player2-info" id="player2">Player 2 Opponent</div>
        </div>
        <form id="Jogar" method="POST" action="OthelloServlet">  
		    <input type="hidden" name="linha" id="linha">
		    <input type="hidden" name="coluna" id="coluna">
		    <input type="hidden" name="timeOfPlay" id="timeOfPlay">
		    <input type="hidden" name="state" id="state">
		</form>
		
		<form id="gameEnded" method="POST" action="FinishGameServlet">  
		    <input type="hidden" name="gameState" id="gameState">
		</form>
    </main>
    
    <div id="msg">
    	<%= mensagem(jogador, estado)%>
	</div>

    <div class="timer" id="timer">30</div>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos
        </div>
    </footer>
    <%
	    if(gameEnded) {
			int gameState = jogador.getStub().estadoFinal(estado);
			%>
			<script>
		    	document.getElementById('gameState').value = "<%=gameState%>";
		    	document.getElementById('gameEnded').submit();
		    </script>
	    	<%
		}
    %>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            	let timerElement = document.getElementById('timer');
            	let timeLeft = 30;

            	function updateTimer() {
                	timerElement.textContent = timeLeft;
                	if (timeLeft === 0) {
                    	alert("Tempo esgotado!");
                    	clearInterval(timerInterval);
                	}
                	timeLeft--;
            	}
            	let timerInterval = setInterval(updateTimer, 1000);
        });
    </script>
</body>
</html>