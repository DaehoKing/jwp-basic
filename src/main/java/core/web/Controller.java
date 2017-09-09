package core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dapa56 on 2017. 9. 8..
 */
public interface Controller {

	String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
