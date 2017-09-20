package core.nmvc;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dapa56 on 2017. 9. 14..
 */
public interface HandlerMapping {
	Object getHandler(HttpServletRequest request);
}
