package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin6Servlet")
public class Admin6Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    String matricula = request.getParameter("Matricula");
		matricula = matricula.toUpperCase();
		String Marca = request.getParameter("Marca");
		String Modelo = request.getParameter("Modelo");
		String Categoria = request.getParameter("Categoria");
		String Cor = request.getParameter("Cor");
		int NumLugares = Integer.valueOf(request.getParameter("NumLugares"));
		int NumPortas = Integer.valueOf(request.getParameter("NumPortas"));
		int PotMotor = Integer.valueOf(request.getParameter("PotMotor"));
		String TipoMotor = request.getParameter("TipoMotor");
		String Estado = request.getParameter("Estado");
		double RegistoKm = Double.valueOf(request.getParameter("RegistoKm"));

		Marca = Auxiliar.normalizaTexto(Marca);
		Modelo = Auxiliar.normalizaTexto(Modelo);
		Cor = Auxiliar.normalizaTexto(Cor);

		Administrador admin = new Administrador();
		
		boolean sucesso = admin.criarVeiculo(matricula, Cor, Marca, Modelo, NumLugares, NumPortas, PotMotor, TipoMotor, Estado, RegistoKm, Categoria);
		
		if(sucesso) {
			request.setAttribute("mensagem", "Sucesso ao criar o veiculo com a matricula " + matricula + ".");
		}else {
			request.setAttribute("mensagem", "Veículo já existe (matricula: " + matricula + ") ou os dados introduzidos não são válidos.");
		}
        request.getRequestDispatcher("adminCriarVeiculo.jsp").forward(request, response);
    }
}



