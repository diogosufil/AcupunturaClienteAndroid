package com.example.acupunturaclienteandroid.webutils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.acupunturaclienteandroid.model.SintomaWEB;
import com.example.acupunturaclienteandroid.model.UtilizadorWEB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class WebServiceUtils {

	public static String URL = "http://acupunturawcf.apphb.com/Service1.svc/REST/";
	public static HttpClient client = new DefaultHttpClient();

	public static String logIn(String username, String password)
			throws ClientProtocolException, IOException, RestClientException,
			JSONException {
		String token = "";

		HttpPost httpPost = new HttpPost(URL + "login?username=" + username
				+ "&password=" + password);

		BasicHttpResponse httpResponse = (BasicHttpResponse) client
				.execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = httpResponse.getEntity();
			token = EntityUtils.toString(entity);

		} else {
			throw new RestClientException(
					"HTTP Response with invalid status code "
							+ httpResponse.getStatusLine().getStatusCode()
							+ ".");
		}

		return token;

	}
	
	public static String logOut(String token)
			throws ClientProtocolException, IOException, RestClientException,
			JSONException {
		HttpPost httpPost = new HttpPost(URL + "logout?token=" + token);
		BasicHttpResponse httpResponse = (BasicHttpResponse) client
				.execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			
			return "Logout Successfull!";
			
		} else {
			throw new RestClientException(
					"HTTP Response with invalid status code "
							+ httpResponse.getStatusLine().getStatusCode()
							+ ".");
		}
	}

//	public static boolean isAdmin(String token) throws ClientProtocolException,
//			IOException, RestClientException, JSONException {
//		HttpGet request = new HttpGet(URL + "isAdmin?token=" + token);
//		//request.setHeader("Allow","GET");
//		request.setHeader("Accept", "Application/JSON");
//		
//		BasicHttpResponse basicHttpResponse = (BasicHttpResponse) client
//				.execute(request);
//		String response = EntityUtils.toString(basicHttpResponse.getEntity());
//
//		if (basicHttpResponse.getStatusLine().getStatusCode() == 200) {
//			if (response.equals("true")) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			throw new RestClientException(
//					"HTTP Response with invalid status code "
//							+ basicHttpResponse.getStatusLine().getStatusCode()
//							+ ".");
//		}
//	}

	public static ArrayList<UtilizadorWEB> getAllUtilizadores(String token)
			throws ClientProtocolException, IOException, RestClientException,
			JSONException {
		ArrayList<UtilizadorWEB> utilizadores = null;
		HttpGet request = new HttpGet(URL + "getAllUtilizadores?token=" + token);
		request.setHeader("Accept", "Application/JSON");

		BasicHttpResponse basicHttpResponse = (BasicHttpResponse) client
				.execute(request);

		if (basicHttpResponse.getStatusLine().getStatusCode() == 200) {
			utilizadores = new ArrayList<UtilizadorWEB>();

			Type collectionType = new TypeToken<ArrayList<UtilizadorWEB>>() {
			}.getType();
			Gson g = new Gson();
			utilizadores = g.fromJson(
					EntityUtils.toString(basicHttpResponse.getEntity()),
					collectionType);

		} else {
			throw new RestClientException(
					"HTTP Response with invalid status code "
							+ basicHttpResponse.getStatusLine().getStatusCode()
							+ ".");
		}

		return utilizadores;

	}
	
	public static ArrayList<SintomaWEB> getAllSintomas(String token)
			throws ClientProtocolException, IOException, RestClientException,
			JSONException {
		ArrayList<SintomaWEB> sintomas = null;
		HttpGet request = new HttpGet(URL + "getListaSintomasXml?token=" + token);
		request.setHeader("Accept", "Application/JSON");

		BasicHttpResponse basicHttpResponse = (BasicHttpResponse) client
				.execute(request);
		Gson g = new Gson();
		if (basicHttpResponse.getStatusLine().getStatusCode() == 200) {
			sintomas = new ArrayList<SintomaWEB>();
			Type collectionType = new TypeToken<ArrayList<SintomaWEB>>() {
			}.getType();
			
			sintomas = g.fromJson(EntityUtils.toString(basicHttpResponse.getEntity()),collectionType);

		} else {
			throw new RestClientException(
					"HTTP Response with invalid status code "
							+ basicHttpResponse.getStatusLine().getStatusCode()
							+ ".");
		}

		return sintomas;
	}

	public static ArrayList<String> getDiagnosticos(String token, ArrayList<SintomaWEB> listaSintomasSelecionados)
			throws ClientProtocolException, IOException, RestClientException,
			JSONException {
		ArrayList<String> diagnosticos = null;
		Gson g = new Gson();

		HttpPost request = new HttpPost(URL + "getListaDiagnosticosXml?token=" + token);
		//request.setHeader("Accept", "Application/JSON");
		
		Type collectionType = new TypeToken<ArrayList<SintomaWEB>>() {
		}.getType();
		
		StringEntity sEntity = new StringEntity(g.toJson(listaSintomasSelecionados, collectionType), "UTF-8");
		sEntity.setContentType("text/json");
		sEntity.setContentType("application/json;charset=UTF-8");
		request.setEntity(sEntity);
		
		HttpResponse httpResponse = (HttpResponse) client
				.execute(request);
		HttpEntity ent = httpResponse.getEntity();
		
		Log.i("error", EntityUtils.toString(ent));
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			diagnosticos = new ArrayList<String>();
			
			collectionType = new TypeToken<ArrayList<String>>() {
			}.getType();
			diagnosticos = g.fromJson(EntityUtils.toString(ent),collectionType);

		} else {
			throw new RestClientException(
					"HTTP Response with invalid status code "
							+ httpResponse.getStatusLine().getStatusCode()
							+ ".");
		}

		return diagnosticos;

	}
}
