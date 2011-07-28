package org.data.agroassistant;

import static android.provider.BaseColumns._ID;
import static org.data.agroassistant.Constants.*;
import static org.data.agroassistant.Constants.FARMER_FNAME;
import static org.data.agroassistant.Constants.FARMER_ID;
import static org.data.agroassistant.Constants.FARMER_LNAME;
import static org.data.agroassistant.Constants.FARMER_SEARCH;
import static org.data.agroassistant.Constants.FARM_SEARCH;
import static org.data.agroassistant.Constants.PRICE_SEARCH;

import java.util.Arrays;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ResultView extends ListActivity {
	
	private AgroAssistantDB agroDB;
	private Cursor resultsCursor;
	private int searchType;
	private String searchParams;
	
	private static final int[] FARMER_TO = {android.R.id.text1, R.id.text2, android.R.id.text2};
	
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
	        	//resultsCursor = agroDB.rawQuery(agroDB, FARMS_TABLE, FROM_S_FARMS, searchParams);
	        	break;
	        case (CROP_SEARCH):
	        	//resultsCursor = agroDB.rawQuery(agroDB, CROPS_TABLE, "FROM_S_CROPS", searchParams);
	        	break;
	        case (PRICE_SEARCH):
	        	//resultsCursor = agroDB.rawQuery(PRICES_TABLE, "FROM_PRICES", searchParams);
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
        	//results = new SimpleCursorAdapter(this, R.layout.farm_row, cursor, new String[] {FEED_TITLE}, TO);
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
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        String fid = cursor.getString(cursor.getColumnIndex(_ID));
        String fname = cursor.getString(cursor.getColumnIndex(FARMER_FNAME));
        String lname = cursor.getString(cursor.getColumnIndex(FARMER_LNAME));

		Toast.makeText(this, "You selected: " + fname + " " + lname, Toast.LENGTH_SHORT).show();
		/*
        Intent i = new Intent(this, ArticlesList.class);
        i.putExtra("feed_id", cursor.getString(cursor.getColumnIndex(_ID)));
        i.putExtra("title", cursor.getString(cursor.getColumnIndex(FEED_TITLE)));
        i.putExtra("url", cursor.getString(cursor.getColumnIndex(FEED_URL)));        
        startActivityForResult(i, ACTIVITY_VIEW);
        */
    }
}
