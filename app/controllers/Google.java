package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;


import play.libs.OAuth2;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http;
import sun.net.www.http.HttpCapture;
import sun.print.resources.serviceui;


import com.google.gson.JsonObject;


public class Google extends Controller{
	
	//private static final String NETWORK_NAME = "G+";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";
    final static String clientId = "1028226317407-jk5p36jspa1j0mltv8pdic5jov6vp4ak.apps.googleusercontent.com";
    final static String clientSecret = "FvTQgY6WHThcH09zlwcGCxjj";
    final static String secretState = "secret" + new Random().nextInt(999_999);
    private static OAuth2AccessToken accessToken;
    final static OAuth20Service service = new ServiceBuilder()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .scope("https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.stream.write https://www.googleapis.com/auth/plus.stream.read") // SUBSTITUA-O PELO ESCOPO DESEJADO
            .state(secretState)
            .callback("http://localhost:9000/google/autenticado")
            .build(GoogleApi20.instance());
   
    
   
    public static void home() {
		render();
		
		
	}
    
    public static void autenticar(){
    	final Map<String, String> additionalParams = new HashMap<>(); 
        additionalParams.put("access_type", "offline");
        additionalParams.put("prompt", "consent");
        String authorizationUrl = service.getAuthorizationUrl(additionalParams);
        
        redirect(authorizationUrl);
        //service.extractAuthorization(authorizationUrl);
        
    }
    
    
    public static void autenticado(String code) throws IOException, InterruptedException, ExecutionException{
    	accessToken = service.getAccessToken(code);
        System.out.println("Obteve o token de acesso!");											//Got the Access Token
        System.out.println("(Se o seu curioso se parece com isso: " + accessToken					//if your curious it looks like this
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        postar();
        Main.home();
    }
    
    
    public static void postar(){
    	
    	String requestUrl =  PROTECTED_RESOURCE_URL + "alt=json";
    	final OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
        service.signRequest(accessToken, request);
        try {
        	Response response = service.execute(request);
        	System.out.println(response.getCode());
        	System.out.println(response.getBody());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/*
    	JsonObject me = null;
    	try {
    	 me = WS.url("GET https://www.googleapis.com/plus/v1/people/me").get().getJson().getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	String email = me.get("email").getAsString();
        String id = me.get("id").getAsString();
        System.out.println(email);
        System.out.println(id);
       */
    }
    	
    	
    	
}
	

