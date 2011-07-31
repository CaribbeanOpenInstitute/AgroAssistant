package org.data.agroassistant;

import static org.data.agroassistant.Constants.*;
import android.app.ListActivity;
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
	
	private AgroAssistantDB agroDB;
	private Cursor resultsCursor;
	private int searchType;
	private String searchParams;
	
	private static final int[] FARMER_TO    = {android.R.id.text1, R.id.text2, android.R.id.text2};
	
	private static final int[] DTL_FARM_TO      = {R.id.dtl_result_propertyid, R.id.dtl_result_district, R.id.dtl_result_extension, R.id.dtl_result_parish};
	private static final int[] FARM_TO      = {R.id.farm_result_farmerFname, R.id.farm_result_farmerLname, R.id.farm_result_propertyid, R.id.farm_result_district, R.id.farm_result_extension, R.id.farm_result_parish};
	private static final int[] CROP_TO      = {R.id.result_crop_group, R.id.result_crop_type, R.id.result_crop_propertyid, R.id.result_crop_area, R.id.result_crop_count, R.id.result_crop_date};
	
	
	private static final String[] DTL_FARM_FROM = {FARM_ID, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH};
	private static final String[] FARM_FROM = {FARMER_FNAME, FARMER_LNAME, FARM_ID, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH};
	private static final String[] CROP_FROM = {CROP_GROUP, CROP_TYPE, CROP_FARM_ID, CROP_AREA, CROP_COUNT, CROP_DATE};
	
	
	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @Intent Params: 
	 * 	(int) 		searchType	 	Type of search performed {Farmer,Farm,Crop,Price}
	 * 	(String)	searchParams 	String of WHERE clause for query
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_search);
		Bundle searchResultBundle = getIntent().getExtras();
		agroDB = new AgroAssistantDB(this);
        searchType = searchResultBundle.getInt("searchType");
        //final String searchResponse = searchResultBundle.getString("searchResponse");
        final String searchParams = searchResultBundle.getString("searchParams");
        
		
		
        
        /*
         * Need
         * 	Cursor
         * 	CursorAdapter
         * 	SQL query (params)
         * 	SQL (from)
         */
        //searchType = FARMER_SEARCH;
        //searchParams = FARMER_ID + "=201001261";
        try {
	        switch(searchType) {
	        case(FARMER_SEARCH): 
	        	//resultsCursor = agroDB.rawQuery(FARMERS_TABLE, FROM_S_FARMERS, searchParams);
	        	resultsCursor = agroDB.farmerRawQuery(FARMERS_TABLE, FROM_S_FARMERS_FARMS, searchParams);
	    		break;
	        case (FARM_SEARCH):
	        	resultsCursor = agroDB.farmerRawQuery( FARMS_TABLE, FROM_S_FARMS_FARMERS, searchParams);
	        	break;
	        case (FARMER_FARM_SEARCH):
	        	resultsCursor = agroDB.rawQuery( FARMS_TABLE, FROM_S_FARMS, searchParams);
	        	break;
	        case (CROP_SEARCH):
	        	resultsCursor = agroDB.rawQuery( CROPS_TABLE, FROM_S_CROPS, searchParams);
	        	break;
	        case (FARM_CROP_SEARCH):
	        	resultsCursor = agroDB.rawQuery(CROPS_TABLE, FROM_S_CROPS, searchParams);
	        	break;
	        case (PRICE_SEARCH):
	        	resultsCursor = agroDB.rawQuery(PRICES_TABLE, "FROM_PRICES", searchParams);
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

		switch(searchType) {
        case(FARMER_SEARCH): 
        	//adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] {FARMER_ID, FARMER_LNAME}, new int[] {android.R.id.text1, android.R.id.text2});
        	results = new SimpleCursorAdapter(this, R.layout.simple_list, cursor, new String[] {FARMER_FNAME, FARMER_LNAME, FARMER_ID}, FARMER_TO);
        	//results = new SimpleCursorAdapter(this, R.layout.farmer_row, cursor, new String[] {_ID, FARMER_ID, FARMER_FNAME, FARMER_LNAME, FARMER_SIZE}, new int[] {R.id.txt_row_id, R.id.txt_farmer_id, R.id.txt_farmer_fname, R.id.txt_farmer_lname, R.id.txt_farmer_size});
    		break;
        case (FARM_SEARCH):
        	results = new SimpleCursorAdapter(this, R.layout.result_farm_row, cursor, FARM_FROM, FARM_TO);
        	break;
        case (FARMER_FARM_SEARCH):
        	RelativeLayout bar = (RelativeLayout) findViewById(R.id.menu_bar);
    		bar.setVisibility(8);
    		results = new SimpleCursorAdapter(this, R.layout.dtl_farm_row, cursor, DTL_FARM_FROM, DTL_FARM_TO);
        	break;
        case (CROP_SEARCH):
        	results = new SimpleCursorAdapter(this, R.layout.result_crop_row, cursor, CROP_FROM, CROP_TO);
        	break;
        case (PRICE_SEARCH):
        	//results = new SimpleCursorAdapter(this, R.layout.price_row, cursor, new String[] {FEED_TITLE}, TO);
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
        
		//Get Cursor at row position
        Cursor cursor   = (Cursor) getListAdapter().getItem(position);
        
		switch(searchType) {
        case(FARMER_SEARCH):
        	detailViewData.putString("firstname", cursor.getString(cursor.getColumnIndex(FARMER_FNAME)));
			detailViewData.putString("lastname" , cursor.getString(cursor.getColumnIndex(FARMER_LNAME)));
			detailViewData.putString("farmerid" , cursor.getString(cursor.getColumnIndex(FARMER_ID )));
			detailViewData.putString("size"     , cursor.getString(cursor.getColumnIndex(FARMER_SIZE   )));
			detailViewIntent.setClass(this, FarmerView.class);
    		break;
        case (FARM_SEARCH):
        	detailViewData.putString("firstname", cursor.getString(cursor.getColumnIndex(FARMER_FNAME)));
			detailViewData.putString("lastname" , cursor.getString(cursor.getColumnIndex(FARMER_LNAME)));
			detailViewData.putString("farmerid" , cursor.getString(cursor.getColumnIndex(FARMER_ID )));
			detailViewData.putString("farmid" , cursor.getString(cursor.getColumnIndex(FARM_ID )));
			detailViewData.putString("parish"     , cursor.getString(cursor.getColumnIndex(FARM_PARISH   )));
			detailViewData.putString("extension"     , cursor.getString(cursor.getColumnIndex(FARM_EXTENSION   )));
			detailViewData.putString("district" , cursor.getString(cursor.getColumnIndex(FARM_DISTRICT )));
			detailViewData.putString("lat"     , cursor.getString(cursor.getColumnIndex(FARM_LAT   )));
			detailViewData.putString("long"     , cursor.getString(cursor.getColumnIndex(FARM_LONG   )));
        	detailViewIntent.setClass(this, FarmView.class);
        	break;
        case (FARMER_FARM_SEARCH):
        	detailViewIntent.setClass(this, FarmView.class);
        	break;
        case (FARM_CROP_SEARCH):
        	break;
        case (CROP_SEARCH):
        	detailViewData.putString("farmid", cursor.getString(cursor.getColumnIndex(CROP_FARM_ID )));
        	detailViewData.putString("cropgroup", cursor.getString(cursor.getColumnIndex(CROP_GROUP)));
        	detailViewData.putString("croptype", cursor.getString(cursor.getColumnIndex(CROP_TYPE)));
        	detailViewData.putString("croparea", cursor.getString(cursor.getColumnIndex(CROP_AREA)));
			detailViewData.putString("cropcount", cursor.getString(cursor.getColumnIndex(CROP_COUNT)));
			detailViewData.putString("cropdate", cursor.getString(cursor.getColumnIndex(CROP_DATE)));
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
		
		/*
        Intent i = new Intent(this, ArticlesList.class);
        i.putExtra("feed_id", cursor.getString(cursor.getColumnIndex(_ID)));
        i.putExtra("title", cursor.getString(cursor.getColumnIndex(FEED_TITLE)));
        i.putExtra("url", cursor.getString(cursor.getColumnIndex(FEED_URL)));        
        startActivityForResult(i, ACTIVITY_VIEW);
        */
    }
}
