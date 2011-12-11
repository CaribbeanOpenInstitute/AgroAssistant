package org.data.agroassistant;

import java.util.ArrayList;
import java.util.List;

import static org.data.agroassistant.Constants.*;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

	private int searchType;
	private static String farmer_name = "", parish = "", extension = "", district = "";
	private static String farmer_id = "";// latitude = "", longitude = "";
	private String apiResponse;
	private int dtlSelection;
	private static ViewAnimator animator;

	private String mResponseError = "Unknown Error";
	private boolean mInitialScreen = true;


	private String queryParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmers_main);

		searchType = FARMER_SEARCH;
		animator = (ViewAnimator)findViewById(R.id.farmerSwitcher);

		String[] farmerItems = getResources().getStringArray(R.array.ary_farmers_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmerItems));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					//Toast.makeText(Farmers.this, "You selected to Search by Farmer name", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, FarmerSearch.class);
					startActivityForResult(farmerSearchIntent,FNAME_SEARCH);
					break;
				case 1:
					//Toast.makeText(Farmers.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, AreaSearch.class);
					startActivityForResult(farmerSearchIntent,AREA_SEARCH);
					break;
				case 2:
					//Toast.makeText(Farmers.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LOCATION_SEARCH);
					break;
				case 3:
					//Toast.makeText(Farmers.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, FarmerDetailSearch.class);
					startActivityForResult(farmerSearchIntent,DETAILED_SEARCH);
					break;
				default:
					//Toast.makeText(Farmers.this, "No Selection made", Toast.LENGTH_SHORT).show();
					break;
				}
		      // When clicked, show a toast with the TextView text

		    }
		  });
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
        super.onActivityResult(requestCode, resultCode, intent);

		if( resultCode == RESULT_OK) {
			if (requestCode == FNAME_SEARCH) {
        		//Call function to pull data by ID or name
        		if(intent.getStringExtra("column").equals("Farmer Name")){
        			farmer_name = intent.getStringExtra("value");
        			fetchFarmerData(farmer_name, FNAME_SEARCH);
        		}else{
        			farmer_id = intent.getStringExtra("value");
        			fetchFarmerData(farmer_id, FNAME_SEARCH);
        		}

        	} else if (requestCode == AREA_SEARCH) {

    			if(intent.getStringExtra("column").equals("Parish")){

        			parish = intent.getStringExtra("value");
        			fetchFarmerData(parish, AREA_SEARCH);

        		}else if(intent.getStringExtra("column").equals("Extension")) {

        			extension = intent.getStringExtra("value");
        			fetchFarmerData(extension, AREA_SEARCH);

        		}else if(intent.getStringExtra("column").equals("District")) {

        			district = intent.getStringExtra("value");
        			fetchFarmerData(district, AREA_SEARCH);
        		}
	    	} else if (requestCode == LOCATION_SEARCH) {
        		//Call function to pull data from query
        		//for farmer search by location
	    	} else if (requestCode == DETAILED_SEARCH) {
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
        			getAreaData(intent);
        			farmer_name = intent.getStringExtra("Farmer");
    				break;
        		default:
        			//Toast.makeText(Farmers.this, "Error: This Makes no Sense", Toast.LENGTH_SHORT).show();
        		}
        		fetchFarmerData("", DETAILED_SEARCH);
        	}
    	} else if( resultCode == RESULT_CANCELED) {
        		//Toast.makeText(Farmers.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
    	}

    }
//*****************************************************************************************************************************************
	private final void fetchFarmerData(String column, final int selection) {

		final RESTServiceObj client = new RESTServiceObj(getString(R.string.FARMS_QUERY_URL));

		switch(selection){
    	case FNAME_SEARCH:
    		if (farmer_id.equals(column)){
        		client.AddParam("FarmerID", column);
        	}else{
        		addNames(column, client);
        	}
    		break;
    	case AREA_SEARCH:
    		addAreaParam(column, client);
    		break;
    	case LOCATION_SEARCH:
    		//perform location search
    		break;
    	case DETAILED_SEARCH:
    		// get values from detail search
    		switch(dtlSelection){
	    		case 1:
	    			addNames(farmer_name, client);
	    			break;
	    		case 2:        			
	    			addAreaParam("", client);
	    			
	    			break;
	    		case 3:
	    			addAreaParam("", client);
	    			addNames(farmer_name, client);
	    			
					break;
	    		
	    		default:
	    			Toast.makeText(Farmers.this, "Error: This Makes no Sense", Toast.LENGTH_SHORT).show();
	    			break;
	    		}
    		break;
    	default:
    		Toast.makeText(Farmers.this, "Something went Totally Wrong ", Toast.LENGTH_SHORT).show();
    		break;
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
			AgroAssistantDB agroDB = new AgroAssistantDB(Farmers.this);
			if (agroDB.queryExists(FARMERS_TABLE, queryParams)) {
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(FARMERS_TABLE, queryParams);
		    	    agroDB.close();
		    	} catch (Exception e) {
		    	    e.printStackTrace();
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

    			searchResultIntent.setClass(Farmers.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){	//Checks if API for data and acts accordingly
    			Toast.makeText(Farmers.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
    			animator.setDisplayedChild(0);
    		}else{
				xmlParse parser = new xmlParse(Farmers.this, apiResponse);
				parser.parseXML(FARMERS_TABLE);
    			/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Farmers.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
    		}
		}
	}
	
	private void addNames(String fullName, RESTServiceObj client){
		String fname, lname;
		fullName = fullName.trim();

		int space = fullName.indexOf(' ');
		if(space < 1){
			client.AddParam("lastname", fullName);
		}else{

			fname = fullName.substring(0, space);
			lname= fullName.substring(space, fullName.length());
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


	private void getAreaData(Intent intent){
		if(intent.getStringExtra("AreaCol").equals("Parish")){

			parish = intent.getStringExtra("Parish");
			Toast.makeText(Farmers.this, "Parish: "+ parish, Toast.LENGTH_SHORT).show();

		}else if(intent.getStringExtra("AreaCol").equals("Extension")) {

			extension = intent.getStringExtra("Extension");
			Toast.makeText(Farmers.this, "Extension "+ extension, Toast.LENGTH_SHORT).show();

		}else{

			district = intent.getStringExtra("District");
			Toast.makeText(Farmers.this, "district "+ district, Toast.LENGTH_SHORT).show();
		}
	}

}
