package core.mvc;

import com.google.common.collect.Sets;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private Set<HandlerMapping> handlerMappingSet = Sets.newHashSet();

    @Override
    public void init() throws ServletException {
        AnnotationHandlerMapping am = new AnnotationHandlerMapping();
        am.initialize();

        RequestMapping rm = new RequestMapping();
        rm.initMapping();

        handlerMappingSet.add(am);
        handlerMappingSet.add(rm);
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping hm :
                handlerMappingSet) {
            Object obj = hm.getHandler(req);
            if(obj != null) {
                return obj;
            }
        }
        return null;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);


        Object handler = getHandler(req);
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
