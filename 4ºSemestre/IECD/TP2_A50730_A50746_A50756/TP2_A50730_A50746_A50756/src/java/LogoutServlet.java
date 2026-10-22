import java.io.IOException;

import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import validate.CheckName;

/**
 * Servlet que suporta a logout dos utilizadores
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String uid = request.getParameter("uid");
			String uidTime = request.getParameter("uid_Time");
			
			HttpSession session = request.getSession();
			ServletContext context = request.getServletContext();
			
			if(uid!=null) {
				Jogador jogador = CheckName.getJogador(request, response);
				if(jogador!=null) {
					jogador.fechar();
					
					context.removeAttribute(uidTime);
					context.removeAttribute(uid);
					session.removeAttribute("uid");
					session.removeAttribute("uid_Time");
					session.removeAttribute("name");
					session.removeAttribute("simbolo");
					session.removeAttribute("end");
					session.removeAttribute("color");
				}
			}
			
			request.getSession().invalidate();
			request.getRequestDispatcher("/login.html").forward(request, response);

	}

}
