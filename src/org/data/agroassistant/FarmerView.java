package org.data.agroassistant;

import android.app.TabActivity;
import static org.data.agroassistant.DBConstants.*;

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
	
	private String mResponseError;
	private String queryParams;
	private String FARMER_TAB = "farmerInfo";
	private String FARM_TAB  = "farmInfo";
	
	@Override
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
	    farmerintent = new Intent().setClass(this, FarmerDetails.class);
	    farmerintent.putExtras(farmerbundle); 
	    
	    queryParams = FARMERS_TABLE + "." + FARMER_ID + "=" + farmerbundle.getString("farmerid");
	    
	    fetchFarmData(farmerbundle.getString("farmerid"));	//Fetching farm data in the background
	    
	    
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
	    
	}
	
	
	
	private class apiRequest extends AsyncTask<RESTServiceObj, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//animator.setDisplayedChild(1);
		}

		@Override
		protected String doInBackground(RESTServiceObj... client) {
			AgroAssistantDB agroDB = new AgroAssistantDB(FarmerView.this);
			if (agroDB.queryExists(FARMS_TABLE, queryParams)) {
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(FARMS_TABLE, queryParams);
		    	    agroDB.close();
		    	} catch (Exception e) {
		    		Log.e("AgroAssistant","Farms RESTServiceObj pull: "+e.toString());
		    	    return null;
		    	}
			}
	    	final String response = client[0].getResponse();
			return response;
		}
		
		@Override
		protected void onPostExecute(String apiResponse) {
			super.onPostExecute(apiResponse);
			//animator.setDisplayedChild(0);
			
			if (apiResponse.equals(DB_SEARCH)) {
				//Does nothing
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){
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
