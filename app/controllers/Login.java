package controllers;

import play.mvc.Controller;

public class Login extends Controller {
	
	public static void logoff() {
		session.clear();
		Application.login();
	}

}
