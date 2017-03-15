package controllers;

import models.UsuarioF;
import play.libs.OAuth.ServiceInfo;
import play.mvc.Controller;

public class Flicker extends Controller{

	 public static final ServiceInfo FLICKER = new ServiceInfo(
			 "https://www.flickr.com/services/oauth/request_token",
 			"https://www.flickr.com/services/oauth/authorize",
 			"https://www.flickr.com/services/oauth/access_token",
 			"ad06142c0a4186ae",
 			"9b45fcc1964f29ad9869495f9a999b25"
	 );
	 
	 public static void index() {
	        
	  }

	 public static void setStatus(String status) throws Exception {
	       
	 }

	 public static void authenticate() {
	       
	 }

	 private static UsuarioF getUser() {
			return null;
	       
	 }

	 
}
