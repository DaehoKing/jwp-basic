package next.controller;

import core.db.DataBase;
import core.web.Controller;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dapa56 on 2017. 9. 8..
 */
public class PostCreateUserController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(PostCreateUserController.class);

	@Override public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
				req.getParameter("email"));
		log.debug("User : {}", user);

		DataBase.addUser(user);

		return "redirect:/";
	}
}
