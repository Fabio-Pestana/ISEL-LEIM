<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
String html = (String) request.getAttribute("resultados");
String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/my_rent_a_car.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <title>My Rent-a-Car</title>
	<style>
		.checkbox-wrapper {
		    display: flex;
		    align-items: center;
		    justify-content: flex-start; /* Alinha à esquerda */
		    gap: 5px; /* Adiciona um pequeno espaço entre a label e o checkbox */
		}

		.checkbox-wrapper label {
		    margin-right: 8px; /* Espaço entre o texto e o checkbox */
		}
		
		.error-message {
		    color: red;
		    font-weight: bold;
		    margin-top: 20px;
		}

	</style>
</head>

<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Gerente.html">
                <i class="bi bi-arrow-left-circle"></i> Voltar
            </a>
        </div>
        <h1 class="text-play">Histórico de um Veículo</h1>
    </header>
    <main>
        <form id="vehicle-form" method="POST" action="Gerente1Servlet">
            <label for="license-plate">Matrícula do Veículo:</label>
            <input type="text" id="license-plate" name="license-plate" placeholder="Matrícula" required>

            <label for="months">Número de Meses:</label>
            <input type="number" id="months" name="months" placeholder="Número de meses" min="1" step="1">

            <div class="checkbox-wrapper">
                <label for="full-history">Todo o Histórico</label>
                <input type="checkbox" id="full-history" name="full-history">
            </div>

            <button type="submit">Procurar</button>
        </form>
        <div>
            <%= html != null ? html : "" %>
            <% if (erro != null && !erro.isEmpty()) { %>
                <div class="error-message">
                    <%= erro %>
                </div>
            <% } %>
        </div>
    </main>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>

    <script>
        const monthsInput = document.getElementById('months');
        const fullHistoryCheckbox = document.getElementById('full-history');

        fullHistoryCheckbox.addEventListener('change', () => {
            if (fullHistoryCheckbox.checked) {
                monthsInput.disabled = true;
                monthsInput.value = "";
            } else {
                monthsInput.disabled = false;
            }
        });

        monthsInput.addEventListener('input', () => {
            if (monthsInput.value) {
                fullHistoryCheckbox.checked = false;
            }
        });
    </script>
</body>
</html>