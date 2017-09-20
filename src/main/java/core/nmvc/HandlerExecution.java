package core.nmvc;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    Object handler;
    Method handlerMethod;

    public HandlerExecution(Object hanler, Method handlerMethod) {
        this.handler = hanler;
        this.handlerMethod = handlerMethod;
    }
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return (ModelAndView) handlerMethod.invoke(handler, request, response);
    }
}
