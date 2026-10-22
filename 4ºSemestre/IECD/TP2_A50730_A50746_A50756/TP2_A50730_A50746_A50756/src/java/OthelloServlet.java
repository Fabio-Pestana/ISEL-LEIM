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
	 * Jogador jogador = CheckName.getJogador(request, response);
	 * 
	 * Servlet que suporta a login/autenticação dos utilizadores
	 */
@WebServlet("/OthelloServlet")
public class OthelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String linha = request.getParameter("linha");
        String coluna = request.getParameter("coluna");
        String time = request.getParameter("timeOfPlay");
  
        Jogador jogador = CheckName.getJogador(request, response);

        if (linha != null && coluna != null) {
            int row = Integer.parseInt(linha);
            int col = Integer.parseInt(coluna);

            try {
                jogador.getStub().jogarWeb(row, col);
                if (time != null) {
                    long timeOfPlay = Long.parseLong(time);
                    CheckName.addTime(request, response, timeOfPlay);
                }
            } catch (Exception e) {
                jogador.fechar();
                request.getRequestDispatcher("/login.html").forward(request, response);
                return; // Aqui retornamos para evitar que o código abaixo seja executado após o encaminhamento
            }
        }

        // Encaminhe a solicitação fora do bloco try-catch
        response.sendRedirect(request.getContextPath() + "/othello.jsp");
    }
}