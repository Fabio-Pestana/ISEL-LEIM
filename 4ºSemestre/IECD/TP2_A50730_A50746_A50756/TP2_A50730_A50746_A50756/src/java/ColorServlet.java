import java.io.IOException;

import client.Jogador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import validate.CheckName;

/**
 * Servlet que suporta a login/autenticação dos utilizadores
 */
@WebServlet("/ColorServlet")
public class ColorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = CheckName.login(request, response);	
		String color = request.getParameter("color-picker");
	      
		if(nome!=null && color!=null) {
			color = color.substring(1);
			Jogador jogador = CheckName.getJogador(request, response);
			try {
				if(jogador.getStub().changeColorWeb(nome, color)) {		
					HttpSession session = request.getSession();
					session.setAttribute("color",color);
					request.getRequestDispatcher("/changeColor.jsp").forward(request,response);
				}else {
					request.getRequestDispatcher("/login.html").forward(request, response);
				}
			} catch (Exception e) {
				jogador.fechar();
				request.getRequestDispatcher("/login.html").forward(request, response);
			}
		}else {
			request.getRequestDispatcher("/login.html").forward(request, response);
		}
	}
}
