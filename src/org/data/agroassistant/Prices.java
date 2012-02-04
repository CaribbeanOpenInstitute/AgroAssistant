package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.SEARCH_CODE;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.DBConstants.*;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ViewAnimator;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Prices extends ListActivity {
	private final String TAG = Farmers.class.getSimpleName();
	
	static final int LIST_CROP_PARISH_SEARCH = 0;
	static final int LIST_PARISH_SEARCH = 1;
	static final int LIST_LOCATION_SEARCH = 2;
	static final int LIST_DETAILED_SEARCH = 3;
	
	private int searchCode;
	private static ViewAnimator loadingAnimator;
	private AgroApplication agroApp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prices_main);
		agroApp = new AgroApplication();
		
		searchCode = PRICE_SEARCH;
		loadingAnimator = (ViewAnimator)findViewById(R.id.priceSwitcher);
		
		String[] priceItems = getResources().getStringArray(R.array.ary_prices_main);
		this.setListAdapter(new AgroArrayAdapter(this, priceItems));
		
		ListView priceListView = getListView();
		priceListView.setTextFilterEnabled(true);

		priceListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case LIST_CROP_PARISH_SEARCH:
					farmerSearchIntent.setClass(Prices.this, CropParishSearch.class);
					startActivityForResult(farmerSearchIntent,LIST_CROP_PARISH_SEARCH);
					break;
				case LIST_PARISH_SEARCH:
					farmerSearchIntent.setClass(Prices.this, ParishSearch.class);
					startActivityForResult(farmerSearchIntent,LIST_PARISH_SEARCH);
					break;
				case LIST_LOCATION_SEARCH:
					farmerSearchIntent.setClass(Prices.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LIST_LOCATION_SEARCH);
					break;
				case LIST_DETAILED_SEARCH:
					farmerSearchIntent.setClass(Prices.this, DetailPriceSearch.class);
					startActivityForResult(farmerSearchIntent,LIST_DETAILED_SEARCH);
					break;
				default:
					//Toast.makeText(Prices.this, "Error: The option you selected does not exist", //Toast.LENGTH_SHORT).show();
					break;
				}
		    }
		  });
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        if( resultCode == RESULT_OK) {
        	ContentValues searchParams = (ContentValues) intent.getParcelableExtra(SEARCH_PARAMS);
			new apiRequest().execute(searchParams);
        }else if( resultCode == RESULT_CANCELED) {
    		//Toast.makeText(Prices.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
    	}
    }
	
	private class apiRequest extends AsyncTask<ContentValues, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingAnimator.setDisplayedChild(1);
		}

		@Override
		protected String doInBackground(ContentValues... searchParams) {
			agroApp = ((AgroApplication)getApplication());
			agroApp.getQueryData(PRICES_SEARCH, searchParams[0]);
	    	String queryParams = agroApp.queryParams;	//e.g "parish=St.ANN and district=balaclava"
			return queryParams;
		}
		
		@Override
		protected void onPostExecute(String queryParams) {
			super.onPostExecute(queryParams);
			Intent searchResultIntent = new Intent();
			Bundle searchResultBundle = new Bundle();
			
			searchResultBundle.putInt(SEARCH_CODE, searchCode);
			searchResultBundle.putString(SEARCH_PARAMS, queryParams);
			searchResultIntent.putExtras(searchResultBundle);

			searchResultIntent.setClass(Prices.this, ResultView.class);
			startActivity(searchResultIntent);
			finish();
		}
	}
}
