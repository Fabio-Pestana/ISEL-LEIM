<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pt.isel.leim.sbd.Stub"%>
<%@ page import="pt.isel.leim.sbd.Condutor"%>
<%@ page import="pt.isel.leim.sbd.Auxiliar"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>

<% 
String msg = (String) request.getAttribute("mensagem");

Stub stub = new Stub();
String nif = (String) session.getAttribute("nif");

String[][] parques = Auxiliar.listarParquesDadosWeb(stub);


StringBuilder parkingLocations = new StringBuilder("[");
for (int i = 0; i < parques.length; i++) {
    String morada = parques[i][0];
    String latitude = parques[i][1];
    String longitude = parques[i][2];
    parkingLocations.append("{ name: '").append(morada).append("', lat: ").append(latitude).append(", lng: ").append(longitude).append(" }");
    if (i < parques.length - 1) parkingLocations.append(", ");
}
parkingLocations.append("]");

Condutor c = new Condutor();
boolean temCarro = c.carrosAlugados(nif);
%>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/double_info_page.css">
    <link href="https://fonts.googleapis.com/css?family=Play" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css" />
    <style>
        #map {
            height: 400px;
            margin: 20px 0;
            border: 2px solid #ccc;
            border-radius: 8px;
        }
        
        main {
		    width: 60%; 
		    max-width: 600px; 
		    margin: auto; 
		}
    </style>
    <title>My Rent-a-Car</title>
</head>
<body>
    <header>
        <div style="position: absolute; top: 10px; left: 10px;">
            <a class="button-link" href="Condutor.html">
                Voltar
            </a>
        </div>
        <h1 class="text-play">Entregar Veículo</h1>
    </header>
    <div class="container">
        <main class="map">
	        <div>
		    	<%= msg != null ? msg : "" %>
		   	</div>
        	<form method="POST" action="Condutor2Servlet">
            <div id="map"></div>
            <p id="nearest-parking">A calcular o parque de estacionamento mais próximo...</p>
            <input type="hidden" id="hidden-parking" name="nearestParkingAddress" value="">
            
            <div class="wrap">
                <button type="submit" id="submit-button" <% if (!temCarro) { %> disabled <% } %>>Entregar Veículo</button>
            </div>
            <% if (!temCarro) { %>
                <p>Não há carros alugados para entregar.</p>
            <% } %>
           </form> 
        </main>
    </div>
    <footer>
        <div class="footer-text">
            &copy;2025 Copyright | Fábio Pestana - João Ramos - Rafael Dias
        </div>
    </footer>

    <script>
        // Dados dos parques de estacionamento (gerados no servidor)
        const parkingLocations = <%= parkingLocations.toString() %>;

        const map = L.map('map').setView([0, 0], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        const calculateDistance = (lat1, lon1, lat2, lon2) => {
            const toRad = (value) => value * Math.PI / 180;
            const R = 6371; // Raio da Terra em km
            const dLat = toRad(lat2 - lat1);
            const dLon = toRad(lon2 - lon1);
            const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
            const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c; // Distância em km
        };

        const findNearestParking = (latitude, longitude) => {
            let nearest = null;
            let shortestDistance = Infinity;

            parkingLocations.forEach(parking => {
                const distance = calculateDistance(latitude, longitude, parking.lat, parking.lng);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearest = parking;
                }
            });

            return nearest;
        };

        const updateLocation = (position) => {
            const { latitude, longitude } = position.coords;

            map.setView([latitude, longitude], 15);
            L.marker([latitude, longitude]).addTo(map).bindPopup("Você está aqui.").openPopup();

            const nearestParking = findNearestParking(latitude, longitude);
            if (nearestParking) {
                document.getElementById('nearest-parking').textContent = 
                    "Parque de estacionamento mais próximo: " + nearestParking.name;
                
                document.getElementById('hidden-parking').value = nearestParking.name;

                L.marker([nearestParking.lat, nearestParking.lng])
                    .addTo(map)
                    .bindPopup(nearestParking.name)
                    .openPopup();
            } else {
                document.getElementById('nearest-parking').textContent = 
                    "Não foi possível determinar o parque de estacionamento mais próximo.";
            }
        };

        const showError = (error) => {
            document.getElementById('nearest-parking').textContent = 
                "Erro ao obter localização. Verifique se o GPS está ativo.";
            console.error(error.message);
        };

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(updateLocation, showError);
        } else {
            document.getElementById('nearest-parking').textContent = 
                "Geolocalização não é suportada pelo navegador.";
        }
    </script>
</body>
</html>
