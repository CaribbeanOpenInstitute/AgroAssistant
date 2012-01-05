package org.data.agroassistant;

import java.util.ArrayList;
import java.util.List;
import static org.data.agroassistant.DBConstants.*;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
//import android.content.Intent;
//import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 *
 * @author Gebre
 */
public class Crops extends ListActivity{

	static final int CROP_TG_SEARCH = 0;
	static final int AREA_SEARCH = 1;
	static final int FNAME_SEARCH = 2;
	static final int PROPERTY_SEARCH = 3;
	static final int LOCATION_SEARCH = 4;
	static final int DETAILED_SEARCH = 5;
	
	
	private static List<CropObj> cropResponse;
	
	private int searchType;
	private static String farmer_name = "", parish = "", extension = "", district = "", crop_type = "", crop_group = "";
	private static String farmer_id = "", property_id = "", latitude = "", longitude = "";
	private String apiResponse;
	private String queryParams = "";
	
	private int dtlSelection;
	private String mResponseError = "Unknown Error";
	private boolean mInitialScreen = true;
	private static ViewAnimator animator;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_main);
        
        searchType = CROP_SEARCH;
        animator = (ViewAnimator)findViewById(R.id.cropSwitcher);
        
        String[] cropItems = getResources().getStringArray(R.array.ary_crops_main);
		this.setListAdapter(new AgroArrayAdapter(this, cropItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));

		ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	
		    	Intent cropSearchIntent = new Intent();
		    	
		    	switch (position) {
		    	case 0:
					cropSearchIntent.setClass(Crops.this, CropSearch.class);
					startActivityForResult(cropSearchIntent,CROP_TG_SEARCH);
					break;
		    	case 1:
					cropSearchIntent.setClass(Crops.this, AreaSearch.class);
					startActivityForResult(cropSearchIntent,AREA_SEARCH);
					break;
		    	case 2:
					cropSearchIntent.setClass(Crops.this, FarmerSearch.class);
					startActivityForResult(cropSearchIntent,FNAME_SEARCH);
					break;
				case 3: 
					cropSearchIntent.setClass(Crops.this, PIDSearch.class);
					startActivityForResult(cropSearchIntent,PROPERTY_SEARCH);
					break;
				case 4:
					cropSearchIntent.setClass(Crops.this, LocationSearch.class);
					startActivityForResult(cropSearchIntent,LOCATION_SEARCH);
					break;
				case 5:
					cropSearchIntent.setClass(Crops.this, DetailSearch.class);
					startActivityForResult(cropSearchIntent,DETAILED_SEARCH);
					break; 
				default:
					//Toast.makeText(Crops.this, "Error: The option you selected does not exist", //Toast.LENGTH_SHORT).show();
					break;
				}
		    }
		  });
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        cropResponse = new ArrayList<CropObj>();
        
        //TODO: Receive query from search functions
		if( resultCode == RESULT_OK) {
			if (requestCode == CROP_TG_SEARCH) {
        		//Call function to pull data from query
        		//intent.
        		if(intent.getStringExtra("column").equals("Crop Type")){
        			
        			crop_type = intent.getStringExtra("value");
        			FetchCropData(crop_type, CROP_TG_SEARCH);
        		}else{
        			crop_group = intent.getStringExtra("value");
        			FetchCropData(crop_group, CROP_TG_SEARCH);
        		}
        	} else if (requestCode == FNAME_SEARCH) {
        		//Call function to pull data from query
        		//for farmer search by ID or Name
        		if(intent.getStringExtra("column").equals("Farmer Name")){
        			
        			farmer_name = intent.getStringExtra("value");
        			//Toast.makeText(Crops.this, "Farmer Name: "+ farmer_name, //Toast.LENGTH_SHORT).show();
        			FetchCropData(farmer_name, FNAME_SEARCH);
        		}else{
        			farmer_id = intent.getStringExtra("value");
        			//Toast.makeText(Crops.this, "Farmer ID: "+ farmer_id, //Toast.LENGTH_SHORT).show();
        			FetchCropData(farmer_id, FNAME_SEARCH);
        		}
        	} else if (requestCode == AREA_SEARCH) {
        		//Call function to pull data from query
        		//for farmer search by parish Extension or District
    			if(intent.getStringExtra("column").equals("Parish")){
        			
        			parish = intent.getStringExtra("value");
        			//Toast.makeText(Crops.this, "Parish: "+ parish, //Toast.LENGTH_SHORT).show();
        			FetchCropData(parish, AREA_SEARCH);
        			
        		}else if(intent.getStringExtra("column").equals("Extension")) {
        			
        			extension = intent.getStringExtra("value");
        			//Toast.makeText(Crops.this, "Extension "+ extension, //Toast.LENGTH_SHORT).show();
        			FetchCropData(extension, AREA_SEARCH);
        			
        		}else{
        			
        			district = intent.getStringExtra("value");
        			//Toast.makeText(Crops.this, "district "+ district, //Toast.LENGTH_SHORT).show();
        			FetchCropData(district, AREA_SEARCH);
        			
        		}
    			
    			if((apiResponse == null) || !(apiResponse.contains("Parish"))){
        			//Toast.makeText(Crops.this, "Error: No Data retrieved", //Toast.LENGTH_SHORT).show();
        			//animator.setDisplayedChild(0);
        		}else{
        			
        			//Toast.makeText(Crops.this, ""+ apiResponse, //Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			//just to test the parsing here
        			cropResponse = parseResponse(apiResponse);
        			
        			////Toast.makeText(Crops.this, ""+ farmer_list.get(0).toString(), //Toast.LENGTH_SHORT).show();
        			//Toast.makeText(Crops.this, "test" +"|"+ cropResponse.get(0).toString() +"|"+ cropResponse.size(), //Toast.LENGTH_SHORT).show();
        			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        			
        		}
	        } else if (requestCode == PROPERTY_SEARCH) {
        		//Call function to pull data from query
        		//for farm search by Property ID
        		property_id = intent.getStringExtra("value");
    			//Toast.makeText(Crops.this, "Property ID "+ property_id, //Toast.LENGTH_SHORT).show();
    			FetchCropData(property_id, PROPERTY_SEARCH);
        		
	        } else if (requestCode == LOCATION_SEARCH) {
	        	
	        } else if (requestCode == DETAILED_SEARCH) {
        		//Call function to pull data from query
        		//for farmer detail search
        		String selectionStr = intent.getStringExtra("selection");
        		dtlSelection = Integer.parseInt(selectionStr);
        		
        		switch(dtlSelection){
        		
        		case 1:
        			farmer_name = intent.getStringExtra("Farmer");
        			
        			break;
        		case 2:        			
        			getAreaData(intent);
        			
        			break;
        		case 3:
        			getCropData(intent);
    				break;
        		case 4:
        			getAreaData(intent);
        			farmer_name = intent.getStringExtra("Farmer");
        			
    				break;
        		case 5:
        			getAreaData(intent);
        			getCropData(intent);
        			
        			break;
        		case 6:
        			farmer_name = intent.getStringExtra("Farmer");
        			getCropData(intent);
    				break;
        		case 7:
        			getAreaData(intent);
        			getCropData(intent);
        			farmer_name = intent.getStringExtra("Farmer");
        			
        			break;
        		default:
        			//Toast.makeText(Crops.this, "Error: This Makes no Sense", //Toast.LENGTH_SHORT).show();
        		}
        		FetchCropData("", DETAILED_SEARCH);
        	}
        } else if( resultCode == RESULT_CANCELED) {
    		//Toast.makeText(Crops.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
    	}
	    		
    }
	
