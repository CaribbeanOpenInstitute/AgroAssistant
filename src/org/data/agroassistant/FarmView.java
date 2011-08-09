package org.data.agroassistant;

import static org.data.agroassistant.Constants.FARM_CROP_SEARCH;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FarmView extends TabActivity{
	
	private String mResponseError = "Unknown Error", apiResponse, queryParams;
	private List<CropObj> crops;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_view);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    
	    Intent farmintent, cropintent;  // Reusable Intent for each tab
	    
	    Intent farmdata = getIntent();  // Reusable Intent for each tab
	    
	    Bundle farmbundle = farmdata.getExtras();
	    Bundle searchResultBundle = new Bundle();
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    farmintent = new Intent().setClass(this, FarmDetails.class);
	    farmintent.putExtras(farmbundle);
	    
	    
	    apiResponse = FetchCropData(farmbundle.getString("farmid"));
	    
	    crops = new ArrayList<CropObj>();
	    
	    crops = parseResponse(apiResponse);
	    
	    cropintent = new Intent();
	    searchResultBundle.putString("response", apiResponse); // add return xml to bundle for next activity
		searchResultBundle.putInt("searchType", FARM_CROP_SEARCH);
		searchResultBundle.putString("searchParams", queryParams);
		cropintent.putExtras(searchResultBundle);

		
		
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("farmdetails").setIndicator("Farms",
	                      res.getDrawable(R.drawable.ic_menu_farmer))
	                  .setContent(farmintent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    cropintent.setClass(this, ResultView.class);
	    spec = tabHost.newTabSpec("croplist").setIndicator("Crops",
	                      res.getDrawable(R.drawable.ic_menu_crop))
	                  .setContent(cropintent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	}
	
private final String FetchCropData(String column) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.CROPS_QUERY_URL));
    	
		client.AddParam("PropertyID", column);
    		
		
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
	
	private List<CropObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(FarmView.this, responseStr);
		String excptn = "";
		ArrayList<CropObj> list = new ArrayList<CropObj>();
		
		parser.parseXML("Crop");
		
		try{

			list = parser.getCropList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}
}
