<%@page import="client.Jogador"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName, org.w3c.dom.Document, org.w3c.dom.Element, java.util.ArrayList, org.w3c.dom.NodeList"%>
<%
String name= CheckName.login(request, response);
String color = CheckName.getColor(request, response);
Jogador jogador = CheckName.getJogador(request, response);

Element ranking = jogador.getStub().rankingWeb();
ArrayList<String> usernames = new ArrayList<String>();
ArrayList<String> profilePics = new ArrayList<String>();
ArrayList<String> flags = new ArrayList<String>();
ArrayList<String> nationalities = new ArrayList<String>();
ArrayList<String> vitorias = new ArrayList<String>();
ArrayList<String> timeInGame = new ArrayList<String>();

NodeList users = ranking.getElementsByTagName("user");
for (int i = 0; i < users.getLength(); i++) {
    Element user = (Element) users.item(i);
    
    usernames.add(user.getAttribute("username"));
    
    profilePics.add("data:image/png;base64," + user.getAttribute("profilePic"));
    
    flags.add("data:image/png;base64," + user.getAttribute("flag"));
    nationalities.add(user.getAttribute("nationality"));
    
    vitorias.add(user.getAttribute("vitorias"));
    
    String averageTime = user.getAttribute("timeAverageInGame");
    if(!averageTime.equals("0")){
    	timeInGame.add(jogador.getStub().millisConverted(Long.parseLong(averageTime)));
    }else{
    	timeInGame.add("---");
    }
    
}
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate" />
    <link rel="stylesheet" href="css/ranking.css">
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
        }
    </style>
    <title>Othello</title>
</head>
<body>
    <header>
        <h1 class="text-play">Top 5 Rankings</h1>
		<hr>
    </header>
    
     <main class="ranking-container">
        <ul class="ranking-list">
	        <% 
	        int pos = 1;
	        for(int i=0; i<usernames.size();i++) { %>
	           	<li class="ranking-item">
                    <div class="left-side">
		            	<span class="ranking-number"><%= i + 1 %></span>
		            	<img src="<%= profilePics.get(i) %>" alt="Player Photo" class="player-photo">
		            	<span class="ranking-name"><%=usernames.get(i)%></span>
	            	</div>
                    <div class="right-side">
						<span class="ranking-victories" data-time="<%= timeInGame.get(i) %>">Victories: <%= vitorias.get(i) %></span>                        
						<img src="<%= flags.get(i) %>" alt="Country Flag" class="country-flag">
                    </div>
	        	</li>
	        <% } %>
   		 </ul>
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