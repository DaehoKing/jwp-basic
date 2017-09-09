package next.controller;

import core.web.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet(value = { "/users/login", "/users/loginForm" })
public class LoginController implements Controller {
    private static final long serialVersionUID = 1L;

    @Override public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/login.jsp";
    }
}
