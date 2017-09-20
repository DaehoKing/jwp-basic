package core.ref;

import org.junit.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object obj = clazz.newInstance();

        Method[] methods = clazz.getMethods();
        for (Method method:
             methods) {
            if(method.getName().startsWith("test")) {
                System.out.println(method.getName()+" call...");
                method.invoke(obj);
            }
        }

    }
}
