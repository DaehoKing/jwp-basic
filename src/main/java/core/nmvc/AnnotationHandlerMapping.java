package core.nmvc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private String[] basePackages;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackages);
        List<Object> handlerList = getHandlerList(reflections.getTypesAnnotatedWith(Controller.class));
        for(Object handler : handlerList) {
            for(Method method : getHandlerMethodList(handler)) {
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                handlerExecutions.put(new HandlerKey(rm.value(), rm.method()), new HandlerExecution(handler, method));
            }
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private List<Method> getHandlerMethodList(Object handler) {
        List<Method> handlerMethods = Lists.newArrayList();
        for(Method method : handler.getClass().getMethods()) {
            if(method.isAnnotationPresent(RequestMapping.class)) {
                handlerMethods.add(method);
            }
        }
        return handlerMethods;
    }

    private List<Object> getHandlerList(Set<Class<?>> clsSet) {
        List<Object> executionList = new ArrayList<>();
        for (Class<?> cls : clsSet) {
            Object handler = null;
            try {
                handler = cls.newInstance();
                executionList.add(handler);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("{}", e);
                continue;
            }
        }
        return executionList;
    }

}
