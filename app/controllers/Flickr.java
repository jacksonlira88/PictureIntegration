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
import javax.naming.Context;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.HashedMap;
import org.xml.sax.SAXException;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.Request;

import models.User;
import models.UsuarioF;
import play.Logger;
import play.libs.OAuth;
//import play.libs.OAuth.Response;
import play.libs.OAuth.ServiceInfo;
import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import play.mvc.results.Redirect;

public class Flickr extends Controller {
	private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";
	private static OAuth1RequestToken requestToken;
	private static OAuth1AccessToken accessToken;
	
	final static String apiKey = "30e667f87d1ecbca4e18846dffff86a2";
    final static String apiSecret = "961b22cca7d087a5";
    final static OAuth10aService service = new ServiceBuilder()
            .apiKey(apiKey)
            .apiSecret(apiSecret)
            .callback("http://localhost:9000/flickr/autenticado")
            .build(FlickrApi.instance());

	public static void home() {
		Main.home();
	}
	
	public static void authenticate() throws IOException {
		requestToken = service.getRequestToken(); //obtém token de solicitação
		String authorizationUrl = service.getAuthorizationUrl(requestToken); //retorna URL de autorização
		authorizationUrl = authorizationUrl+"&perms=delete"; //adiciona tipo de permissão a URL de autorização
		redirect(authorizationUrl);
	}
	
	public static void autenticado(String oauth_token,String oauth_verifier) throws IOException {
		accessToken = service.getAccessToken(requestToken, oauth_verifier); //troca token de solicitação por token de acesso
		Main.home();
	}
}