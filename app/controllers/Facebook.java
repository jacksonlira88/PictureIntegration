package controllers;

import com.google.gson.JsonObject;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import models.User;
import play.libs.OAuth2;
import play.libs.WS;
import play.mvc.Controller;

public class Facebook extends Controller {

   	public static String token;
	
	public static OAuth2 FACEBOOK = new OAuth2(
            "https://graph.facebook.com/oauth/authorize",
            "https://graph.facebook.com/oauth/access_token",
            "566151883592285",
            "92a1ae36b2a00b92d7714b618fd6ae93"
    );
	
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
	        Main.home();
	    }
	    FACEBOOK.retrieveVerificationCode(authURL(), "scope", "email");
	}
	
	static String authURL() {
	    return play.mvc.Router.getFullUrl("Facebook.auth");
	}
	
	public static void post(String url) {
		//String token = "EAACEdEose0cBAKyNV42CGk5RbQbLEb8z3Awit3rWuwWYeNlDZCKenVQZCPFxv0BBF9kB00npd9lZBdlSCbVF95W2kT7VSTGgdnn9aBZAAPmFPhkyJOZBxWSYqxMGkZCZBARbnWiycvqNMe5vzD3ZC8GkRFZBGMKCZCiXlu695M0aieRCjOG5S6vy9Se32jXNRDoe4ZD";
		FacebookClient cliente = new DefaultFacebookClient(token);
        FacebookType response =  cliente.publish("me/feed", FacebookType.class, Parameter.with("message", "http://images.esoterikha.com/natal/decoracao-de-natal-para-comercio.jpg"));
        System.out.print("fb.com/"+response.getId());
	}

}
