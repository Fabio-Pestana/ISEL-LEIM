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
	

@WebServlet("/Condutor3Servlet")
public class Condutor3Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    HttpSession session = request.getSession();
	    String nif = (String) session.getAttribute("nif");
		String morada = request.getParameter("morada");
		String piso = request.getParameter("floor");
		String fila = request.getParameter("row");
		String numero = request.getParameter("spot");
		String avaliacao = request.getParameter("rating");
		String comentario = request.getParameter("comment");
		
		if(morada.length() != 0) {
			Condutor condutor = new Condutor();
			boolean sucesso = condutor.entregarVeiculo(nif, piso, fila, numero, morada);
			if(sucesso) {
				request.setAttribute("mensagem", "Entrega do veiculo com sucesso");
				if(!avaliacao.equals("")) {
					int aval = Integer.parseInt(avaliacao);
					condutor.avaliarServicoWeb(nif, aval, comentario);
				}
			}else {
				request.setAttribute("mensagem", "Insucesso ao entregar o veiculo");
			}
		}
        request.getRequestDispatcher("condutorEntregarVeiculo.jsp").forward(request, response);
    }
}


