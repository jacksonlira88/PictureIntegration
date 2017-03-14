package controllers;

import models.User;
import play.Logger;
import play.libs.OAuth2;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;

import com.google.gson.JsonObject;

public class Application extends Controller {

    // The following keys correspond to a test application
    // registered on Facebook, and associated with the loisant.org domain.
    // You need to bind loisant.org to your machine with /etc/hosts to
    // test the application locally.

	public static OAuth2 FACEBOOK = new OAuth2(
            "https://graph.facebook.com/oauth/authorize",
            "https://graph.facebook.com/oauth/access_token",
            "566151883592285",
            "92a1ae36b2a00b92d7714b618fd6ae93"
    );
    
	//public static void index() {
		//login();
	//	render();
	//}
	
	public static void home() {
        render();
    }
    
	public static void login() {
	    render();
	}
	
	public static void upload() {
		render();
	}
	
	public static void auth() {
	    if (OAuth2.isCodeResponse()) {
	        User u = null;
	        
	        OAuth2.Response response;
	        response = FACEBOOK.retrieveAccessToken(authURL());
	        
	        String accessToken = response.accessToken;
	        
	        JsonObject me = WS.url("https://graph.facebook.com/me?fields=id,name,email&access_token=%s", WS.encode(accessToken)).get().getJson().getAsJsonObject();
	        
	        //String email = "teste@gmail.com";
	       // String email = me.get("email").getAsString();
	        String id = me.get("id").getAsString();
	        u = User.find("lower(id)", id.toLowerCase()).first();
	        if(u == null){
	        	u = new User();
	        	u.id = id;
	        	u.save();
	        }
	        home();
	    }
	    FACEBOOK.retrieveVerificationCode(authURL());
	}
	
	static String authURL() {
	    return play.mvc.Router.getFullUrl("Application.auth");
	}
	
	public static void sair(){
		Login.logoff();
	}

}
