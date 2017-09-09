package core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dapa56 on 2017. 9. 8..
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	private final String REDIRECT_PREFIX = "redirect:";

	@Override protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String path = req.getRequestURI();
			Controller controller = RequestMapping.getController(path);
			String view = controller.execute(req, resp);
			log.debug("View Name : {}", view);
			view(view, req, resp);
		}
		catch (Exception e) {
			log.debug("{}",e);
		}
	}

	private void view(String view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		if(view != null && view.startsWith(REDIRECT_PREFIX)) {
			resp.sendRedirect(view.substring(REDIRECT_PREFIX.length()));
			return;
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher(view);
		dispatcher.forward(req,resp);
	}

}
