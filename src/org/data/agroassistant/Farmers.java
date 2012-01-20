package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.AREA_SEARCH;
import static org.data.agroassistant.AgroConstants.DETAILED_SEARCH;
import static org.data.agroassistant.AgroConstants.FARMERS_SEARCH;
import static org.data.agroassistant.AgroConstants.LOCATION_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.DBConstants.FARMER_SEARCH;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ViewAnimator;


public class Farmers extends ListActivity {
	private final String TAG = Farmers.class.getSimpleName();
	
	private static final int LIST_FARMER_SEARCH = 0;
	private static final int LIST_AREA_SEARCH = 1;
	private static final int LIST_LOCATION_SEARCH = 2;
	private static final int LIST_DETAILED_SEARCH = 3;

	private int searchCode;
	private static ViewAnimator loadingAnimator;
	private AgroApplication agroApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		agroApp = new AgroApplication();
		
		setContentView(R.layout.farmers_main);

		searchCode = FARMER_SEARCH;
		loadingAnimator = (ViewAnimator)findViewById(R.id.farmerSwitcher);	//Loading Animator

		String[] farmerItems = getResources().getStringArray(R.array.ary_farmers_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmerItems));

		ListView farmerListView = getListView();
		farmerListView.setTextFilterEnabled(true);

		farmerListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case LIST_FARMER_SEARCH:
					farmerSearchIntent.setClass(Farmers.this, FarmerSearch.class);
					startActivityForResult(farmerSearchIntent,FARMER_SEARCH); //Define in AgroConstants
					break;
				case LIST_AREA_SEARCH:
					farmerSearchIntent.setClass(Farmers.this, AreaSearch.class);
					startActivityForResult(farmerSearchIntent,AREA_SEARCH);
					break;
				case LIST_LOCATION_SEARCH:
					farmerSearchIntent.setClass(Farmers.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LOCATION_SEARCH);
					break;
				case LIST_DETAILED_SEARCH:
					farmerSearchIntent.setClass(Farmers.this, FarmerDetailSearch.class);
					startActivityForResult(farmerSearchIntent,DETAILED_SEARCH);
					break;
				default:
					//Toast.makeText(Farmers.this, "No Selection made", Toast.LENGTH_SHORT).show();
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
    	} else if( resultCode == RESULT_CANCELED) {
        		//Toast.makeText(Farmers.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
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
			agroApp.getQueryData(FARMERS_SEARCH, searchParams[0]);
			
	    	String queryParams = agroApp.queryParams;	//e.g "parish=St.ANN and district=balaclava"
	    	//Log.e(TAG, String.format("Query Param String: %s", queryParams));
			return queryParams;
		}
		
		@Override
		protected void onPostExecute(String queryParams) {
			super.onPostExecute(queryParams);
			Intent searchResultIntent = new Intent();
			Bundle searchResultBundle = new Bundle();
			
			searchResultBundle.putInt("searchCode", searchCode);
			searchResultBundle.putString("searchParams", queryParams);
			searchResultIntent.putExtras(searchResultBundle);

			searchResultIntent.setClass(Farmers.this, ResultView.class);
			startActivity(searchResultIntent);
			finish();
			
		}
	}
}
