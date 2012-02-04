package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.FARMERS_SEARCH;
import static org.data.agroassistant.AgroConstants.FARMS_SEARCH;
import static org.data.agroassistant.AgroConstants.PRICES_SEARCH;
import static org.data.agroassistant.DBConstants.CROPS_TABLE;
import static org.data.agroassistant.DBConstants.CROP_GROUP;
import static org.data.agroassistant.DBConstants.CROP_TYPE;
import static org.data.agroassistant.DBConstants.FARMERS_TABLE;
import static org.data.agroassistant.DBConstants.FARMER_FNAME;
import static org.data.agroassistant.DBConstants.FARMER_ID;
import static org.data.agroassistant.DBConstants.FARMER_LNAME;
import static org.data.agroassistant.DBConstants.FARM_DISTRICT;
import static org.data.agroassistant.DBConstants.FARM_EXTENSION;
import static org.data.agroassistant.DBConstants.*;
import static org.data.agroassistant.DBConstants.FARM_LAT;
import static org.data.agroassistant.DBConstants.FARM_LONG;
import static org.data.agroassistant.DBConstants.FARM_PARISH;
import static org.data.agroassistant.DBConstants.PRICES_TABLE;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * AgroApplication class used to manage all data calls to the API and database
 * @author Matthew McNaughton
 *
 */
public class AgroApplication extends Application implements OnSharedPreferenceChangeListener {
	private static final String TAG = AgroApplication.class.getSimpleName();
	
	private SharedPreferences prefs;
	public AgroData agroData;
	Context appCtx;
	
	private RESTServiceObj client;
	int tableCode;
	public String queryParams = "";
	public String apiResponse;
	String queryTable = "";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		appCtx = this.getApplicationContext();
		
		agroData = new AgroData(this);
				
	}
	
	/**
	 * TODO Implement with Username, Password and API key
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		//Authenticate user and check for new API key
	}
	
	/**
	 * 
	 * @param table		Table int constant being searched 
	 * @param values	Name/Pair pair of params being searched
	 * TODO:		Farm searches that have crop level parameters
	 */
	public void getQueryData(int table, ContentValues values) {
		tableCode = table;
		queryParams = "";
		
		//Set correct API URI for the search
		switch(tableCode) {	
		case FARMERS_SEARCH:	//Farmer ID or Farmer lastname or first anme last name
			client = new RESTServiceObj(FARMS_QUERY_URL);
			queryTable = FARMERS_TABLE;
			break;
		case FARMS_SEARCH:	//PropertyID
			client = new RESTServiceObj(FARMS_QUERY_URL);
			queryTable = FARMS_TABLE;
			break;
		case CROPS_SEARCH:
			client = new RESTServiceObj(CROPS_QUERY_URL);
			queryTable = CROPS_TABLE;
			break;
		case PRICES_SEARCH:
			client = new RESTServiceObj(PRICES_QUERY_URL);
			queryTable = PRICES_TABLE;
			break;
		default:
			return;
		}
		
		//Farmer ID or Farmer First name + Last name or Last name
		if(values.containsKey(FARMER_ID)) {
			client.AddParam(FARMER_ID, values.getAsString(FARMER_ID));
		} else {
			if(values.containsKey(FARMER_LNAME)) {
				client.AddParam(FARMER_LNAME, values.getAsString(FARMER_LNAME));
				if(values.containsKey(FARMER_FNAME))
					client.AddParam(FARMER_FNAME, values.getAsString(FARMER_FNAME));
			}
		}
		
		//Farm Search
		if(values.containsKey(FARM_ID)) { //Parish ID
			client.AddParam(FARM_ID, values.getAsString(FARM_ID));
		}
		
		//Area Search
		if (values.containsKey(FARM_PARISH))
			client.AddParam(FARM_PARISH, values.getAsString(FARM_PARISH));
		else if (values.containsKey(FARM_EXTENSION))
			client.AddParam(FARM_EXTENSION, values.getAsString(FARM_EXTENSION));
		else if (values.containsKey(FARM_DISTRICT))
			client.AddParam(FARM_DISTRICT, values.getAsString(FARM_DISTRICT));
			
		//Location Search
		if (values.containsKey(FARM_LAT)) {
			client.AddLocation(values.getAsString(FARM_LAT), values.getAsString(FARM_LONG));
		}
		
		//Crop Group or Type Search
		if(values.containsKey(CROP_GROUP)) {
			client.AddParam(CROP_GROUP, values.getAsString(CROP_GROUP));
		} else if(values.containsKey(CROP_TYPE)) {
			client.AddParam(CROP_TYPE, values.getAsString(CROP_TYPE));
		}
		
		queryParams = client.toString();
		Log.e(TAG, "Query Params " + queryParams);
		Log.e(TAG, String.format("Check that query: %s on table %s", queryParams, queryTable));
		if (agroData.queryExists(tableCode, queryParams)) {
			apiRequest(true); //Pull from local DB
		} else {
			apiRequest(false); //Pull from API
		}
	}
	
	/**
	 * apiRequest
	 * @param API_FLAG TRUE indicates query to API || FALSE indicates local DB query
	 */
	private void apiRequest(boolean DB_FLAG) {
		Intent searchResultIntent = new Intent();
		Bundle searchResultBundle = new Bundle();
		
		if (!DB_FLAG) { //False means go to API Call
			try {
				Log.e(TAG, "API PULL");
	    	    client.Execute(RESTServiceObj.RequestMethod.GET);
	    	    //Insert query params into db
	    	    agroData.insertQuery(tableCode, queryParams);	
	    	} catch (Exception e) {
	    		Log.d(TAG, String.format("apiRequest Exception: %s", e.toString()));
	    	}
    		apiResponse = client.getResponse();
			
    		//Checks if API for data and acts accordingly
			if((apiResponse != null) && (apiResponse.contains("Parish"))){	//Contains data	
				xmlParse parser = new xmlParse(AgroApplication.this, apiResponse);
				parser.parseXML(queryTable);
    		} else {
    			Log.d(TAG, "API Response containts no data");
    		}
		 
		}
		//Call & pass necessary information to ResultView activity
		searchResultBundle.putInt("searchType", tableCode);
		searchResultBundle.putString("searchParams", queryParams);
		searchResultIntent.putExtras(searchResultBundle);
	}
	
	public static String UppercaseFirstLetters(String str) 
	{
	    boolean prevWasWhiteSp = true;
	    char[] chars = str.toCharArray();
	    for (int i = 0; i < chars.length; i++) {
	        if (Character.isLetter(chars[i])) {
	            if (prevWasWhiteSp) {
	                chars[i] = Character.toUpperCase(chars[i]);    
	            }
	            prevWasWhiteSp = false;
	        } else {
	            prevWasWhiteSp = Character.isWhitespace(chars[i]);
	        }
	    }
	    return new String(chars);
	}

	public static String getTableName(int tableCode) {
		switch(tableCode) {
		case FARMERS_SEARCH:
			return FARMERS_TABLE;
		case FARMS_SEARCH:
			return FARMS_TABLE;
		case CROPS_SEARCH:
			return CROPS_TABLE;
		case PRICES_SEARCH:
			return PRICES_TABLE;
		}
		return null;
	}
	
	public static int getTableCode(String tableName) {
		if (tableName.equals(FARMERS_TABLE))
			return FARMERS_SEARCH;
		else if (tableName.equals(FARMS_TABLE))
			return FARMS_SEARCH;
		else if (tableName.equals(CROPS_TABLE))
			return CROPS_SEARCH;
		else if (tableName.equals(PRICES_TABLE))
			return PRICES_SEARCH;
		
		return -1;
	}

}
