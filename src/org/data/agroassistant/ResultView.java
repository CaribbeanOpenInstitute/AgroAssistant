package org.data.agroassistant;

import android.app.ListActivity;
import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.DBConstants.*;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ResultView extends ListActivity {
	private AgroApplication agroApp;
	//private AgroAssistantDB agroDB;
	private Cursor resultsCursor;
	private int searchType;
	private int searchCode;
	//private String searchParams;
	
	private static final int[] FARMER_TO    = {android.R.id.text1, R.id.text2, android.R.id.text2};
	
	private static final int[] DTL_FARM_TO      = {R.id.dtl_result_propertyid, R.id.dtl_result_district, R.id.dtl_result_extension, R.id.dtl_result_parish};
	private static final int[] FARM_TO      = {R.id.farm_result_farmerFname, R.id.farm_result_farmerLname, R.id.farm_result_propertyid, R.id.farm_result_district, R.id.farm_result_extension, R.id.farm_result_parish};
	private static final int[] CROP_TO      = {R.id.farm_result_farmerFname, R.id.farm_result_farmerLname, R.id.farm_result_district, R.id.farm_result_extension, R.id.farm_result_parish,R.id.result_crop_group, R.id.result_crop_type, R.id.result_crop_propertyid, R.id.result_crop_area, R.id.result_crop_count, R.id.result_crop_date};
	
	
	private static final String[] DTL_FARM_FROM = {FARM_ID, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH};
	private static final String[] FARM_FROM = {FARMER_FNAME, FARMER_LNAME, FARM_ID, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH};
	private static final String[] CROP_FROM = {FARMER_FNAME, FARMER_LNAME, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH, CROP_GROUP, CROP_TYPE, CROP_FARM_ID, CROP_AREA, CROP_COUNT, CROP_DATE};
	
	
	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @Intent Params: 
	 * 	(int) 		searchType	 	Type of search performed {Farmer,Farm,Crop,Price}
	 * 	(String)	searchParams 	String of WHERE clause for query
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_search);
		Bundle searchResultBundle = getIntent().getExtras();
		//agroDB = new AgroAssistantDB(this);
		searchCode = searchResultBundle.getInt(SEARCH_CODE);
        searchType = searchResultBundle.getInt(SEARCH_TYPE);
        String searchParams = searchResultBundle.getString(SEARCH_PARAMS);
        
      //Refactor
        agroApp = ((AgroApplication)getApplication());
      /////////	
		
        //searchType = FARMER_SEARCH;
        //searchParams = FARMER_ID + "=201001261";
        try {
	        switch(searchCode) {
	        case(FARMER_SEARCH): 
	        	resultsCursor = agroApp.agroData.farmerRawQuery(FARMERS_TABLE, FROM_S_FARMERS_FARMS, searchParams);
	    		break;
	        case (FARM_SEARCH):
	        	if(searchType != DETAILED_SEARCH) 
	        		resultsCursor = agroApp.agroData.farmerRawQuery( FARMS_TABLE, FROM_S_FARMS_FARMERS, searchParams);
	        	else
	        		resultsCursor = agroApp.agroData.cropRawQuery( CROPS_TABLE, FROM_S_CROPS, searchParams);
	        	break;
	        case (FARMER_FARM_SEARCH):
	        	resultsCursor = agroApp.agroData.farmerRawQuery( FARMS_TABLE, FROM_S_FARMS_FARMERS, searchParams);
	        	break;
	        case (CROP_SEARCH):
	        	resultsCursor = agroApp.agroData.cropRawQuery( CROPS_TABLE, FROM_S_CROPS, searchParams);
	        	break;
	        case (FARM_CROP_SEARCH):
	        	resultsCursor = agroApp.agroData.cropRawQuery(CROPS_TABLE, FROM_S_CROPS, searchParams);
	        	break;
	        case (PRICE_SEARCH):
	        	//resultsCursor = agroDB.rawQuery(PRICES_TABLE, FROM_S_PRICES, searchParams);
	        	break;
	        default:
	        	break;
	        }
	        
	        startManagingCursor(resultsCursor);
	        showResults(resultsCursor);
        } finally {
        	//agroDB.close();
        }
	}

	private void showResults(Cursor cursor){
		SimpleCursorAdapter results = null;
		//Log.d("AgroAssistant", "showResults: Cusor contains " + cursor.getCount() + " record(s)");
		RelativeLayout bar;
		switch(searchCode) {
        case(FARMER_SEARCH): 
        	results = new SimpleCursorAdapter(this, R.layout.simple_list, cursor, new String[] {FARMER_FNAME, FARMER_LNAME, FARMER_ID}, FARMER_TO);
    		break;
        case (FARM_SEARCH):
        	results = new SimpleCursorAdapter(this, R.layout.result_farm_row, cursor, FARM_FROM, FARM_TO);
        	break;
        case (FARMER_FARM_SEARCH):
        	bar = (RelativeLayout) findViewById(R.id.menu_bar);
    		bar.setVisibility(8);
    		results = new SimpleCursorAdapter(this, R.layout.dtl_farm_row, cursor, DTL_FARM_FROM, DTL_FARM_TO);
        	break;
        case (CROP_SEARCH):
        	results = new SimpleCursorAdapter(this, R.layout.result_crop_row, cursor, CROP_FROM, CROP_TO);
        	break;
        case(FARM_CROP_SEARCH):
        	bar = (RelativeLayout) findViewById(R.id.menu_bar);
			bar.setVisibility(8);
        	results = new SimpleCursorAdapter(this, R.layout.result_crop_row, cursor, CROP_FROM, CROP_TO);
    		break;
    	
        case (PRICE_SEARCH):
        	//results = new SimpleCursorAdapter(this, R.layout.result_price_row, cursor, PRICE_FROM, PRICE_TO);
        	break;
        default:
        	break;
        }
		setListAdapter(results);
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent detailViewIntent = new Intent();
		Bundle detailViewData   = new Bundle();
        
        Cursor cursor   = (Cursor) getListAdapter().getItem(position);	//Get Cursor at row position
        
		switch(searchCode) {
        case(FARMER_SEARCH):
			detailViewData.putString(FARMER_ID , cursor.getString(cursor.getColumnIndex(FARMER_ID )));
			detailViewIntent.setClass(this, FarmerView.class);
    		break;
        case (FARM_SEARCH):
        	detailViewData.putString(FARM_ID , cursor.getString(cursor.getColumnIndex(FARM_ID )));
        	detailViewIntent.setClass(this, FarmView.class);
        	break;
        case (FARMER_FARM_SEARCH):
        	detailViewData.putString(FARM_ID , cursor.getString(cursor.getColumnIndex(FARM_ID )));
        	detailViewIntent.setClass(this, FarmView.class);
        	break;
        case (FARM_CROP_SEARCH):
        	detailViewData.putString(FARM_ID , cursor.getString(cursor.getColumnIndex(FARM_ID )));
			detailViewIntent.setClass(this, FarmView.class);
        	break;
        	
        case (CROP_SEARCH):
        	detailViewData.putString(FARM_ID , cursor.getString(cursor.getColumnIndex(FARM_ID )));
			detailViewIntent.setClass(this, FarmView.class);
        	break;
        case (PRICE_SEARCH):
        	detailViewIntent.setClass(this, FarmView.class);
        	break;
        default:
        	break;
        }
		
		detailViewIntent.putExtras(detailViewData);
		startActivity(detailViewIntent);
		finish();
    }
}
