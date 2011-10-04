package org.data.agroassistant;

/*
 * Prices Activity
 * 
 * DESC: Top level activity for all searches related to Price information
 * TODO: 1) Implement correct searches
 * 		 2) Refactor activity
 * 
 */
import static org.data.agroassistant.Constants.*;

import java.util.ArrayList;
import java.util.List;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Prices extends ListActivity {
	static final int CROP_PARISH_SEARCH = 0;
	static final int PARISH_SEARCH = 1;
	static final int LOCATION_SEARCH = 2;
	static final int DETAILED_SEARCH = 3;
	
	private int searchType;
	private static String parish = "", crop_type = "", uprice = "", lprice = "", fprice = "", supplyStatus = "", quality = "";
	private static String priceMonth = "";
	private String queryParams;
	
	private static ViewAnimator animator;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prices_main);
		
		searchType = PRICE_SEARCH;
		animator = (ViewAnimator)findViewById(R.id.priceSwitcher);
		
		String[] priceItems = getResources().getStringArray(R.array.ary_prices_main);
		this.setListAdapter(new AgroArrayAdapter(this, priceItems));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					//Toast.makeText(Prices.this, "You selected to Search by Crop Type", //Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, CropParishSearch.class);
					startActivityForResult(farmerSearchIntent,CROP_PARISH_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					//Toast.makeText(Prices.this, "You selected to Search by Parish", //Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, ParishSearch.class);
					startActivityForResult(farmerSearchIntent,PARISH_SEARCH);
					break;
				case 2:
					//Toast.makeText(Prices.this, "You selected to Search by Location", //Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LOCATION_SEARCH);
					break;
				case 3:
					//Toast.makeText(Prices.this, "You selected to Search by Detailed search", //Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, DetailPriceSearch.class);
					startActivityForResult(farmerSearchIntent,DETAILED_SEARCH);
					
					break;
				default:
					//Toast.makeText(Prices.this, "Error: The option you selected does not exist", //Toast.LENGTH_SHORT).show();
					break;
				}
		      // When clicked, show a //Toast with the TextView text
		      
		    }
		  });

	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //TODO: Receive query from search functions
        
        if( resultCode == RESULT_OK) {
    		//Call function to pull data from query
	        if (requestCode == CROP_PARISH_SEARCH) {
	        	//Call function to pull data from query
        		parish = intent.getStringExtra("Parish");
	        	crop_type = intent.getStringExtra("Crop Type");
        		//Toast.makeText(Prices.this, "Crop Type: "+ crop_type, //Toast.LENGTH_SHORT).show();
        		FetchPriceData("", CROP_PARISH_SEARCH);
        		
        		
	        }else if (requestCode == PARISH_SEARCH){
	        	
	        	parish = intent.getStringExtra("Parish");
	        	FetchPriceData(parish, PARISH_SEARCH);
	        	
	        }else if (requestCode == LOCATION_SEARCH){
	        	
	        }else if (requestCode == DETAILED_SEARCH){
	        	
				
	        	parish = intent.getStringExtra("Parish");
	        	crop_type = intent.getStringExtra("Crop Type");
	        	
	        	if (!intent.getStringExtra("Price").equals("")){
	        		
	        		if (intent.getStringExtra("Column Type").equals("Upper")){
		        		uprice = intent.getStringExtra("Price");
		        	}else if(intent.getStringExtra("Column Type").equals("Lower")){
		        		lprice = intent.getStringExtra("Price");
		        	}else{
		        		fprice = intent.getStringExtra("Price");
		        	}
	        	}else 
	        	
	        	
	        	supplyStatus = intent.getStringExtra("Supply Status");
	        	quality      = intent.getStringExtra("Quality");
	        	priceMonth   = intent.getStringExtra("Price Month");
	        	
	        	FetchPriceData("", DETAILED_SEARCH);
	        }else{
	        	// log error here
	        }
	        
        }else if( resultCode == RESULT_CANCELED) {
    		//Toast.makeText(Prices.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
    	}
    }
	
	private final void FetchPriceData(String column, final int selection) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.PRICES_QUERY_URL));
    	
		switch(selection){
    	
    	case CROP_PARISH_SEARCH:
    		//client.AddParam("CropType", column);
    		addDetailedParams(client);
    		
    		break;
    	case PARISH_SEARCH:
    		client.AddParam("Parish", column);
    		
    	case LOCATION_SEARCH:
    		
    		break;
    	case DETAILED_SEARCH:
    		addDetailedParams(client);
    		break;
    	    		
    	default:
    		//Toast.makeText(Prices.this, "Something went Totally Wrong ", //Toast.LENGTH_SHORT).show();
    		break;
    	}
		
    	/*try {
    	    client.Execute(RESTServiceObj.RequestMethod.GET);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    //mResponseError = client.getErrorMessage();
    	    //return null;
    	}
    	final String response = client.getResponse();
    	if (response == null)
    		//mResponseError = client.getErrorMessage();
    	*/
		
    	queryParams = client.toString();
    	Log.d("AgroAssistant", "Prices > FetchPricesData: queryParams sent to API: " + queryParams);
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
			AgroAssistantDB agroDB = new AgroAssistantDB(Prices.this);
			if (agroDB.queryExists(PRICES_TABLE, queryParams)) {
				agroDB.close();
				return DB_SEARCH;
			} else {
		    	try {
		    	    client[0].Execute(RESTServiceObj.RequestMethod.GET);
		    	    agroDB.insertQuery(PRICES_TABLE, queryParams);
		    	    agroDB.close();
		    	} catch (Exception e) {
		    		Log.e("AgroAssistant","Prices RESTServiceObj pull: "+e.toString());
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

    			searchResultIntent.setClass(Prices.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
			}
			else if((apiResponse == null) || !(apiResponse.contains("Parish"))){	//Checks if API for data and acts accordingly
    			Toast.makeText(Prices.this, "Error: No Data retrieved", Toast.LENGTH_SHORT).show();
    			animator.setDisplayedChild(0);
    		}else{
				xmlParse parser = new xmlParse(Prices.this, apiResponse);
				parser.parseXML(PRICES_TABLE);
    			/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
    			searchResultBundle.putInt("searchType", searchType);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Prices.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
    		}
		}
	}
	
	/*private List<PriceObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(Prices.this, responseStr);
		String excptn = "";
		ArrayList<PriceObj> list = new ArrayList<PriceObj>();
		
		parser.parseXML(PRICES_TABLE);
		
		try{

			list = parser.getPriceList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}*/
	
	private void addDetailedParams(RESTServiceObj conn){
		
		if (!parish.equals("") && parish.length()>1){
			conn.AddParam("Parish", parish);
		}
		if (!crop_type.equals("") && crop_type.length()>1){
			conn.AddParam("CropType", crop_type);
		}
		if (lprice.equals("") && uprice.equals("") && fprice.equals("")){
			// no price to add
		}else if (lprice.equals("") && uprice.equals("")){
			conn.AddParam("FreqPrice", fprice);
			
		}else if(lprice.equals("") && fprice.equals("")){
			conn.AddParam("UpperPrice", uprice);
			
		}else if(uprice.equals("") && fprice.equals("")){
			conn.AddParam("LowerPrice", lprice);
		}
		
		if (!supplyStatus.equals("") && supplyStatus.length()>1){
			conn.AddParam("SupplyStatus", supplyStatus);
		}
		if (!quality.equals("") && quality.length()>1){
			conn.AddParam("Quality",quality);
		}
		if (!priceMonth.equals("") && priceMonth.length()>1){
			conn.AddParam("PriceMonth", priceMonth);
		}
		
	}
}
