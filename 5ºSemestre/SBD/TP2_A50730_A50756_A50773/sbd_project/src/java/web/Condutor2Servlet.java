package pt.isel.leim.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Condutor;
	

@WebServlet("/Condutor2Servlet")
public class Condutor2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    
		String morada = request.getParameter("nearestParkingAddress");
		
		Condutor condutor = new Condutor();
		String[][] resultados = condutor.lugaresDisponiveis(morada);
		String piso = gerarOpcoesHtml(resultados, 0);
		String fila = gerarOpcoesHtml(resultados, 1);
		String numero = gerarOpcoesHtmlNumeros(resultados);
		request.setAttribute("pisos", piso);
		request.setAttribute("filas", fila);
		request.setAttribute("numeros", numero);
		request.setAttribute("morada", morada);

        request.getRequestDispatcher("condutorProcurarLugares.jsp").forward(request, response);
    }
	
	
	private String gerarOpcoesHtml(String[][] resultados, int idx) {
        Set<String> uniqueValues = new HashSet<>();
        StringBuilder options = new StringBuilder();

        for (String[] row : resultados) {
            String aux = row[idx];
            if (uniqueValues.add(aux)) {
                options.append("<option value=\"").append(aux).append("\">").append(aux).append("</option>");
            }
        }

        return options.toString();
    }
	
	// Gera as opções de número com o formato: "1 (piso: x e fila: y)"
    private String gerarOpcoesHtmlNumeros(String[][] resultados) {
        StringBuilder options = new StringBuilder();

        for (String[] row : resultados) {
            String numero = row[2];
            String piso = row[0];
            String fila = row[1];
            options.append("<option value=\"").append(numero).append("\">")
                   .append(numero).append(" (piso: ").append(piso)
                   .append(" e fila: ").append(fila).append(")</option>");
        }

        return options.toString();
    }
}

