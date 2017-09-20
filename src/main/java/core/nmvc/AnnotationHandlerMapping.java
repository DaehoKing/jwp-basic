package core.nmvc;

import com.google.common.collect.Maps;
import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping{
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private String[] basePackages;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() {
        this.getAnnotatedClassList(Controller.class).stream().forEach(cls ->{
            Object handler = null;
            try {
                handler = cls.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("{}", e);
            }
            Controller annotation = (Controller)cls.getAnnotation(Controller.class);
            for (Method handlerMethod : getAnnotatedMethodList(cls.getMethods(), RequestMapping.class)) {
                RequestMapping rm = handlerMethod.getAnnotation(RequestMapping.class);
                String path = annotation.value() + rm.value();
                handlerExecutions.put(new HandlerKey(path, rm.method()), new HandlerExecution(handler, handlerMethod));
            }
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private List<Method> getAnnotatedMethodList(Method[] methods, Class annotation) {
        List<Method> annotated = new ArrayList<>();
        for (Method method: methods) {
            if(method.isAnnotationPresent(annotation)) {
                annotated.add(method);
            }
        }
        return annotated;

    }
    private List<Class> getAnnotatedClassList(Class annotation) {
        List<Class> clazzList = new ArrayList<Class>();
        for (String fileName :
                this.getFileNameList()) {
            try {
                Class clazz = Class.forName(fileName);
                if(clazz.isAnnotationPresent(annotation)) {
                    clazzList.add(Class.forName(fileName));
                }
            } catch (ClassNotFoundException e) {
                logger.error("{}", e);
            }
        }

        return clazzList;
    }

    private List<String> getFileNameList() {
        List<String> fileNameList= new ArrayList<>();
        for(String basePackage : basePackages) {
            basePackage = basePackage.replace(".", "/");
            searchFiles(new File(ClassLoader.getSystemClassLoader().getResource("./" + basePackage).getPath()), fileNameList);
        }
        return fileNameList;
    }

    public void searchFiles(File file, List<String>fileNameList) {
        if(file.isDirectory()) {
            for (File f :
                    file.listFiles()) {
                searchFiles(f,fileNameList);
            }
        }
        else {
            String fileName = file.getName();
            if(fileName.endsWith(".class")) {
                for(String basePackage : basePackages) {
                    int basePackagePos = file.getPath().indexOf(basePackage.replace(".","/"));
                    if( basePackagePos != -1 ) {
                        fileNameList.add(
                                file.getPath().substring(basePackagePos, file.getPath().indexOf(".class")).replace("/", ".")
                        );
                    }
                }
            }
        }
    }
}
