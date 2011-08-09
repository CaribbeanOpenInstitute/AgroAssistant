package org.data.agroassistant;

import static org.data.agroassistant.Constants.*;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FarmerView extends TabActivity{
	
	private String mResponseError = "Unknown Error", apiResponse, queryParams;
	private List<FarmObj> farms;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.farmer_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    
	    Intent farmerintent, farmintent;  // Reusable Intent for each tab
	    
	    Intent farmerdata = getIntent(); //intent containing farmer data passed from the calling activity
	    
	    Bundle farmerbundle = farmerdata.getExtras();
	    Bundle searchResultBundle = new Bundle();
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    farmerintent = new Intent().setClass(this, FarmerInfo.class);
	    farmerintent.putExtras(farmerbundle);
	    
	    
	    apiResponse = fetchFarmData(farmerbundle.getString("farmerid"));
	    
	    farms = new ArrayList<FarmObj>();
	    
	    farms = parseResponse(apiResponse);
	    
	    farmintent = new Intent();
	    searchResultBundle.putString("response", apiResponse); // add return xml to bundle for next activity
		searchResultBundle.putInt("searchType", FARMER_FARM_SEARCH);
		searchResultBundle.putString("searchParams", queryParams);
		farmintent.putExtras(searchResultBundle);

		
		
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("farmerInfo").setIndicator("Farmer",
	                      res.getDrawable(R.drawable.ic_menu_farmer))
	                  .setContent(farmerintent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    farmintent.setClass(this, ResultView.class);
	    spec = tabHost.newTabSpec("farmInfo").setIndicator("Farms",
	                      res.getDrawable(R.drawable.ic_menu_farm))
	                  .setContent(farmintent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
	
	private final String fetchFarmData(String column) {
		
		 RESTServiceObj client;
		 
			 client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));
		   	
		
       		client.AddParam("FarmerID", column);
       	
		
   	try {
   	    client.Execute(RESTServiceObj.RequestMethod.GET);
   	} catch (Exception e) {
   	    e.printStackTrace();
   	    mResponseError = client.getErrorMessage();
   	    return null;
   	}
   	final String response = client.getResponse();
   	
   	if (response == null)
   		mResponseError = client.getErrorMessage();
   	 
   		queryParams = client.toString();
		return response;
   }
	
	private List<FarmObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(this, responseStr);
		String excptn = "";
		ArrayList<FarmObj> list = new ArrayList<FarmObj>();
		
		parser.parseXML("Farm");
		
		try{

			list = parser.getFarmList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}
}
