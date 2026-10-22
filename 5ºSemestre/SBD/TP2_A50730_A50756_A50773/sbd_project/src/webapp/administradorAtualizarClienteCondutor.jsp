<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String msgCliente = (String) request.getAttribute("mensagemCliente");
String msgCoundutor = (String) request.getAttribute("mensagemCondutor");
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/double_info_page.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <title>My Rent-a-Car</title>

</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Administrador.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Atualizar ficha Cliente/Condutor</h1>
    </header>

    <div class="container">
        <main class="cliente">
            <form method="POST" action="Admin1Servlet">
                <label for="NIF">Insira o NIF do cliente que quer atualizar: </label>
                <input type="text" id="NIF" name="NIF" placeholder="NIF" required>
    
                <label for="fieldCliente">Qual campo deseja trocar? </label>
                <select id="fieldCliente" name="fieldCliente" required>
                    <option value="" disabled selected>Escolha uma opção</option>
                    <option value="Nome">Nome</option>
                    <option value="Tipo">Tipo(empresa ou pessoa)</option>
                    <option value="Distrito">Distrito</option>
                    <option value="Concelho">Concelho</option>
                    <option value="Freguesia">Freguesia</option>
                    <option value="Telemovel">Telemóvel</option>
                    <option value="Mail">Mail</option>
                    <option value="Lingua">Língua</option>
                    <option value="CapitalSocial">Capital Social</option>
                    <option value="PreferenciaCultural">Preferência Cultural</option>
                </select>
    
                <label for="newValueCliente">Insira o novo valor: </label>
                <input type="text" id="newValueCliente" name="newValueCliente" placeholder="Novo valor" required>
    
                <div class="wrap">
                    <button type="submit"> Submit </button>
                </div>
                <div>
		             <%= msgCliente != null ? msgCliente : "" %>
		        </div>
            </form>
        </main>

        <main class="condutor">
            <form method="POST" action="Admin2Servlet">
                <label for="NIF">Insira o NIF do cliente ao qual o condutor está associado: </label>
                <input type="text" id="NIF" name="NIF" placeholder="NIF" required>
                
                <label for="CartaConducao">Insira o número da carta de condução do condutor: </label>
                <input type="text" id="CartaConducao" name="CartaConducao" placeholder="Nº Carta de Conducao" required>

                <label for="fieldCondutor">Qual campo deseja trocar? </label>
                <select id="fieldCondutor" name="fieldCondutor" required>
                    <option value="" disabled selected>Escolha uma opção</option>
                    <option value="Nome">Nome do Condutor</option>
                    <option value="DataNascimento">Data de Nascimento</option>
                    <option value="DataEmissaoCarta">Data de Emissão da Carta</option>
                    <option value="ValidadeCarta">Validade da Carta</option>
                </select>
    
                <label for="newValueCondutor">Insira o novo valor: </label>
                <input type="text" id="newValueCondutor" name="newValueCondutor" placeholder="Novo valor" required>
    
                <div class="wrap">
                    <button type="submit"> Submit </button>
                </div>
                <div>
		             <%= msgCoundutor != null ? msgCoundutor : "" %>
		        </div>
            </form>
        </main>
    </div>

    <footer>
        <div class="footer-text">
            &copy;2024 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>

    <script>
        const fieldCondutor = document.getElementById('fieldCondutor');
        const newValueCondutor = document.getElementById('newValueCondutor');

        fieldCondutor.addEventListener('change', function () {
            const selectedValue = fieldCondutor.value;
            if (selectedValue === 'DataNascimentoCondutor' || 
                selectedValue === 'DataEmissaoCarta' || 
                selectedValue === 'ValidadeCarta') {
                newValueCondutor.type = 'date';
            } else {
                newValueCondutor.type = 'text';
            }
        });
    </script>
</body>

</html>
