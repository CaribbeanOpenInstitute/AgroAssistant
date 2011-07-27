/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.data.agroassistant;

//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 *
 * @author Gebre
 */
public class Farms extends ListActivity{

	private static final int FNAME_SEARCH = 0;
	private static final int AREA_SEARCH = 1;
	private static final int PROPERTY_SEARCH = 2;
	private static final int LOCATION_SEARCH = 3;
	private static final int DETAILED_SEARCH = 4;
	
	private static List<FarmObj> farmResponse;
	
	private static String farmer_name = "", parish = "", extension = "", district = "";
	private static String farmer_id = "", property_id = "", latitude = "", longitude = "";
	private String apiResponse;
	
	private String mResponseError = "Unknown Error";
	private boolean mInitialScreen = true;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farms_main);

        String[] farmItems = getResources().getStringArray(R.array.ary_farms_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));

		ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		      // When clicked, show a toast with the TextView text
		    	
		    	Intent farmSearchIntent = new Intent();
		    	
		    	switch (position) {
				case 0:
					Toast.makeText(Farms.this, "You selected to Search by Farmer name", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, FarmerSearch.class);
					startActivityForResult(farmSearchIntent,FNAME_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					Toast.makeText(Farms.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, AreaSearch.class);
					startActivityForResult(farmSearchIntent,AREA_SEARCH);
					break;
				case 2: 
					Toast.makeText(Farms.this, "You selected to Search by Property ID", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, PIDSearch.class);
					startActivityForResult(farmSearchIntent,PROPERTY_SEARCH);
					break;
				case 3:
					Toast.makeText(Farms.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, LocationSearch.class);
					startActivityForResult(farmSearchIntent,LOCATION_SEARCH);
					break;
				case 4:
					Toast.makeText(Farms.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					farmSearchIntent.setClass(Farms.this, FarmerView.class);
					startActivityForResult(farmSearchIntent,DETAILED_SEARCH);
					break; 
				default:
					Toast.makeText(Farms.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
					break;
				}
		      
		    }
		  });
    }
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        farmResponse = new ArrayList<FarmObj>();
        
        //TODO: Receive query from search functions
        if (requestCode == FNAME_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//for farmer search by ID or Name
        		if(intent.getStringExtra("column").equals("Farmer Name")){
        			
        			farmer_name = intent.getStringExtra("value");
        			Toast.makeText(Farms.this, "Farmer Name: "+ farmer_name, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmData(farmer_name, FNAME_SEARCH);
        		}else{
        			farmer_id = intent.getStringExtra("value");
        			Toast.makeText(Farms.this, "Farmer ID: "+ farmer_id, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmData(farmer_id, FNAME_SEARCH);
        		}
        		if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			Toast.makeText(Farms.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
        			
        		}else{
        			
        			Toast.makeText(Farms.this, ""+ apiResponse, Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			farmResponse = parseResponse(apiResponse);
        			
        			//Toast.makeText(Farmers.this, ""+ farmer_list.get(0).toString(), Toast.LENGTH_SHORT).show();
        			Toast.makeText(Farms.this, "test" +"|"+ farmResponse.get(0).toString() +"|"+ farmResponse.size(), Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			/*Intent farmerIntent = new Intent();
        			Bundle b = new Bundle();
        			b.putString("response", apiResponse); // add return xml to bundle for next activity
        			farmerIntent.putExtras(b);
        			farmerIntent.setClass(Farmers.this, FarmerView.class);
        			
        			startActivity(farmerIntent);
        			finish();
        			//*///animator.setDisplayedChild(0);
        		}
        	}else if( resultCode == RESULT_CANCELED) {
        		Toast.makeText(Farms.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        		
        	}
        }if (requestCode == AREA_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//for farmer search by parish Extension or District
    			if(intent.getStringExtra("column").equals("Parish")){
        			
        			parish = intent.getStringExtra("value");
        			Toast.makeText(Farms.this, "Parish: "+ parish, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmData(parish, AREA_SEARCH);
        			
        		}else if(intent.getStringExtra("column").equals("Extension")) {
        			
        			extension = intent.getStringExtra("value");
        			Toast.makeText(Farms.this, "Extension "+ extension, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmData(extension, AREA_SEARCH);
        			
        		}else{
        			
        			district = intent.getStringExtra("value");
        			Toast.makeText(Farms.this, "district "+ district, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmData(district, AREA_SEARCH);
        			
        		}
    			
    			if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			Toast.makeText(Farms.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
        			//animator.setDisplayedChild(0);
        		}else{
        			
        			Toast.makeText(Farms.this, ""+ apiResponse, Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			farmResponse = parseResponse(apiResponse);
        			
        			//Toast.makeText(Farmers.this, ""+ farmer_list.get(0).toString(), Toast.LENGTH_SHORT).show();
        			Toast.makeText(Farms.this, "test" +"|"+ farmResponse.get(0).toString() +"|"+ farmResponse.size(), Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			
        		}
	        }else if( resultCode == RESULT_CANCELED) {
	    		Toast.makeText(Farms.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
	    		
	    		
	    	}
        	
        }if (requestCode == PROPERTY_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//for farm search by Property ID
        		property_id = intent.getStringExtra("value");
    			Toast.makeText(Farms.this, "Property ID "+ property_id, Toast.LENGTH_SHORT).show();
    			apiResponse = fetchFarmData(property_id, PROPERTY_SEARCH);
        		
    			if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			Toast.makeText(Farms.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
        			//animator.setDisplayedChild(0);
        		}else{
        			
        			Toast.makeText(Farms.this, ""+ apiResponse, Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			farmResponse = parseResponse(apiResponse);
        			
        			//Toast.makeText(Farmers.this, ""+ farmer_list.get(0).toString(), Toast.LENGTH_SHORT).show();
        			Toast.makeText(Farms.this, "test" +"|"+ farmResponse.get(0).toString() +"|"+ farmResponse.size(), Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			
        		}
	        }else if( resultCode == RESULT_CANCELED) {
	    		Toast.makeText(Farms.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
	    		
	    		
	    	}
        }if (requestCode == LOCATION_SEARCH) {
        	
        }if (requestCode == DETAILED_SEARCH) {
        	
        }
        
    }
//*****************************************************************************************************************************************
	private final String fetchFarmData(String column, final int selection) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));
    	
		switch(selection){
    	
    	case 0:
    		if (farmer_id.equals(column)){
        		client.AddParam("FarmerID", column);
        	}else{
        		
        		String fname, lname;
        		column = column.trim();
        		
        		int space = column.indexOf(' ');
        		if(space < 1){
        			client.AddParam("lastname", column);
        		}else{
        			
        			fname = column.substring(0, space);
        			lname= column.substring(space, column.length());
        			lname = lname.trim();
        			
        			client.AddParam("firstname", fname);
        			client.AddParam("lastname", lname);
        		}
        	}
    		break;
    	case 1:
    		if(parish.equals(column)){
    			client.AddParam("Parish", column);
    			
    		}else if(extension.equals(column)){
    			client.AddParam("Extension", column);
    			
    		}else{
    			client.AddParam("District", column);
    		}
    		break;
    	case 2:
    		client.AddParam("PropertyID", column);
    		break;
    	//case 3:
    		//perform location search
    	//case 4:
    		// get values from detailed search
    	default:
    		Toast.makeText(Farms.this, "Something went Totally Wrong ", Toast.LENGTH_SHORT).show();
    		
    	}
		
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
    	
		return response;
    }
	
	private List<FarmObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(Farms.this, responseStr);
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
