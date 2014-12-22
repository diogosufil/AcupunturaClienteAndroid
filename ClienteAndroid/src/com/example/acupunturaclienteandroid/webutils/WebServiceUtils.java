package com.example.acupunturaclienteandroid.webutils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
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

import com.example.acupunturaclienteandroid.model.UtilizadorWEB;

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

    public static ArrayList<UtilizadorWEB> getAllUtilizadores(String token)
            throws ClientProtocolException, IOException, RestClientException, JSONException {
        ArrayList<UtilizadorWEB> equipas = null;
      HttpGet request = new HttpGet(URL + "getAllUtilizadores?token=" + token);
                request.setHeader("Accept", "Application/JSON");

        BasicHttpResponse basicHttpResponse = (BasicHttpResponse) client
                .execute(request);

        if (basicHttpResponse.getStatusLine().getStatusCode() == 200) {
            equipas = new ArrayList<UtilizadorWEB>();

            Type collectionType = new TypeToken<ArrayList<UtilizadorWEB>>(){}.getType();
            Gson g = new Gson();
            equipas = g.fromJson(EntityUtils.toString(basicHttpResponse.getEntity()),collectionType);


        } else {
            throw new RestClientException(
                    "HTTP Response with invalid status code "
                            + basicHttpResponse.getStatusLine().getStatusCode()
                            + ".");
        }

        return equipas;

    }

}
