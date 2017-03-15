package controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.google.gson.JsonObject;

import models.User;
import models.UsuarioF;
import play.Logger;
import play.libs.OAuth;
import play.libs.OAuth.ServiceInfo;
import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class Flicker extends Controller {

	public static final ServiceInfo FLICKER = new ServiceInfo("https://www.flickr.com/services/oauth/request_token",
			"https://www.flickr.com/services/oauth/authorize", "https://www.flickr.com/services/oauth/access_token",
			"9b45fcc1964f29ad9869495f9a999b25", "ad06142c0a4186ae");

	public static void home() {

		// descomente a linha á baixo << renderText("vai lá omi"); >> e veja que
		// a autenticação está realmente funcionando,
		// por hra vai ficar assim... depois colocamos uma view.

		// ou comente para autenticar e voltar para a página home sem uma
		// resposta visual.

		renderText("vai lá omi");

		Main.home();
	}

	public static void usuario() {
		String token = session.get("token");
     	String secret = session.get("secret");
     	
     	Map<String, Object> parametros = new HashMap<String, Object>();
     	parametros.put("api_key", FLICKER.consumerKey);
     	parametros.put("method", "flickr.people.findByUsername");
     	parametros.put("username", "jacksonlira");
     	parametros.put("format", "json");
     	parametros.put("nojsoncallback", 1);
     	
     	HttpResponse resp = WS.url(" https://api.flickr.com/services/rest/").params(parametros).post();
     	JsonObject me = resp.getJson().getAsJsonObject();
		renderText(me.get("user").getAsJsonObject().get("id").getAsString());
	}

	public static void authenticate() {

		if (OAuth.isVerifierResponse()) {
			// We got the verifier; now get the access token, store it and back
			// to index
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
			// We received the unauthorized tokens in the OAuth object - store
			// it before we proceed

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