//*****************************************************************************************************************************************
	private final void FetchCropData(String column, final int selection) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.CROPS_QUERY_URL));
    	
		switch(selection){
    	
    	case 0:
    		if(crop_type.equals(column)){
    			client.AddParam("CropType", column);
    		}else{
    			client.AddParam("CropGroup", column);
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
    	case 3:
    		client.AddParam("PropertyID", column);
    		break;
    	case 4:
    		//perform location search
    		break;
    	case 5:
    		// get values from detail search
    		switch(dtlSelection){
    		
    		case 1:
    			addNames(farmer_name, client);
    			
    			break;
    		case 2:        			
    			addAreaParam("", client);
    			
    			break;
    		case 3:
    			addCropParam(client);
				break;
    		case 4:
    			addAreaParam("", client);
    			addNames(farmer_name, client);
    			
				break;
    		case 5:
    			addAreaParam("", client);
    			addCropParam(client);
    			
    			break;
    		case 6:
    			addNames(farmer_name, client);
    			addCropParam(client);
				break;
    		case 7:
    			addAreaParam("", client);
    			addCropParam(client);
    			addNames(farmer_name, client);
    			
    			break;
    		default:
    			//Toast.makeText(Crops.this, "Error: This Makes no Sense", //Toast.LENGTH_SHORT).show();
    		}
    		break;
    		
    	default:
    		//Toast.makeText(Crops.this, "Something went Totally Wrong ", //Toast.LENGTH_SHORT).show();
    		
    	}
		
		queryParams = client.toString();
		new apiRequest().execute(client);
    }
	
	private class apiRequest extends AsyncTask<RESTServiceObj, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			animator.setDisplayedChild(1);
		}

		@Override
		protected String doInBackground(RESTServiceObj... client) {
			AgroAssistantDB agroDB = new AgroAssistantDB(Crops.this);
			if (agroDB.queryExists(CROPS_TABLE, queryParams)) {
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(CROPS_TABLE, queryParams);
		    	    agroDB.close();
		    	} catch (Exception e) {
		    		Log.e("AgroAssistant","Crops RESTServiceObj pull: "+e.toString());
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
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Crops.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){	//Checks if API for data and acts accordingly
    			Toast.makeText(Crops.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
    			animator.setDisplayedChild(0);
    		}else{
				xmlParse parser = new xmlParse(Crops.this, apiResponse);
				parser.parseXML(CROPS_TABLE);
    			/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Crops.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
    		}
		}
	}
	
	private List<CropObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(Crops.this, responseStr);
		String excptn = "";
		ArrayList<CropObj> list = new ArrayList<CropObj>();
		
		parser.parseXML(CROPS_TABLE);
		
		try{

			list = parser.getCropList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}
	
	
	private void addNames(String name, RESTServiceObj client){
		String fname, lname;
		farmer_name = farmer_name.trim();
		
		int space = farmer_name.indexOf(' ');
		if(space < 1){
			client.AddParam("lastname", farmer_name);
		}else{
			
			fname = farmer_name.substring(0, space);
			lname= farmer_name.substring(space, farmer_name.length());
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
	
	private void addCropParam(RESTServiceObj client){
		if(!crop_type.equals("")){
			client.AddParam("CropType", crop_type);
			
		}else{
			client.AddParam("CropGroup", crop_group);
		}
	}
	private void getAreaData(Intent intent){
		if(intent.getStringExtra("AreaCol").equals("Parish")){
			
			parish = intent.getStringExtra("Parish");
			//Toast.makeText(Crops.this, "Parish: "+ parish, //Toast.LENGTH_SHORT).show();
			
		}else if(intent.getStringExtra("AreaCol").equals("Extension")) {
			
			extension = intent.getStringExtra("Extension");
			//Toast.makeText(Crops.this, "Extension "+ extension, //Toast.LENGTH_SHORT).show();
			
		}else{
			
			district = intent.getStringExtra("District");
			//Toast.makeText(Crops.this, "district "+ district, //Toast.LENGTH_SHORT).show();
		}
	}
	
	private void getCropData(Intent intent){
		if(intent.getStringExtra("CropCol").equals("Crop Type")){
			
			crop_type = intent.getStringExtra("Crop Type");
			//Toast.makeText(Crops.this, "Crop Type: "+ crop_type, //Toast.LENGTH_SHORT).show();
			
		}else{
			crop_group = intent.getStringExtra("Crop Group");
			//Toast.makeText(Crops.this, "Crop Group: "+ crop_group, //Toast.LENGTH_SHORT).show();
			
		}
	}

}
