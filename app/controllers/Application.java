package controllers;

import models.User;
import play.Logger;
import play.libs.OAuth2;
import play.libs.WS;
import play.libs.OAuth2.Response;
import play.libs.WS.HttpResponse;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Params;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

public class Application extends Controller {

    // The following keys correspond to a test application
    // registered on Facebook, and associated with the loisant.org domain.
    // You need to bind loisant.org to your machine with /etc/hosts to
    // test the application locally

	public static String token;
	
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
	        token = response.accessToken;
	        
	        JsonObject me = WS.url("https://graph.facebook.com/me?fields=id,name,email&access_token=%s", WS.encode(token)).get().getJson().getAsJsonObject();
	        
	        //String email = "teste@gmail.com";
	        String email = me.get("email").getAsString();
	        String id = me.get("id").getAsString();
	        
	        u = User.find("lower(id)", id.toLowerCase()).first();
	        if(u == null){
	        	u = new User();
	        	u.id = id;
	        	u.save();
	        }
	        home();
	    }
	    FACEBOOK.retrieveVerificationCode(authURL(), "scope", "email");
	}
	
	
	
	static String authURL() {
	    return play.mvc.Router.getFullUrl("Application.auth");
	}
	
	public static void sair(){
		Login.logoff();
	}
	
	public static void post(String url) {
		FacebookClient cliente = new DefaultFacebookClient(token);
        FacebookType response =  cliente.publish("me/feed", FacebookType.class, Parameter.with("message", "Teste postagem com API (de novo)"));
        System.out.print("fb.com/"+response.getId());
	}

}
