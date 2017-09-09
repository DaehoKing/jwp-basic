package next.controller;

import core.db.DataBase;
import core.web.Controller;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by dapa56 on 2017. 9. 8..
 */
public class PostLoginController implements Controller {
	@Override public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		User user = DataBase.findUserById(userId);
		if (user == null) {
			req.setAttribute("loginFailed", true);
			return "/user/login.jsp";
		}

		if (user.matchPassword(password)) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
			return "redirect:/";
		} else {
			req.setAttribute("loginFailed", true);
			return "/user/login.jsp";
		}
	}
}
