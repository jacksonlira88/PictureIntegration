package controllers;

import com.google.gson.JsonObject;

import models.User;
import models.UsuarioF;
import play.Logger;
import play.libs.OAuth;
import play.libs.OAuth.ServiceInfo;
import play.libs.OAuth2;
import play.libs.WS;
import play.mvc.Controller;

public class Flicker extends Controller{

	 public static final ServiceInfo FLICKER = new ServiceInfo(
			 "https://www.flickr.com/services/oauth/request_token",
 			"https://www.flickr.com/services/oauth/authorize",
 			"https://www.flickr.com/services/oauth/access_token",
 			"961b22cca7d087a5",
 			"30e667f87d1ecbca4e18846dffff86a2"
	 );
	 
	 public static void home() {
		
		 // descomente a linha á baixo << renderText("vai lá omi"); >> e veja que a autenticação está realmente funcionando, 
		 //por hra vai ficar assim... depois colocamos uma view.
		 
		 //ou comente para autenticar e voltar para a página home sem uma resposta visual.
		 
		 
		renderText("vai lá omi");
		 
		Main.home();
	  }

	 public static void authenticate() {
	        
	        if (OAuth.isVerifierResponse()) {
	            // We got the verifier; now get the access token, store it and back to index
	        	String token = session.get("token");
	        	String secret = session.get("secret");
	        	
	            OAuth.Response oauthResponse = OAuth.service(FLICKER).retrieveAccessToken(token, secret);
	            if (oauthResponse.error == null) {
	                token = oauthResponse.token;
	                secret = oauthResponse.secret;
	            } else {
	                Logger.error("Error connecting to twitter: " + oauthResponse.error);
	            }
	            home();
	        }
	        OAuth twitt = OAuth.service(FLICKER);
	        OAuth.Response oauthResponse = twitt.retrieveRequestToken();
	        if (oauthResponse.error == null) {
	            // We received the unauthorized tokens in the OAuth object - store it before we proceed
	            
	        	session.put("token", oauthResponse.token);
	        	session.put("secret", oauthResponse.secret);
	            redirect(twitt.redirectUrl(oauthResponse.token));
	        } else {
	            Logger.error("Error connecting to flicker: " + oauthResponse.error);
	            home();
	        }
	    }

	 private static UsuarioF getUser() {
			return null;
	       
	 }
}