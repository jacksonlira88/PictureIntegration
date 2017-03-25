package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.services.oauth2.Oauth2Request;

import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http;
import sun.net.www.http.HttpCapture;

public class Google extends Controller{
	
	//private static final String NETWORK_NAME = "G+";
    //private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";
    final static String clientId = "1028226317407-jk5p36jspa1j0mltv8pdic5jov6vp4ak.apps.googleusercontent.com";
    final static String clientSecret = "OjA0dl8Q7SMqj0muXfrNOn5T";
    final static String secretState = "secret" + new Random().nextInt(999_999);
    
    final static OAuth20Service service = new ServiceBuilder()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .scope("https://www.googleapis.com/auth/plus.me") // SUBSTITUA-O PELO ESCOPO DESEJADO
            .state(secretState)
            .callback("http://localhost:9000/google/autenticado")
            .build(GoogleApi20.instance());
    
    
   
    public static void home() {
		render();
	}
    
    public static void auth(){
    	final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        additionalParams.put("prompt", "consent");
        String authorizationUrl = service.getAuthorizationUrl(additionalParams);
        
        redirect(authorizationUrl);
        //service.extractAuthorization(authorizationUrl);
          
    }
    
    
    public static void autenticado(String code) throws IOException{
    	OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Obteve o token de acesso!");											//Got the Access Token
        System.out.println("(Se o seu curioso se parece com isso: " + accessToken					//if your curious it looks like this
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        
        Main.home();
    }
    
	
}

