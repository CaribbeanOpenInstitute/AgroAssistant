package org.data.agroassistant;



/* DESC:	Activity to view the information of an individual farm. Shows that farm information as well as
 * 				associated crops
 * BUGS:	1)Clicking individual crop FCes application
 * TODO:	To refactor code to read from database rather than bundle with strings passed in.
 */

import java.util.ArrayList;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.DBConstants.*;

import java.util.List;

import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

public class FarmView extends TabActivity{
	private final String TAG = Farmers.class.getSimpleName();
	
	private String mResponseError = "Unknown Error";
	private String apiResponse;
	private String queryParams;
	private List<CropObj> crops;
	private final String FARM_TAB  = "farmdetails";
	private final String CROP_TAB  = "croplist";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    
	    Intent farmintent, cropintent;  // Reusable Intent for each tab
	    Intent farmdata = getIntent();  // Reusable Intent for each tab
	    
	    final Bundle farmbundle = farmdata.getExtras();
	    Bundle searchResultBundle = new Bundle();
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    farmintent = new Intent().setClass(this, FarmDetails.class);
	    farmintent.putExtras(farmbundle);
	    String farmID = farmbundle.getString(FARM_ID);
	    
	    queryParams = FARMS_TABLE + "." + FARM_ID + "=" + farmID;
	    
	    fetchCropData(farmID);	//Fetching farm data in the background
	    
	    cropintent = new Intent();
	    searchResultBundle.putString("response", apiResponse); // add return xml to bundle for next activity
		searchResultBundle.putInt("searchType", FARM_CROP_SEARCH);
		searchResultBundle.putString("searchParams", queryParams);
		cropintent.putExtras(searchResultBundle);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec(FARM_TAB).setIndicator("Farm",
	                      res.getDrawable(R.drawable.ic_menu_farm))
	                  .setContent(farmintent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    cropintent.setClass(this, ResultView.class);
	    spec = tabHost.newTabSpec(CROP_TAB).setIndicator("Crops",
	                      res.getDrawable(R.drawable.ic_menu_crop))
	                  .setContent(cropintent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	}
	
	private class apiRequest extends AsyncTask<ContentValues, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ContentValues... searchParams) {
			AgroApplication  agroApp = ((AgroApplication)getApplication());
			agroApp.getQueryData(CROPS_SEARCH, searchParams[0]);
			
			String queryParams = agroApp.queryParams;	//e.g "propertyid=10342342"
	    	Log.e(TAG, String.format("Query Param String: %s", queryParams));
			return queryParams;
			
			/*AgroAssistantDB agroDB = new AgroAssistantDB(FarmView.this);
			if (agroDB.queryExists(CROPS_TABLE, queryParams)) {
				Log.d("AgroAssistant","Crops query "+queryParams+" exists in DB");
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(CROPS_TABLE, queryParams);
		    	    Log.d("AgroAssistant","Crops query "+queryParams+" entered into DB");
		    	    agroDB.close();
		    	} catch (Exception e) {
		    		Log.e("AgroAssistant","Crops RESTServiceObj pull: "+e.toString());
		    	    return null;
		    	}
			}
	    	final String response = client[0].getResponse();
			return response;*/
		}
		
		@Override
		protected void onPostExecute(String apiResponse) {
			super.onPostExecute(apiResponse);
			
			/*if (apiResponse.equals(DB_SEARCH)) {
				//Does nothing
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){
    			//No crop information in the database
				//Toast.makeText(FarmView.this, "Error: No Farms Data retrieved", Toast.LENGTH_SHORT).show();
    		}else{
    			xmlParse parser = new xmlParse(FarmView.this, apiResponse);
    			parser.parseXML(CROPS_TABLE);
    		}*/
		}
	}
	
	private final void fetchCropData(String column) {
		ContentValues cropParams = new ContentValues();
		cropParams.put(FARM_ID, column);
		//RESTServiceObj client;
		//client = new RESTServiceObj(getString(R.string.CROPS_QUERY_URL));
		//client.AddParam("PropertyID", column);
		new apiRequest().execute(cropParams);
	}
}
