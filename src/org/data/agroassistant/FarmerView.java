package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.DBConstants.DB_SEARCH;
import static org.data.agroassistant.DBConstants.FARMERS_TABLE;
import static org.data.agroassistant.DBConstants.FARMER_ID;
import static org.data.agroassistant.DBConstants.FARMS_TABLE;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

/* DESC:	Activity to view the information of an individual farmer
 * BUGS: 	1)If the user clicks on the Farms tab before the data is completely pulled from the API then they won't get 
 * 				the full farmer information
 */

public class FarmerView extends TabActivity{
	private final String TAG = Farmers.class.getSimpleName();
	
	private String queryParams;
	private final String FARMER_TAB = "farmerInfo";
	private final String FARM_TAB  = "farmInfo";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.farmer_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent farmerIntent, farmIntent;  // Reusable Intent for each tab
	    Intent farmerData = getIntent(); //intent containing farmer data passed from the calling activity
	    Bundle searchResultBundle = new Bundle();	//Bundle pass back to result view
	    final Bundle farmerbundle = farmerData.getExtras(); 
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    farmerIntent = new Intent().setClass(this, FarmerDetails.class);
	    farmerIntent.putExtras(farmerbundle); 
	    
	    String farmerID = farmerbundle.getString(FARMER_ID);
	    fetchFarmData(farmerID);	//Fetching farm data in the background
	    
	    queryParams = FARMERS_TABLE + "." + FARMER_ID + "=" + farmerID;
	    farmIntent = new Intent();
		searchResultBundle.putInt(SEARCH_TYPE, FARMER_FARM_SEARCH);
		searchResultBundle.putString(SEARCH_PARAMS, queryParams);
		farmIntent.putExtras(searchResultBundle);

		
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec(FARMER_TAB).setIndicator("Farmer",
	                      res.getDrawable(R.drawable.ic_menu_farmer))
	                  .setContent(farmerIntent);
	    tabHost.addTab(spec);
	    //FARM Tab
	    farmIntent.setClass(this, ResultView.class);
	    spec = tabHost.newTabSpec(FARM_TAB).setIndicator("Farms",
	                      res.getDrawable(R.drawable.ic_menu_farm))
	                  .setContent(farmIntent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	    
	}
	
	
	
	private class apiRequest extends AsyncTask<ContentValues, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ContentValues... searchParams) {
			AgroApplication  agroApp = ((AgroApplication)getApplication());
			agroApp.getQueryData(FARMS_SEARCH, searchParams[0]);
			
	    	String queryParams = agroApp.queryParams;	//e.g "parish=St.ANN and district=balaclava"
	    	Log.e(TAG, String.format("Query Param String: %s", queryParams));
			return queryParams;
		}
		
		@Override
		protected void onPostExecute(String queryParams) {
			super.onPostExecute(queryParams);
		}
	}
	
	private final void fetchFarmData(String column) {
		ContentValues farmParams = new ContentValues();
		farmParams.put(FARMER_ID, column);
		new apiRequest().execute(farmParams);
	}
}
