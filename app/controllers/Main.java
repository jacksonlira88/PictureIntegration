package controllers;

import play.mvc.Controller;

public class Main extends Controller {
	
	public static void home() {
        render();
    }
	
	public static void login() {
	    render();
	}
	
	public static void logoff() {
		session.clear();
		login();
	}
	
	public static void upload() {
		render();
	}

}
