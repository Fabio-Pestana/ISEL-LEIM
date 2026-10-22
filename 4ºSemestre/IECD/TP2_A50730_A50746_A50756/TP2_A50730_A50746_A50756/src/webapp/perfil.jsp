<%@page import="client.Jogador"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName, org.w3c.dom.Document, org.w3c.dom.Element"%>
<%
String name= CheckName.login(request, response);
String color = CheckName.getColor(request, response);

Jogador jogador = CheckName.getJogador(request, response);
Element perfil = jogador.getStub().seePerfilWeb(name);

String nomeJogador = perfil.getAttribute("username");
String birthdate = perfil.getAttribute("birthdate");
String nationality = perfil.getAttribute("nationality");
String flag = "data:image/png;base64," + perfil.getAttribute("flag");
String age = perfil.getAttribute("age");
String vitorias = perfil.getAttribute("vitorias");
String derrotas = perfil.getAttribute("derrotas");
String jogos = perfil.getAttribute("jogos");
String time = perfil.getAttribute("tempo");
String averageTime = perfil.getAttribute("timeAverageInGame");
String tempo = jogador.getStub().millisConverted(Long.parseLong(time));
String tempoMedio = jogador.getStub().millisConverted(Long.parseLong(averageTime));
String photo = "data:image/png;base64," + perfil.getAttribute("profilePic");
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate" />
    <link rel="stylesheet" href="css/profile.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.8.1/font/bootstrap-icons.min.css">
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
    </style>
    
    <title>Othello</title>
</head>
<body>
    <header>
        <h1 class="text-play">Perfil do Jogador</h1>
		<hr>
    </header>
    
    <main class="profile">
        <div class="profile-photo">
            <img alt="Foto de Perfil" id="profile-img" src="<%=photo%>">
        </div>
        <div class="profile-details">
            <p><strong>Nome do Utilizador:</strong> <span id="username"><%=nomeJogador%></span></p>
            <p><strong>Data de Nascimento:</strong> <span id="birthdate"><%=birthdate%></span></p>
            <p><strong>Nacionalidade:</strong>
                <span class="nationality-container">
                    <span id="nationality"><%=nationality%></span>
                    <img alt="Flag" id="flag_photo" src="<%=flag%>">
                </span>
            </p>
            <p><strong>Idade:</strong> <span id="age"><%=age%></span></p>
            <p><strong>Número de Vitórias:</strong> <span id="wins"><%=vitorias%></span></p>
            <p><strong>Número de Derrotas:</strong> <span id="losses"><%=derrotas%></span></p>
            <p><strong>Número de Jogos:</strong> <span id="games"><%=jogos%></span></p>
            <% if (!time.equals("0")) { %>
                <p><strong>Tempo Total em Jogos:</strong> <span id="total-time"><%=tempo%></span></p>
                <p><strong>Tempo Médio por Jogo:</strong> <span id="average-time"><%=tempoMedio%></span></p>
            <% } %>
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
</html>
