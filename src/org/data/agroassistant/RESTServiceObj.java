package org.data.agroassistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import static org.data.agroassistant.Constants.FARMERS_TABLE;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class RESTServiceObj {

    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
 
    public enum RequestMethod { GET, POST };
    private String url;
 
    private int responseCode;
    private String message;
 
    private String response;
 
    public String getResponse() {
        return response;
    }
 
    public String getErrorMessage() {
        return message;
    }
    
    @Override
    public String toString() {
    	String combinedParams = "";
    	String paramString = "";
    	if(!params.isEmpty()){
            for(NameValuePair p : params)
            {
            	String paramValue = "";
            	try {
					paramValue = URLEncoder.encode(p.getValue(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				//Checks if the column variable is "farmerid". If so then it inserts the table name into param name. 
				//This prevents database error in JOIN
				Log.d("AgroAssistant", "RESTServiceObj: To string function. p.getName = " + p.getName());
				if (p.getName().equals("FarmerID"))
					paramString = FARMERS_TABLE + "." + p.getName() + "=" + "'" +paramValue + "'";
				else
					paramString = p.getName() + "=" + "'" +paramValue + "'";
				
                if(combinedParams.length() > 1)
                {
                    combinedParams  +=  " AND " + paramString;
                }
                else
                {
                    combinedParams += paramString;
                }
            }
        }
    	return combinedParams.toLowerCase();
    }
 
    public int getResponseCode() {
        return responseCode;
    }
 
    public RESTServiceObj(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }
 
    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }
 
    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }
 
    public void Execute(RequestMethod method) throws Exception
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }
 
                HttpGet request = new HttpGet(url + combinedParams);
 
                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }
 
                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);
 
                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }
 
                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
 
                executeRequest(request, url);
                break;
            }
        }
    }
 
    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();
 
        HttpResponse httpResponse;
 
        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
 
            HttpEntity entity = httpResponse.getEntity();
 
            if (entity != null) {
 
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
 
                // Closing the input stream will trigger connection release
                instream.close();
            }
 
        } catch (ClientProtocolException e)  {
        	message = e.getMessage();
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
        	message = e.getMessage();
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }
 
    private static String convertStreamToString(InputStream is) {
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
