<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="pt.isel.leim.sbd.Funcionario"%>
<%@page import="pt.isel.leim.sbd.Auxiliar"%>
<% 
String opcoes = (String) request.getAttribute("opcoes");
String aluguer = (String) request.getAttribute("aluguer");
String matriculaAluguer = (String) request.getAttribute("matricula");
String nif = (String) request.getAttribute("nif");
float custoPrevisto = (float) request.getAttribute("custoPrevisto");
float reputacao = (float) request.getAttribute("reputacao");

%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Rent-a-Car</title>
</head>
<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="funcionarioEscolherAluguer.jsp">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Finalização do Aluguer</h1>
    </header>
    <main>
    	<div>
             <%= opcoes != null ? opcoes : "" %>
        </div>
        <form id="finalize-form" method="POST" action="Funcionario5Servlet">
            <input type="hidden" id="custoFinalHidden" name="custoFinal" value="<%= custoPrevisto %>">
        	<input type="hidden" name="matriculaAluguer" value="<%= matriculaAluguer %>">
    		<input type="hidden" name="nif" value="<%= nif %>">
    		<input type="hidden" name="aluguer" value="<%= aluguer %>">
    		
                <div>
                    <label for="avaliacao">Avaliação do Condutor:</label>
                    <input type="number" id="avaliacao" name="avaliacao" min=1 max=5 required>
                </div>
                <div>
                    <label for="kmPercorridos">Km Percorridos:</label>
                    <input type="number" id="kmPercorridos" name="kmPercorridos" step="0.01" required>
                </div>
                <div>
                    <label for="desconto">Desconto:</label>
                    <select id="desconto" name="desconto" onChange="calcularCustoFinal()" required>
                    	<option value="0">0%</option>
                        <option value="5">5%</option>
                        <option value="10">10%</option>
                    </select>
                </div>
                <div>
                    <label for="custoFinal">Custo Final Efetivo:</label>
                    <p id="custoFinal"><%= custoPrevisto %> €</p>
                </div>
                <button type="submit">Finalizar Aluguer</button>
            </form>
            <script>
        function calcularCustoFinal() {
            // Obter os valores do custo previsto e desconto
            const custoPrevisto = parseFloat(<%= custoPrevisto %>);
            const descontoElement = document.getElementById("desconto");
            const descontoValue = descontoElement.options[descontoElement.selectedIndex].value;
            
            // Remover o símbolo de porcentagem e calcular o desconto
            const desconto = parseFloat(descontoValue) / 100;
            const custoFinal = custoPrevisto * (1 - desconto);

            // Atualizar o texto do parágrafo do custo final
            document.getElementById("custoFinal").innerText = custoFinal + "€";
            document.getElementById("custoFinalHidden").value = custoFinal;
        }
    </script>
    </main>
     <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>
</body>
</html>
