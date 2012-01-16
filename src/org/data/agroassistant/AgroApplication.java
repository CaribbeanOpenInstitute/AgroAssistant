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
import static org.data.agroassistant.DBConstants.FARM_ID;
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
	int searchType;
	public String queryParams = "";
	public String apiResponse;
	String queryTable = "";
	
	
	//@Override
	/*public AgroApplication(Context ctx) {
		agroData = new AgroData(ctx);
		this.onCreate();
	}*/
	
	
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
	 * TODO:		Logic for DB vs API searching
	 */
	public void getQueryData(int table, ContentValues values) {
		searchType = table;
		queryParams = "";
		
		//Set correct API URI for the search
		switch(table) {	
		case FARMERS_SEARCH:	//Farmer ID or Farmer lastname or first anme last name
			client = new RESTServiceObj(FARMS_QUERY_URL);
			queryTable = FARMERS_TABLE;
			break;
		case FARMS_SEARCH:	//PropertyID
			client = new RESTServiceObj(FARMS_QUERY_URL);
			queryTable = FARMERS_TABLE;
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
		Log.e(TAG, "Query Param string" + queryParams);
		Log.e(TAG, String.format("Check that query: %s on table %s (%d)", queryParams, queryTable, table));
		if (agroData.queryExists(table, queryParams)) {
			apiRequest(true); //Pull from local DB
		} else {
			apiRequest(false); //Pull from API
		//new apiRequest().execute(client);
		}
	}
	
	/**
	 * apiRequest
	 * @param API_FLAG TRUE indicates query to API || FALSE indicates local DB query
	 */
	private void apiRequest(boolean DB_FLAG) {
		Intent searchResultIntent = new Intent();
		Bundle searchResultBundle = new Bundle();
		
		if (DB_FLAG) { //False means go to API Call
			//DB Call
		} else {
			try {
	    	    client.Execute(RESTServiceObj.RequestMethod.GET);
	    	    //Insert query params into db
	    	    //agroDB.insertQuery(FARMERS_TABLE, queryParams);	
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
		 
			//Call & pass necessary information to ResultView activity
			searchResultBundle.putInt("searchType", searchType);
			searchResultBundle.putString("searchParams", queryParams);
			searchResultIntent.putExtras(searchResultBundle);
		}
		
		/*//Pass query params and search type to ResultView
		searchResultBundle.putInt("searchType", searchType);
		searchResultBundle.putString("searchParams", queryParams);
		searchResultIntent.putExtras(searchResultBundle);

		searchResultIntent.setClass(AgroApplication.this, ResultView.class);
		startActivity(searchResultIntent);*/
	}
	/*
	private class apiRequest extends AsyncTask<RESTServiceObj, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			animator.setDisplayedChild(1);
		}

		@Override
		protected String doInBackground(RESTServiceObj... client) {
			AgroAssistantDB agroDB = new AgroAssistantDB(Farmers.this);
			if (agroDB.queryExists(FARMERS_TABLE, queryParams)) {
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(FARMERS_TABLE, queryParams);
		    	    agroDB.close();
		    	} catch (Exception e) {
		    	    e.printStackTrace();
		    	    return null;
		    	}
			}
	    	final String response = client[0].getResponse();
			return response;
		}
		
		@Override
		protected void onPostExecute(String apiResponse) {
			super.onPostExecute(apiResponse);
			Intent searchResultIntent = new Intent();
			Bundle searchResultBundle = new Bundle();
			
			if (apiResponse.equals(DB_SEARCH)) {
				/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
	/*
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Farmers.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){	//Checks if API for data and acts accordingly
    			Toast.makeText(Farmers.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
    			animator.setDisplayedChild(0);
    		}else{
				xmlParse parser = new xmlParse(Farmers.this, apiResponse);
				parser.parseXML(FARMERS_TABLE);
    			/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
	/*
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Farmers.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
    		}
		}
	}
	
	private void addNames(String fullName, RESTServiceObj client){
		String fname, lname;
		fullName = fullName.trim();

		int space = fullName.indexOf(' ');
		if(space < 1){
			client.AddParam("lastname", fullName);
		}else{

			fname = fullName.substring(0, space);
			lname= fullName.substring(space, fullName.length());
			lname = lname.trim();

			client.AddParam("firstname", fname);
			client.AddParam("lastname", lname);
		}
	}

	private void addAreaParam(String value, RESTServiceObj client){

		if(district.equals("") && extension.equals("")){
			client.AddParam("Parish", parish);

		}else if(parish.equals("") && district.equals("")){
			client.AddParam("Extension", extension);

		}else{
			client.AddParam("District", district);
		}
	}


	private void getAreaData(Intent intent){
		if(intent.getStringExtra("AreaCol").equals("Parish")){

			parish = intent.getStringExtra("Parish");
			Toast.makeText(Farmers.this, "Parish: "+ parish, Toast.LENGTH_SHORT).show();

		}else if(intent.getStringExtra("AreaCol").equals("Extension")) {

			extension = intent.getStringExtra("Extension");
			Toast.makeText(Farmers.this, "Extension "+ extension, Toast.LENGTH_SHORT).show();

		}else{

			district = intent.getStringExtra("District");
			Toast.makeText(Farmers.this, "district "+ district, Toast.LENGTH_SHORT).show();
		}
	}*/

}
