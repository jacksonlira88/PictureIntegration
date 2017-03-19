package controllers;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.HashedMap;
import org.xml.sax.SAXException;

import com.aetrion.flickr.*;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.AuthUtilities;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.contacts.ContactsInterface;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.photosets.PhotosetsInterface;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;
import com.google.gson.JsonObject;
import com.ning.http.client.Request;

import models.User;
import models.UsuarioF;
import okhttp3.OkHttpClient;
import play.Logger;
import play.libs.OAuth;
import play.libs.OAuth.Response;
import play.libs.OAuth.ServiceInfo;
import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class Flicker extends Controller {

	public static String frob;
	public static String token;
	public static String apiKey = "30e667f87d1ecbca4e18846dffff86a2";
	public static String sharedSecret = "961b22cca7d087a5";
	public static Permission permission = Permission.DELETE;
	
	private Transport transportAPI;

	public static ServiceInfo service = new ServiceInfo("https://www.flickr.com/services/oauth/request_token",
			"https://www.flickr.com/services/oauth/authorize", "https://www.flickr.com/services/oauth/access_token",
			"9b45fcc1964f29ad9869495f9a999b25", "ad06142c0a4186ae");

	// public static OAuth FLICKER = new OAuth(new ServiceInfo(
	// "https://www.flickr.com/services/oauth/request_token",
	// "https://www.flickr.com/services/oauth/authorize",
	// "https://www.flickr.com/services/oauth/access_token",
	// "9b45fcc1964f29ad9869495f9a999b25", "ad06142c0a4186ae"));
	// jacksonlira
	// apikey: 9b45fcc1964f29ad9869495f9a999b25
	// secretkey: ad06142c0a4186ae

	// walter/santiago
	// apikey: 30e667f87d1ecbca4e18846dffff86a2
	// secretkey: 961b22cca7d087a5

	public static void home() {

		// descomente a linha á baixo << renderText("vai lá omi"); >> e veja que
		// a autenticação está realmente funcionando,
		// por hra vai ficar assim... depois colocamos uma view.

		// ou comente para autenticar e voltar para a página home sem uma
		// resposta visual.

		renderText("vai lá omi");

		Main.home();
	}
	
	public static void authenticate() {
		Flickr f;
		try {
			f = new Flickr(apiKey, sharedSecret, (new REST()));
			RequestContext requestContext = RequestContext.getRequestContext();
			AuthInterface authInterface = f.getAuthInterface();

			requestContext.setSharedSecret(sharedSecret);

			frob = "72157678124925404-eac01459af0e50e6-229217";
			//frob = authInterface.getFrob();
			System.out.println(frob);
			URL joep = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
			redirect(joep.toString());
			authInterface.getToken(frob);
			System.out.println(token);
			System.out.println(joep.toExternalForm());
			System.out.println("Press return after you granted access at this URL:");
			
			try {
				Auth auth = new Auth();
				requestContext.setAuth(auth);
				// authInterface.addAuthToken();
				authInterface.getToken(frob);
				auth.setToken(token);
				System.out.println(token);
				auth.setPermission(permission);
				System.out.println("Token Is: " + auth.getToken());
				System.out.println("Permission for token: " + auth.getPermission());
				f.setAuth(auth);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (FlickrException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException | IOException | SAXException | FlickrException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*public static void usuario() {
		String token = session.get("token");
		String secret = session.get("secret");

		System.out.println("1" + token + "\n" + secret);

		Map<String, Object> parametros = new HashMap<String, Object>();
		// parametros.put("api_key", FLICKER.consumerKey);
		parametros.put("method", "flickr.people.findByUsername");
		parametros.put("username", "walter/santiago");
		parametros.put("format", "json");
		parametros.put("nojsoncallback", 1);

		HttpResponse resp = WS.url(" https://api.flickr.com/services/rest/").params(parametros).post();
		JsonObject me = resp.getJson().getAsJsonObject();
		renderText(me.get("user").getAsJsonObject().get("id").getAsString());
	}
	
	private static String getSignature(String key, String data) {
		final String HMAC_ALGORITHM = "HmacSHA1";
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);
		Mac macInstance = null;
		try {
			macInstance = Mac.getInstance(HMAC_ALGORITHM);
			macInstance.init(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] signedBytes = macInstance.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(signedBytes);
	}
	
	private static String oauthEncode(String input) {
		Map<String, String> oathEncodeMap = new HashMap<>();
		oathEncodeMap.put("\\*", "%2A");
		oathEncodeMap.put("\\+", "%20");
		oathEncodeMap.put("%7E", "~");
		String encoded = "";
		 
		try {
			encoded = URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 
		for (Map.Entry<String, String> entry : oathEncodeMap.entrySet()) {
			encoded = encoded.replaceAll(entry.getKey(), entry.getValue());
		}
		
		return encoded;
	}

	public static void authenticate() {
		String baseString1 = "GET";
			
		String requestTokenUrl = "http://www.flickr.com/services/oauth/request_token";
		String baseString2 = oauthEncode(requestTokenUrl);
		
		String nonce = "flickr_oauth" + String.valueOf(System.currentTimeMillis());
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String callbackParam = "oauth_callback=" + oauthEncode("some_ callback_url}");
		String apiKeyParam = "oauth_consumer_key=" + apiKey; //your apiKey from flickr
		String nonceParam = "oauth_nonce=" + nonce;
		String signatureMethodParam = "oauth_signature_method=" + "HMAC-SHA1";
		String timestampParam = "oauth_timestamp=" + timestamp;
		String versionParam = "oauth_version=" + "1.0";
		String unencBaseString3 = callbackParam + "&" + apiKeyParam + "&" + nonceParam + "&" + signatureMethodParam + "&" + timestampParam + "&" + versionParam;
		String baseString3 = oauthEncode(unencBaseString3);
		
		String baseString = baseString1 + "&" + baseString2 + "&" + baseString3;
		
		String signature = getSignature(apiKeyParam, baseString);
		String signatureParam = "oauth_signature=" + oauthEncode(signature);
		
		String url = requestTokenUrl + "?" + callbackParam + "&" + apiKeyParam + "&" + nonceParam + "&" + timestampParam + "&" + signatureMethodParam + "&" + versionParam + "&" + signatureParam;
		
		redirect(url);
	}

	static String authURL() {
		return play.mvc.Router.getFullUrl("Flicker.home");
	}

	/*
	 * public static void authenticate() {
	 * 
	 * if (OAuth.isVerifierResponse()) { // We got the verifier; now get the
	 * access token, store it and back // to index String token =
	 * session.get("token"); String secret = session.get("secret");
	 * 
	 * System.out.println("2"+token+"\n"+secret);
	 * 
	 * OAuth.Response oauthResponse =
	 * OAuth.service(FLICKER).retrieveAccessToken(token, secret); if
	 * (oauthResponse.error == null) { token = oauthResponse.token; secret =
	 * oauthResponse.secret;
	 * 
	 * System.out.println("3"+token+"\n"+secret); } else { Logger.error(
	 * "Error connecting to twitter: " + oauthResponse.error); } home(); } OAuth
	 * twitt = OAuth.service(FLICKER); OAuth.Response oauthResponse =
	 * twitt.retrieveRequestToken(); if (oauthResponse.error == null) { // We
	 * received the unauthorized tokens in the OAuth object - store // it before
	 * we proceed
	 * 
	 * System.out.println("4"+oauthResponse.token);
	 * 
	 * session.put("token", oauthResponse.token); session.put("secret",
	 * oauthResponse.secret); redirect(twitt.redirectUrl(oauthResponse.token));
	 * } else { System.out.println("5"+oauthResponse.toString()); Logger.error(
	 * "Error connecting to flicker: " + oauthResponse.error); //home(); }
	 * usuario(); }
	 */

	private static UsuarioF getUser() {
		return null;

	}
}