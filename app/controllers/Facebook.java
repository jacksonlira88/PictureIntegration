package controllers;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
	
	public static void autenticar() {
		
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
	        Main.login();
	    }
	    FACEBOOK.retrieveVerificationCode(authURL(), "scope", "email");
	}
	
	static String authURL() {
	    return play.mvc.Router.getFullUrl("Facebook.autenticar");
	}
	
	public static void post(String url) throws SAXException, IOException, ParserConfigurationException {
		String token = "EAACEdEose0cBAOJZBqunwdkoRLiwstyv3YZBQk202QZAAMnGiZB57BtLCCSN6So1g49hU4sv4oiYio4lKE0abfS5UxNIciIuLWAA2yFVD3osw1UfxR655ZBReG6q4vgzMcFNXrrT5hG0ZBMnTz8FC8T4jX2d2VMzd3DyJ69JbtZBMZAhgKnqjsOBcZBbGR1o2FswZD";
		FacebookClient cliente = new DefaultFacebookClient(token);
        FacebookType response =  cliente.publish("me/feed", FacebookType.class, Parameter.with("message", url));
        System.out.print("fb.com/"+response.getId());
        Flickr.listarPhotos();
	}

}
