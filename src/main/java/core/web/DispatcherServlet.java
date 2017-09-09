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
@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		super.doGet(req, resp);
		try {
			this.processRequest(req,resp);
		} catch (Exception e) {
			log.debug("{}",e);
		}
	}

	@Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		super.doPost(req, resp);
		try {
			this.processRequest(req, resp);
		} catch (Exception e) {
			log.debug("{}",e);
		}
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("Servlet path : {}", path);
		Controller controller = RequestMapping.getController(path);
		String view = controller.execute(req, resp);
		log.debug("result of controller : {}", view);

		if(view != null && view.startsWith("redirect:")) {
			view = view.split(":")[1];
			resp.sendRedirect(view);
			return;
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher(view);
		dispatcher.forward(req,resp);
	}
}
