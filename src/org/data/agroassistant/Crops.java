package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.CROPS_SEARCH;
import static org.data.agroassistant.AgroConstants.CROP_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_CODE;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
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
/**
 *
 * @author Gebre
 */
public class Crops extends ListActivity{

	static final int LIST_CROP_TG_SEARCH = 0;
	static final int LIST_AREA_SEARCH = 1;
	static final int LIST_FNAME_SEARCH = 2;
	static final int LIST_PROPERTY_SEARCH = 3;
	static final int LIST_LOCATION_SEARCH = 4;
	static final int LIST_DETAILED_SEARCH = 5;
	
	private int searchCode;
	private static ViewAnimator loadingAnimator;
	private AgroApplication agroApp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_main);
        agroApp = new AgroApplication();
        
        searchCode = CROP_SEARCH;
        loadingAnimator = (ViewAnimator)findViewById(R.id.cropSwitcher);
        
        String[] cropItems = getResources().getStringArray(R.array.ary_crops_main);
		this.setListAdapter(new AgroArrayAdapter(this, cropItems));

		ListView cropListView = getListView();
		cropListView.setTextFilterEnabled(true);

		cropListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent cropSearchIntent = new Intent();
		    	switch (position) {
		    	case LIST_CROP_TG_SEARCH:
					cropSearchIntent.setClass(Crops.this, CropSearch.class);
					startActivityForResult(cropSearchIntent,LIST_CROP_TG_SEARCH);
					break;
		    	case LIST_AREA_SEARCH:
					cropSearchIntent.setClass(Crops.this, AreaSearch.class);
					startActivityForResult(cropSearchIntent,LIST_AREA_SEARCH);
					break;
		    	case LIST_FNAME_SEARCH:
					cropSearchIntent.setClass(Crops.this, FarmerSearch.class);
					startActivityForResult(cropSearchIntent,LIST_FNAME_SEARCH);
					break;
				case LIST_PROPERTY_SEARCH: 
					cropSearchIntent.setClass(Crops.this, PIDSearch.class);
					startActivityForResult(cropSearchIntent,LIST_PROPERTY_SEARCH);
					break;
				case LIST_LOCATION_SEARCH:
					cropSearchIntent.setClass(Crops.this, LocationSearch.class);
					startActivityForResult(cropSearchIntent,LIST_LOCATION_SEARCH);
					break;
				case LIST_DETAILED_SEARCH:
					cropSearchIntent.setClass(Crops.this, DetailSearch.class);
					startActivityForResult(cropSearchIntent,LIST_DETAILED_SEARCH);
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
        
		if( resultCode == RESULT_OK) {
			ContentValues searchParams = (ContentValues) intent.getParcelableExtra(SEARCH_PARAMS);
			new apiRequest().execute(searchParams);
        } else if( resultCode == RESULT_CANCELED) {
    		//Toast.makeText(Crops.this, "Error: There was a problem requesting search", //Toast.LENGTH_SHORT).show();
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
			agroApp.getQueryData(CROPS_SEARCH, searchParams[0]);
			
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

			searchResultIntent.setClass(Crops.this, ResultView.class);
			startActivity(searchResultIntent);
			finish();
			
		}
	}
}
