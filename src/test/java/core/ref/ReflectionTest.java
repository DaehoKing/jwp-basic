package core.ref;

import next.model.Question;
import next.model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        for (Field field :
                clazz.getDeclaredFields()) {
            logger.debug("field : {} {} {}", Modifier.toString(field.getModifiers()), field.getType(), field.getName());
        }
        for (Constructor constructor :
                clazz.getConstructors()) {
            logger.debug("Constructor : {} {} {}", Modifier.toString(constructor.getModifiers()), constructor.getDeclaringClass(), constructor.getName());
        }
        for (Method method :
                clazz.getMethods()) {
            logger.debug("Method : {} {} {}", Modifier.toString(method.getModifiers()), method.getDeclaringClass(), method.getName());
        }
    }
    
    @Test
    public void newInstanceWithConstructorArgs() throws NoSuchMethodException {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());
        Stream.of(clazz.getDeclaredConstructor(
                String.class
                ,String.class
                ,String.class
                ,String.class
        )).forEach(constructor -> {
            try {
                ((User)constructor.newInstance("dapa56", "123", "김대호", "a@b")).getName();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
    
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Constructor constructor = clazz.getConstructor();
        Object obj = constructor.newInstance();
        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);
        nameField.set(obj, "김대호");
        ageField.setInt(obj, 28);

        Student student = ((Student) obj);
        assertEquals("김대호", student.getName());
        assertEquals(28, student.getAge());
    }
}
