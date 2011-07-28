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
	
	private static final String[] DTL_FARM_FROM = {FARM_ID, FARM_DISTRICT, FARM_EXTENSION, FARM_PARISH};
	
	
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
	        	resultsCursor = agroDB.rawQuery(FARMERS_TABLE, FROM_S_FARMERS, searchParams);
	    		break;
	        case (FARM_SEARCH):
	        	resultsCursor = agroDB.rawQuery( FARMS_TABLE, FROM_S_FARMS, searchParams);
	        	break;
	        case (FARMER_FARM_SEARCH):
	        	
	        	resultsCursor = agroDB.rawQuery( FARMS_TABLE, FROM_S_FARMS, searchParams);
	        	break;
	        case (CROP_SEARCH):
	        	resultsCursor = agroDB.rawQuery( CROPS_TABLE, "FROM_S_CROPS", searchParams);
	        	break;
	        case (FARM_CROP_SEARCH):
	        	resultsCursor = agroDB.rawQuery(CROPS_TABLE, "FROM_S_CROPS", searchParams);
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
		Log.d("AgroAssistant", "showResults: Cusor contains " + cursor.getCount() + " record(s)");

		switch(searchType) {
        case(FARMER_SEARCH): 
        	//adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] {FARMER_ID, FARMER_LNAME}, new int[] {android.R.id.text1, android.R.id.text2});
        	results = new SimpleCursorAdapter(this, R.layout.simple_list, cursor, new String[] {FARMER_FNAME, FARMER_LNAME, FARMER_ID}, FARMER_TO);
        	//results = new SimpleCursorAdapter(this, R.layout.farmer_row, cursor, new String[] {_ID, FARMER_ID, FARMER_FNAME, FARMER_LNAME, FARMER_SIZE}, new int[] {R.id.txt_row_id, R.id.txt_farmer_id, R.id.txt_farmer_fname, R.id.txt_farmer_lname, R.id.txt_farmer_size});
    		break;
        case (FARM_SEARCH):
        	//results = new SimpleCursorAdapter(this, R.layout.farm_row, cursor, new String[] {FEED_TITLE}, TO);
        	break;
        case (FARMER_FARM_SEARCH):
        	//RelativeLayout bar = (RelativeLayout) findViewById(R.id.home_menu_bar);
    		//bar.setVisibility(8);
    		results = new SimpleCursorAdapter(this, R.layout.dtl_farm_row, cursor, DTL_FARM_FROM, DTL_FARM_TO);
        	break;
        case (CROP_SEARCH):
        	//results = new SimpleCursorAdapter(this, R.layout.crop_row, cursor, new String[] {FEED_TITLE}, TO);
        	break;
        case (PRICE_SEARCH):
        	//results = new SimpleCursorAdapter(this, R.layout.price_row, cursor, new String[] {FEED_TITLE}, TO);
        	break;
        default:
        	break;
        }
		setListAdapter(results);
	}
	
	/*
	public Cursor getFarmers() {
		SQLiteDatabase db = agroDB.getReadableDatabase();
		//Cursor cursor = db.query(FARMERS_TABLE, FROM_FARMERS, null, null, null, null, null);
		//Cursor cursor = db.query(FARMERS_TABLE, new String[] {"*"}, null, null, null, null, null);
		Cursor cursor = db.rawQuery("SELECT _id, farmerid, firstname, lastname, farmersize FROM farmers", null);
		startManagingCursor(cursor);
		
		Log.d("AgroAssistant", "getFarmers: Cusor contains " + cursor.getCount() + " record(s)");
    	Log.d("AgroAssistant", "getFarmers: Cusor contains " + cursor.getColumnCount() + " column(s)");
    	Log.d("AgroAssistant", "getFarmers: Cusor contains " + Arrays.toString(cursor.getColumnNames()));
		//Log.d("AgroAssistant", "ID " + cursor.getString(cursor.getColumnIndex("_id")));
    	//Log.d("AgroAssistant", "FID " + cursor.getString(cursor.getColumnIndex("farmerid")));
    	//Log.d("AgroAssistant", "FNAME " + cursor.getString(2));
    	//Log.d("AgroAssistant", "LNAME " + cursor.getString(cursor.getColumnIndex(FARMER_LNAME)));
		
		return cursor;
	}
	*/
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Cursor cursor   = (Cursor) getListAdapter().getItem(position);
        //String fid      = cursor.getString(cursor.getColumnIndex(_ID));
        String fname    = cursor.getString(cursor.getColumnIndex(FARMER_FNAME));
        String lname    = cursor.getString(cursor.getColumnIndex(FARMER_LNAME));
        String size     = cursor.getString(cursor.getColumnIndex(FARMER_SIZE ));
        String farmerid = cursor.getString(cursor.getColumnIndex(FARMER_ID   ));
        
		Toast.makeText(this, "You selected: " + fname + " " + lname, Toast.LENGTH_SHORT).show();
		
		Intent farmerintent = new Intent();
		Bundle farmerdata   = new Bundle();
		
		farmerdata.putString("firstname", fname    );
		farmerdata.putString("lastname" , lname    );
		farmerdata.putString("farmerid" , farmerid );
		farmerdata.putString("size"     , size     );
		
		farmerintent.putExtras(farmerdata);
		
		farmerintent.setClass(this, FarmerView.class);
		startActivity(farmerintent);
		finish();
		
		/*
        Intent i = new Intent(this, ArticlesList.class);
        i.putExtra("feed_id", cursor.getString(cursor.getColumnIndex(_ID)));
        i.putExtra("title", cursor.getString(cursor.getColumnIndex(FEED_TITLE)));
        i.putExtra("url", cursor.getString(cursor.getColumnIndex(FEED_URL)));        
        startActivityForResult(i, ACTIVITY_VIEW);*/
    }
}
