import java.io.IOException;
import java.util.Enumeration;

import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import validate.CheckName;

@WebServlet("/RestartGameServlet")
public class RestartGameServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Jogador jogador = CheckName.getJogador(request, response);
		HttpSession session = request.getSession(false);
		if (session != null) {
			try {
				boolean retry = jogador.getStub().playAgainWeb(true);
				if(retry) {
					response.sendRedirect(request.getContextPath() + "/othello.jsp");
				}else {
					jogador.fechar();
					jogador = new Jogador();
					ServletContext context = request.getServletContext();
					String uid = (String) session.getAttribute("uid");
					context.setAttribute(uid, jogador);
					session.setAttribute("simbolo", null);
					request.getRequestDispatcher("/menu.jsp").forward(request,response);
				}
			} catch (Exception e) {
				request.getRequestDispatcher("/login.html").forward(request,response);
			}
		} else {
		    System.out.println("A sessão não está disponível.");
		}
    }
}