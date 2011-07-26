package org.data.agroassistant;

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
import android.widget.ViewAnimator;

public class Farmers extends ListActivity {
	private static final int FNAME_SEARCH = 0;
	private static final int AREA_SEARCH = 1;
	private static final int LOCATION_SEARCH = 2;
	private static final int DETAILED_SEARCH = 3;
	
	private static List<FarmerObj> farmerResponse;
	
	
	private static String farmer_name = "", parish = "", extension = "", district = "";
	private static String farmer_id = "", latitude = "", longitude = "";
	private static ViewAnimator animator;
	
	private String mResponseError = "Unknown Error";
	private boolean mInitialScreen = true;
	
	private String apiResponse;
	
	//private LayoutInflater mInflater;
	//private Vector<RowData> data;
	//RowData rd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmers_main);
		
		
		//animator = (ViewAnimator)findViewById(R.id.anim);
		
		String[] farmerItems = getResources().getStringArray(R.array.ary_farmers_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmerItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					Toast.makeText(Farmers.this, "You selected to Search by Farmer name", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, FarmerSearch.class);
					startActivityForResult(farmerSearchIntent,FNAME_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					Toast.makeText(Farmers.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, AreaSearch.class);
					startActivityForResult(farmerSearchIntent,AREA_SEARCH);
					break;
				case 2:
					Toast.makeText(Farmers.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LOCATION_SEARCH);
					break;
				case 3:
					Toast.makeText(Farmers.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					//farmerSearchIntent.setClass(Farmers.this, FarmerView.class);
					farmerSearchIntent.setClass(Farmers.this, FarmView.class);
					startActivityForResult(farmerSearchIntent,DETAILED_SEARCH);
					break; 
				default:
					Toast.makeText(Farmers.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
					break;
				}
		      // When clicked, show a toast with the TextView text
		      
		    }
		  });

	}
	
	protected void onPreExecute() {
		animator.setDisplayedChild(1);
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        farmerResponse = new ArrayList<FarmerObj>();
        
        //TODO: Receive query from search functions
        if (requestCode == FNAME_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//for farmer search by ID or Name
        		if(intent.getStringExtra("column").equals("Farmer Name")){
        			
        			farmer_name = intent.getStringExtra("value");
        			Toast.makeText(Farmers.this, "Farmer Name: "+ farmer_name, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmerData(farmer_name, FNAME_SEARCH);
        		}else{
        			farmer_id = intent.getStringExtra("value");
        			Toast.makeText(Farmers.this, "Farmer ID: "+ farmer_id, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmerData(farmer_id, FNAME_SEARCH);
        		}
        		if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			Toast.makeText(Farmers.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
        			animator.setDisplayedChild(0);
        		}else{
        			
        			Toast.makeText(Farmers.this, ""+ apiResponse, Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			farmerResponse = parseResponse(apiResponse);
        			
        			//Toast.makeText(Farmers.this, ""+ farmer_list.get(0).toString(), Toast.LENGTH_SHORT).show();
        			Toast.makeText(Farmers.this, "test" +"|"+ farmerResponse.get(0).toString() +"|"+ farmerResponse.size(), Toast.LENGTH_SHORT).show();
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
        		Toast.makeText(Farmers.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        		
        	}
        }if (requestCode == AREA_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        		//for farmer search by parish Extension or District
    			if(intent.getStringExtra("column").equals("Parish")){
        			
        			parish = intent.getStringExtra("value");
        			Toast.makeText(Farmers.this, "Parish: "+ parish, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmerData(parish, AREA_SEARCH);
        			
        		}else if(intent.getStringExtra("column").equals("Extension")) {
        			
        			extension = intent.getStringExtra("value");
        			Toast.makeText(Farmers.this, "Extension "+ extension, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmerData(extension, AREA_SEARCH);
        			
        		}else{
        			
        			district = intent.getStringExtra("value");
        			Toast.makeText(Farmers.this, "district "+ district, Toast.LENGTH_SHORT).show();
        			apiResponse = fetchFarmerData(district, AREA_SEARCH);
        			
        		}
    			
    			if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			Toast.makeText(Farmers.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
        			animator.setDisplayedChild(0);
        		}else{
        			
        			Toast.makeText(Farmers.this, ""+ apiResponse, Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			farmerResponse = parseResponse(apiResponse);
        			
        			//Toast.makeText(Farmers.this, ""+ farmer_list.get(0).toString(), Toast.LENGTH_SHORT).show();
        			Toast.makeText(Farmers.this, "test" +"|"+ farmerResponse.get(0).toString() +"|"+ farmerResponse.size(), Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			
        		}
	        }else if( resultCode == RESULT_CANCELED) {
	    		Toast.makeText(Farmers.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
	    		
	    		
	    	}
        	
        }if (requestCode == LOCATION_SEARCH) {
        	
        }if (requestCode == DETAILED_SEARCH) {
        	
        }
        
    }
//*****************************************************************************************************************************************
	private final String fetchFarmerData(final String column, final int selection) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));
    	
		switch(selection){
    	
    	case 0:
    		if (farmer_id.equals(column)){
        		client.AddParam("FarmerID", column);
        	}else{
        		
        		client.AddParam("FarmerID", column);
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
    		
    	//case 2:
    		//perform location search
    	//case 3:
    		// get values from detailed search
    	default:
    		Toast.makeText(Farmers.this, "Something went Totally Wrong ", Toast.LENGTH_SHORT).show();
    		
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
	
	private List<FarmerObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(responseStr);
		String excptn = "";
		ArrayList<FarmerObj> list = new ArrayList<FarmerObj>();
		
		parser.parseXML("Farmer");
		
		try{

			list = parser.getFarmerList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}
	
}
