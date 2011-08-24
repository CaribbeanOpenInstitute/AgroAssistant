package org.data.agroassistant;

import static org.data.agroassistant.Constants.FARMERS_TABLE;
import static org.data.agroassistant.Constants.FARMER_FARM_SEARCH;
import static org.data.agroassistant.Constants.FARMER_ID;
import static org.data.agroassistant.Constants.FARMS_TABLE;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

/*
 * BUG: If the user clicks on the Farms tab before the data is completely pulled from the API then they won't get 
 * 			the full farmer information
 */

public class FarmerView extends TabActivity{
	
	private String mResponseError, queryParams;
	private String FARMER_TAB = "farmerInfo";
	private String FARM_TAB  = "farmInfo";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.farmer_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    
	    Intent farmerintent, farmintent;  // Reusable Intent for each tab
	    
	    Intent farmerdata = getIntent(); //intent containing farmer data passed from the calling activity
	    
	    final Bundle farmerbundle = farmerdata.getExtras(); 
	    Bundle searchResultBundle = new Bundle();
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    farmerintent = new Intent().setClass(this, FarmerInfo.class);
	    farmerintent.putExtras(farmerbundle); 
	    
	    
	    fetchFarmData(farmerbundle.getString("farmerid"));	//Fetching farm data in the background
	    
	    queryParams = FARMERS_TABLE + "." + FARMER_ID + "=" + farmerbundle.getString("farmerid");
	    
	    
	    farmintent = new Intent();
		searchResultBundle.putInt("searchType", FARMER_FARM_SEARCH);
		searchResultBundle.putString("searchParams", queryParams);
		farmintent.putExtras(searchResultBundle);

		
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec(FARMER_TAB).setIndicator("Farmer",
	                      res.getDrawable(R.drawable.ic_menu_farmer))
	                  .setContent(farmerintent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    farmintent.setClass(this, ResultView.class);
	    spec = tabHost.newTabSpec(FARM_TAB).setIndicator("Farms",
	                      res.getDrawable(R.drawable.ic_menu_farm))
	                  .setContent(farmintent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	    
	    /*
	    tabHost.setOnTabChangedListener(new OnTabChangeListener(){
	    	//@Override
	    	public void onTabChanged(String tabId) {
	    	    if(  FARMER_TAB.equals(tabId)) {
	    	    	Log.d("AgroAssistant","FarmerView: Clicked on Farmer Info tab");	
	    	    	//tabHost.setCurrentTab(0);
	    	        //destroy earth
	    	    }
	    	    if(FARM_TAB.equals(tabId)) {
	    	    	Log.d("AgroAssistant","FarmerView: Clicked on Farm Info tab");
	    	    	
	    	    	//fetchFarmData(farmerbundle.getString("farmerid"));	
	    	    	
	    	    	//Fetching farm data in the background
	    	    	RESTServiceObj client;
	    			client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));
	    			client.AddParam("FarmerID", "Farmerid");
	    			while ( (new apiRequest().execute(client)).getStatus() != AsyncTask.Status.FINISHED ){
	    			
	    			}
	    	    	
	    			//while(fetchFarms.getStatus() != AsyncTask.Status.FINISHED) {
	    				
	    			//}
	    	    	//tabHost.setCurrentTab(1);
	    	        //destroy mars
	    	    }
	    	}});
		*/
	    
	}
	
	
	
	private class apiRequest extends AsyncTask<RESTServiceObj, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//animator.setDisplayedChild(1);
		}

		@Override
		protected String doInBackground(RESTServiceObj... client) {
			//if (db.queryExists(client.toString) {
			//pull from DB
			//else
		    	try {	//Check here if Query in database
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	} catch (Exception e) {
		    	    e.printStackTrace();
		    	    mResponseError = client[0].getErrorMessage();
		    	    return null;
		    	}
	    	final String response = client[0].getResponse();
	    	if (response == null)
	    		mResponseError = client[0].getErrorMessage();
			return response;
		}
		
		@Override
		protected void onPostExecute(String apiResponse) {
			super.onPostExecute(apiResponse);
			//animator.setDisplayedChild(0);
			
			//Checks if API for data
    		if((apiResponse == null) || !(apiResponse.contains("Parish"))){
    			Toast.makeText(FarmerView.this, "Error: No Farms Data retrieved", Toast.LENGTH_SHORT).show();
    		}else{
    			xmlParse parser = new xmlParse(FarmerView.this, apiResponse);
    			parser.parseXML(FARMS_TABLE);
    		}
		}
	}
	
	private final void fetchFarmData(String column) {
		RESTServiceObj client;
		client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));
		client.AddParam("FarmerID", column);
		new apiRequest().execute(client);
	}
}
