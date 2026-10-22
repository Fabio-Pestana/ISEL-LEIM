import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet que suporta a login/autenticação dos utilizadores
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = java.util.UUID.randomUUID().toString();
		String uidTime = java.util.UUID.randomUUID().toString();
		String nome = request.getParameter("username");
		String senha = request.getParameter("password");
		
		long time = 0L;
	      
		if(nome!=null && senha!=null) {
			try {
				Jogador jogador = new Jogador();
				if(jogador.getStub().loginWeb(nome, senha)) {
					String color = jogador.getStub().getFavColorWeb(nome);
					HttpSession session = request.getSession();
					ServletContext context = request.getServletContext();
					context.setAttribute(uidTime, time);
					context.setAttribute(uid, jogador);
					session.setAttribute("uid", uid);
					session.setAttribute("uid_Time", uidTime);
					session.setAttribute("name",nome);
					session.setAttribute("simbolo", null);
					session.setAttribute("color",color);
					request.getRequestDispatcher("/menu.jsp").forward(request,response);
				}else {
					jogador.fechar();
					request.getRequestDispatcher("/login.html").forward(request, response);
				}
			} catch (Exception e) {}
		}else {
			request.getRequestDispatcher("/login.html").forward(request, response);
		}
	}
}