package core;

import core.annotation.Controller;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by dapa56 on 2017. 9. 13..
 */
public class FileTest {
	@Test
	public void searchFileTest() throws IOException {
		Reflections reflections = new Reflections("core");
		reflections.getTypesAnnotatedWith(Controller.class);
		Enumeration path = Thread.currentThread().getContextClassLoader().getResources("core/ref");
		while(path.hasMoreElements()) {
			System.out.println(path.nextElement());
		}
//		System.out.println(Thread.currentThread().getContextClassLoader().getResources("core/ref/").getPath());
//		File file = new File(ClassLoader.getSystemClassLoader().getResource("core.ref").getPath());
//		Stream.of(file.listFiles()).forEach(f->{
//			getFiles(file);
//		});
//		Stream.of(file.listFiles()).forEach(f -> {
//			System.out.println(f.getName());
//		});
	}

	public void getFiles(File file) {
		if(file.isDirectory()) {
			for (File f :
					file.listFiles()) {
				getFiles(f);
			}
		}
		else {
			System.out.println(file.getName());
		}
	}
}
