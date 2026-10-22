<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String msgCliente = (String) request.getAttribute("mensagemCliente");
String msg = (String) request.getAttribute("mensagemCondutor");
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/double_info_page.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <title>My Rent-a-Car</title>
    <script>
        function toggleCapitalSocial() {
            const tipo = document.getElementById("Tipo").value;
            const capitalSocial = document.getElementById("CapitalSocial");

            if (tipo === "empresa") {
                capitalSocial.disabled = false;
            } else {
                capitalSocial.disabled = true;
                capitalSocial.value = ""; 
            }
        }
		
		function validateCartaDates(event) {
		            const dataEmissao = document.getElementById("DataEmissaoCarta").value;
		            const validadeCarta = document.getElementById("ValidadeCarta").value;
		
		            if (dataEmissao && validadeCarta) {
		                const dataEmissaoDate = new Date(dataEmissao);
		                const validadeCartaDate = new Date(validadeCarta);
		
		                if (dataEmissaoDate >= validadeCartaDate) {
		                    alert("A Data de Emissão deve ser mais antiga que a Validade da Carta de Condução.");
		                    event.preventDefault(); 
		                }
		            }
		        }
    </script>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Administrador.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Criar ficha Cliente/Condutor</h1>
    </header>

    <div class="container">

        <main class="cliente">
            <h1 class="text"> Ficha Cliente</h1>
            <form method="POST" action="Admin4Servlet">
                <label for="NIF">Insira o NIF: </label>
                <input type="text" id="NIF" name="NIF" placeholder="NIF" required>
    
                <label for="Nome">Insira o Nome: </label>
                <input type="text" id="Nome" name="Nome" placeholder="Nome" required>
				
				<label for="Tipo">Selecione o tipo: </label>
				<select id="Tipo" name="Tipo" onchange="toggleCapitalSocial()" required>
					<option value="" disabled selected>Escolha uma opção</option>
					<option value="pessoa">Pessoa</option>
					<option value="empresa">Empresa</option>				                    
				</select>
    	
                <label for="Distrito">Insira o Distrito (se for português): </label>
                <input type="text" id="Distrito" name="Distrito" placeholder="Distrito">
    
                <label for="Concelho">Insira o Concelho (se for português): </label>
                <input type="text" id="Concelho" name="Concelho" placeholder="Concelho">
    
                <label for="Freguesia">Insira a Freguesia (se for português): </label>
                <input type="text" id="Freguesia" name="Freguesia" placeholder="Freguesia">
    
                <label for="Telemovel">Insira o Número de Telemóvel: </label>
                <input type="text" id="Telemovel" name="Telemovel" placeholder="Telemóvel" required>
    
                <label for="Mail">Insira o endereço Mail: </label>
                <input type="text" id="Mail" name="Mail" placeholder="Mail" required>
    
                <label for="Lingua">Insira a Preferência Linguística: </label>
                <input type="text" id="Lingua" name="Lingua" placeholder="Língua" required>
    
                <label for="CapitalSocial">Insira a Capital Social (se empresa): </label>
                <input type="number" id="CapitalSocial" name="CapitalSocial" placeholder="Capital Social" disabled>

                <label for="PrefCultural">Insira uma Preferência Cultural (caso necessário): </label>
                <input type="text" id="PrefCultural" name="PrefCultural" placeholder="PrefCultural">
    
                <div class="wrap"> 
                    <button type="submit"> Submit </button>
                </div>
                <div>
		    	<%= msgCliente != null ? msgCliente : "" %>
		   	 	</div>
            </form>
        </main>

        <main class="condutor"> 
            <h1 class="text">Ficha Condutor</h1>
            <form method="POST" action="Admin5Servlet" onsubmit="validateCartaDates(event)">
                <label for="NIF">Insira o NIF do Cliente associado: </label>
                <input type="text" id="NIF" name="NIF" placeholder="NIF" required>

                <label for="NomeCondutor">Insira o Nome do Condutor: </label>
                <input type="text" id="NomeCondutor" name="NomeCondutor" placeholder="Nome do Condutor" required>

                <label for="DataNascimentoCondutor">Data de Nascimento do Condutor: </label>
                <input type="date" id="DataNascimentoCondutor" name="DataNascimentoCondutor" required>

                <label for="CartaConducao">Número da Carta de Condução: </label>
                <input type="text" id="CartaConducao" name="CartaConducao" placeholder="Nº Carta" required>

                <label for="DataEmissaoCarta">Data de Emissão da Carta: </label>
                <input type="date" id="DataEmissaoCarta" name="DataEmissaoCarta" required>

                <label for="ValidadeCarta">Validade da Carta de Condução: </label>
                <input type="date" id="ValidadeCarta" name="ValidadeCarta" min="" required>

                <div class="wrap">
                    <button type="submit">Submit</button>
                </div>
                <div>
		    	<%= msg != null ? msg : "" %>
		   	 	</div>
            </form>
        </main>
    </div>
    
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
    
    <script>
	    var today = new Date();
	    var dd = today.getDate();
	    var mm = today.getMonth() + 1; 
	    var yyyy = today.getFullYear();
	
	    if (dd < 10) {
	       dd = '0' + dd;
	    }
	
	    if (mm < 10) {
	       mm = '0' + mm;
	    } 
	        
	    today = yyyy + '-' + mm + '-' + dd;
	    document.getElementById("ValidadeCarta").setAttribute("min", today);
    </script>
</body>

</html>
