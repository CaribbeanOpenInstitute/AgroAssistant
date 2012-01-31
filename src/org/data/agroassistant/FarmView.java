package org.data.agroassistant;

/**
 * DESC:	Activity to view the information of an individual farm. Shows that farm information as well as
 * 				associated crops
 * 
 * TODO:	Implement farm details edits and updating
 * TODO:	Farm data pulled on UI thread.
 */

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.DBConstants.FARMS_TABLE;
import static org.data.agroassistant.DBConstants.FARM_ID;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class FarmView extends TabActivity{
	private final String TAG = Farmers.class.getSimpleName();
	
	private String apiResponse;
	private String queryParams;
	private final String FARM_TAB  = "farmdetails";
	private final String CROP_TAB  = "croplist";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    
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
		searchResultBundle.putInt(SEARCH_CODE, FARM_CROP_SEARCH);
		searchResultBundle.putString(SEARCH_PARAMS, queryParams);
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
			
		}
		
		@Override
		protected void onPostExecute(String apiResponse) {
			super.onPostExecute(apiResponse);
			
		}
	}
	
	private final void fetchCropData(String column) {
		ContentValues cropParams = new ContentValues();
		cropParams.put(FARM_ID, column);
		new apiRequest().execute(cropParams);
	}
}
