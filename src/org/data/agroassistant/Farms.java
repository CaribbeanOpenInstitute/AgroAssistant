/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gebre
 * @author matthewm
 */

package org.data.agroassistant;

import java.util.ArrayList;

import java.util.List;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.DBConstants.*;



import android.app.ListActivity;
import android.content.ContentValues;
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
public class Farms extends ListActivity{
	private final String TAG = Farms.class.getSimpleName();

	private static final int LIST_FNAME_SEARCH = 0;
	private static final int LIST_AREA_SEARCH = 1;
	private static final int LIST_PROPERTY_SEARCH = 2;
	private static final int LIST_LOCATION_SEARCH = 3;
	private static final int LIST_DETAILED_SEARCH = 4;
	
	/*private int searchCode;
	private static String farmer_name = "", parish = "", extension = "", district = "", crop_type = "", crop_group = "";
	private static String farmer_id = "", property_id = "", latitude = "", longitude = "";
	private String apiResponse;*/
	private String queryParams;
	
	private int searchCode;
	private int searchType;
	private static ViewAnimator loadingAnimator;
	private AgroApplication agroApp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farms_main);
        agroApp = new AgroApplication();
        
        searchCode = FARM_SEARCH;
        loadingAnimator = (ViewAnimator)findViewById(R.id.farmSwitcher);

        String[] farmItems = getResources().getStringArray(R.array.ary_farms_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmItems));

		ListView farmListView = getListView();
		  farmListView.setTextFilterEnabled(true);

		  farmListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmSearchIntent = new Intent();
		    	switch (position) {
				case LIST_FNAME_SEARCH:
					farmSearchIntent.setClass(Farms.this, FarmerSearch.class);
					startActivityForResult(farmSearchIntent,LIST_FNAME_SEARCH);
					break;
				case LIST_AREA_SEARCH:
					farmSearchIntent.setClass(Farms.this, AreaSearch.class);
					startActivityForResult(farmSearchIntent,LIST_AREA_SEARCH);
					break;
				case LIST_PROPERTY_SEARCH: 
					farmSearchIntent.setClass(Farms.this, PIDSearch.class);
					startActivityForResult(farmSearchIntent,LIST_PROPERTY_SEARCH);
					break;
				case LIST_LOCATION_SEARCH:
					farmSearchIntent.setClass(Farms.this, LocationSearch.class);
					startActivityForResult(farmSearchIntent,LIST_LOCATION_SEARCH);
					break;
				case LIST_DETAILED_SEARCH:
					farmSearchIntent.setClass(Farms.this, DetailSearch.class);
					startActivityForResult(farmSearchIntent,LIST_DETAILED_SEARCH);
					break; 
				default:
					//Toast.makeText(Farms.this, "Error: The option you selected does not exist", //Toast.LENGTH_SHORT).show();
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
        	searchType = intent.getIntExtra(SEARCH_TYPE, 0);
			new apiRequest().execute(searchParams);
    	}else if( resultCode == RESULT_CANCELED) {
        		//Toast.makeText(Farms.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
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
			//Log.e(TAG, String.format("Query Search Params: %s", searchParams[0]));
			agroApp = ((AgroApplication)getApplication());
			agroApp.getQueryData(FARMS_SEARCH, searchParams[0]);
			
			
	    	queryParams = agroApp.queryParams;	//e.g "parish=St.ANN and district=balaclava"
	    	//Log.e(TAG, String.format("Query Param String: %s", queryParams));
			return queryParams;
		}
		
		@Override
		protected void onPostExecute(String queryParams) {
			super.onPostExecute(queryParams);
			Intent searchResultIntent = new Intent();
			Bundle searchResultBundle = new Bundle();
			
			searchResultBundle.putInt(SEARCH_CODE, searchCode);
			searchResultBundle.putInt(SEARCH_TYPE, searchType);
			searchResultBundle.putString(SEARCH_PARAMS, queryParams);
			searchResultIntent.putExtras(searchResultBundle);

			searchResultIntent.setClass(Farms.this, ResultView.class);
			startActivity(searchResultIntent);
			finish();
		}
	}
}
