package org.data.agroassistant;

import static org.data.agroassistant.Constants.FARMER_SEARCH;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Prices extends ListActivity {
	static final int CROP_SEARCH = 0;
	static final int PARISH_SEARCH = 1;
	static final int LOCATION_SEARCH = 2;
	static final int DETAILED_SEARCH = 3;
	
	private static List<PriceObj> priceResponse;
	
	private static String parish = "", crop_type = "", uprice = "", lprice = "", fprice = "", supplyStatus = "", quality = "";
	private static String farmer_id = "", property_id = "", latitude = "", longitude = "", priceMonth = "";
	private String apiResponse;
	private String queryParams;
	
	private int dtlSelection;
	private String mResponseError = "Unknown Error";
	private boolean mInitialScreen = true;
	
	//private LayoutInflater mInflater;
	//private Vector<RowData> data;
	//RowData rd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prices_main);
		
		String[] priceItems = getResources().getStringArray(R.array.ary_prices_main);
		this.setListAdapter(new AgroArrayAdapter(this, priceItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					//Toast.makeText(Prices.this, "You selected to Search by Crop Type", //Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, CropSearch.class);
					startActivityForResult(farmerSearchIntent,CROP_SEARCH);
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
        
        priceResponse = new ArrayList<PriceObj>();
        Intent searchResultIntent = new Intent();
		Bundle searchResultBundle = new Bundle();
        
        if( resultCode == RESULT_OK) {
    		//Call function to pull data from query
	        if (requestCode == CROP_SEARCH) {
	        	//Call function to pull data from query
        		//intent.
        		crop_type = intent.getStringExtra("value");
        		//Toast.makeText(Prices.this, "Crop Type: "+ crop_type, //Toast.LENGTH_SHORT).show();
        		apiResponse = FetchPriceData(crop_type, CROP_SEARCH);
        		
        		
	        }else if (requestCode == PARISH_SEARCH){
	        	
	        	parish = intent.getStringExtra("Parish");
	        	apiResponse = FetchPriceData(parish, PARISH_SEARCH);
	        	
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
	        	
	        	apiResponse = FetchPriceData("", DETAILED_SEARCH);
	        }else{
	        	// log error here
	        }
	        
	      //Checks if API for data and acts accordingly
    		if((apiResponse == null) || !(apiResponse.contains("Parish"))){
    			//Toast.makeText(Prices.this, "Error: No Data retrieved", //Toast.LENGTH_SHORT).show();
    		}else{
    			//Toast.makeText(Prices.this, apiResponse, //Toast.LENGTH_SHORT).show();
    			
    			priceResponse = parseResponse(apiResponse);
    			/*
    			 *Call & pass necessary information to ResultView activity
    			 *finish Farmer search activity
    			 */
    			//Toast.makeText(Prices.this, ""+priceResponse.get(0) + "|" + priceResponse.size()+ "|" + queryParams, //Toast.LENGTH_SHORT).show();
    			
    			searchResultBundle.putString("response", apiResponse); // add return xml to bundle for next activity
    			searchResultBundle.putInt("searchType", FARMER_SEARCH);
    			searchResultBundle.putString("searchParams", queryParams);
    			searchResultIntent.putExtras(searchResultBundle);

    			searchResultIntent.setClass(Prices.this, ResultView.class);
    			startActivity(searchResultIntent);
    			finish();
    			//*/
    		}
        }else if( resultCode == RESULT_CANCELED) {
    		//Toast.makeText(Prices.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
    		
    	}
    }
	
	private final String FetchPriceData(String column, final int selection) {
		
		final RESTServiceObj client = new RESTServiceObj(getString(R.string.PRICES_QUERY_URL));
    	
		switch(selection){
    	
    	case CROP_SEARCH:
    		client.AddParam("CropType", column);
    		
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
	
	private List<PriceObj> parseResponse(String responseStr){
		
		xmlParse parser = new xmlParse(Prices.this, responseStr);
		String excptn = "";
		ArrayList<PriceObj> list = new ArrayList<PriceObj>();
		
		parser.parseXML("Price");
		
		try{

			list = parser.getPriceList();
			
		}catch(Exception e){
			excptn = e.toString();
		}
		
		return list;
	}
	
	private void addDetailedParams(RESTServiceObj conn){
		
		if (!parish.equals("") && parish.length()>1){
			conn.AddParam("Parish", parish);
		}
		if (!crop_type.equals("") && crop_type.length()>1){
			conn.AddParam("Crop Type", crop_type);
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
