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

	
@WebServlet("/FinishGameServlet")
public class FinishGameServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gameSt = request.getParameter("gameState");
		
		Jogador jogador = CheckName.getJogador(request, response);
		String nome = CheckName.login(request, response);
		
		if(gameSt!=null) {
        	long Final_time = CheckName.getTime(request, response);
        	int gameState = Integer.parseInt(gameSt);
        	try {
				jogador.getStub().updateUserWeb(nome, gameState, Final_time);		
			} catch (Exception e) {
				request.getRequestDispatcher("/login.html").forward(request, response);
				return;
			}
        	request.getRequestDispatcher("/BackToMenuOrGame.jsp").forward(request,response);
        }
	}
}