<%@page import="client.Jogador"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page errorPage="error.jsp" %>  
<%@page import="validate.CheckName, org.w3c.dom.Document, org.w3c.dom.Element"%>
<%
String name = CheckName.login(request, response);
String color = CheckName.getColor(request, response);
String Buttons = color;
if(color.equals("#676e7c")){
	Buttons = "#0056b3";
}

Jogador jogador = CheckName.getJogador(request, response);
Element perfil = jogador.getStub().seePerfilWeb(name);
String photo = "data:image/png;base64," + perfil.getAttribute("profilePic");
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/picture.css">
    <script src="js/ColorChange.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <title>Othello</title>
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
        
        .picture-container {
            display: flex;
            align-items: flex-start;
            justify-content: center;
            width: 90%;
            max-width: 600px;
        }

        .profile-photo {
            flex: 1;
            padding-right: 20px;
            max-width: 200px;
        }

        .profile-photo img {
            width: 100%;
            border-radius: 10px;
            border: 3px solid <%=Buttons%>;
            margin-bottom: 20px;
        }

        .upload-section {
            flex: 1;
            text-align: center;
            max-width: 200px;
            margin-top: 40px;
        }

        .custom-file-upload {
            padding: 10px;
            border-radius: 10px;
            border: 2px solid <%=Buttons%>;
            background-color: <%=Buttons%>;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
            margin-bottom: 10px;
            display: block;
            width: 100%;
        }

        .submit-button {
            padding: 10px;
            border-radius: 10px;
            border: none;
            color: white;
            cursor: pointer;
            background-color: <%=Buttons%>;
            font-size: 16px;
            width: 100%;
            margin-bottom: 10px;
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
</head>

<body>
    <header>
        <h1 class="text-play">Alterar Foto</h1>
    </header>

    <main class="picture-container">
        <div class="profile-photo">
            <img alt="Foto de Perfil" id="profile-img" src="<%=photo%>">
        </div>
        
        <div class="upload-section">
            <form id="pictureForm" method="POST" action="PictureServlet" onsubmit="prepareImage(event)">
                <label for="file-upload" class="custom-file-upload">
                    Escolher Ficheiro
                </label>
                <input type="file" id="file-upload" name="picture" accept="image/jpeg">
                        
                <input type="hidden" id="pictureBase64" name="pictureBase64">
                
                <button type="submit" class="submit-button">Submit</button>
            </form>
            
            <div class="menu-link-container">
                <div>Voltar para o 
                    <a href="menu.jsp" class="account-link">Menu</a>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana & Miguel Alcobia & João Ramos 
        </div>
    </footer>
    
    <script>
	    <%
	    if(!color.equals("#676e7c")){%>
		    window.onload = function() {
		            const buttonFile = document.getElementsByClassName("custom-file-upload")[0];
	                applyButtonColor(buttonFile, "<%=color%>");
	                const button = document.getElementsByClassName('submit-button')[0];
	                applyButtonColor(button, "<%=color%>");
		        };
	    <% } %>

    
		function prepareImage(event) {
		            event.preventDefault();
		
		            let fileInput = document.getElementById('file-upload');
					let file = fileInput.files[0];
		
		            if (file) {
						let reader = new FileReader();
		                reader.onload = function () {
							let imageData = reader.result;
		
		                    // Remove o prefixo "data:image/jpeg;base64,"
							let base64Data = imageData.substring(imageData.indexOf(',') + 1);
		
		                    document.getElementById('pictureBase64').value = base64Data;
		                    document.getElementById('pictureForm').submit();
		                }
		                reader.readAsDataURL(file);
		            } else {
		                alert("Erro");
		            }
		        }
    </script>
</body>
</html>