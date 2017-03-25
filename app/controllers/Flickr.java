package controllers;

import java.util.List;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
import play.libs.OAuth.ServiceInfo;
import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import play.mvc.results.Redirect;

public class Flickr extends Controller {
	private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";
	private static final String PHOTOS_URL = "http://www.flickr.com/photos/";
	private static OAuth1RequestToken requestToken;
	private static OAuth1AccessToken accessToken;

	final static OAuth10aService service = new ServiceBuilder()
			.apiKey("30e667f87d1ecbca4e18846dffff86a2")
			.apiSecret("961b22cca7d087a5")
			.callback("http://localhost:9000/flickr/autenticado")
			.build(FlickrApi.instance());

	public static void home() {
		render();
	}
	
	public static String getUrlPhoto(String photo_id) 
			throws IOException, ParserConfigurationException, SAXException {
		final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service.getConfig());
		request.addParameter("method", "flickr.photos.getInfo");
		request.addParameter("photo_id", photo_id);
		service.signRequest(accessToken, request);
		final Response response = service.execute(request);

		String xml = response.getBody();
		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		
		NodeList nList = doc.getElementsByTagName("photo");
		Node nNode = nList.item(0);
		String secret = null;
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			secret = eElement.getAttribute("secret");
		}
		
		String url = PHOTOS_URL+photo_id+"_"+secret+".jpg";

		return url;
	}

	public static List getIdsDePhotos() 
			throws IOException, SAXException, ParserConfigurationException {
		final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service.getConfig());
		request.addParameter("method", "flickr.people.getPhotos"); // flickr.photos.getInfo
		request.addParameter("user_id", accessToken.getParameter("user_nsid").replace("%40", "@"));
		service.signRequest(accessToken, request);
		final Response response = service.execute(request);

		String xml = response.getBody();
		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);

		NodeList nList = doc.getElementsByTagName("photo");
		List ids = new ArrayList<String>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ids.add(eElement.getAttribute("id"));
			}
		}
		return ids;
	}
	
	public static List atualizaURLs() 
			throws IOException, SAXException, ParserConfigurationException {
		List urls = new ArrayList<String>();
		for (int temp = 0; temp < getIdsDePhotos().size(); temp++) {
			urls.add(getUrlPhoto(getIdsDePhotos().get(temp).toString()));
		}
		
		return urls;
	}
 
	public static void listarPhotos() 
			throws SAXException, IOException, ParserConfigurationException {
		List listaUrlPhotos = atualizaURLs();
		render(listaUrlPhotos);
	}

	public static void autenticado(String oauth_token, String oauth_verifier)
			throws IOException, SAXException, ParserConfigurationException {
		// troca token de solicitação por token de acesso
		accessToken = service.getAccessToken(requestToken, oauth_verifier);
		Main.flickrAutenticado = true;
		listarPhotos();
	}
	
	public static void autenticar() throws IOException {
		// obtém token de solicitação
		requestToken = service.getRequestToken();
		// retorna URL de autorização
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		// adiciona tipo de permissão a URL de autorização
		authorizationUrl = authorizationUrl + "&perms=delete";
		redirect(authorizationUrl);
	}
}