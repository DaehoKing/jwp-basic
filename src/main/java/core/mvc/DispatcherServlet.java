package core.mvc;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping am;
    private RequestMapping rm;

    @Override
    public void init() throws ServletException {
        am = new AnnotationHandlerMapping();
        am.initialize();
        rm = new RequestMapping();
        rm.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);


        Object handler = am.getHandler(req);
        if(handler == null) {
            handler = rm.getHandler(req);
        }
        ModelAndView mav;
        try {
            mav = execute(handler, req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        if( handler instanceof Controller) {
            return ((Controller) handler).execute(request, response);
        }
        return null;
    }
}
