package next.controller;

import core.db.DataBase;
import core.web.Controller;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/users/profile")
public class ProfileController implements Controller {
    private static final long serialVersionUID = 1L;

    @Override public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);

        return "/user/profile.jsp";
    }
}
