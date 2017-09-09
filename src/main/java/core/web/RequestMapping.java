package core.web;

import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dapa56 on 2017. 9. 8..
 */
public class RequestMapping {
	private static final Logger log = LoggerFactory.getLogger(RequestMapping.class);
	static public Map<String, Controller> map = new HashMap<>();
	static{
		log.debug("initializing CreateUserController...");
		map.put("/users/create", new PostCreateUserController());
		map.put("/users/form", new CreateUserController());

		log.debug("initializing HomeController...");
		map.put("/", new HomeController());

		log.debug("initializing ListUserController...");
		map.put("/users", new ListUserController());

		log.debug("initializing ListUserController...");
		map.put("/users/login", new PostLoginController());
		map.put("/users/loginForm", new LoginController());

		log.debug("initializing LogoutController...");
		map.put("/users/logout", new LogoutController());

		log.debug("initializing ProfileController...");
		map.put("/users/profile", new ProfileController());

		log.debug("initializing UpdateUserController...");
		map.put("/users/update", new PostUpdateUserController());
		map.put("/users/updateForm", new UpdateUserController());
	}

	static Controller getController(String path){
		return map.get(path);
	}
}
