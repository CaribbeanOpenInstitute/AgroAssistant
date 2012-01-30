package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PIDSearch extends Activity{
	private ContentValues searchInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.pid_search);
	    
	    
	    final EditText edt_farmer_search = (EditText) findViewById(R.id.fsearch_input);
	    final Button btn_search = (Button) findViewById(R.id.btn_farmer_search);
	    
	    
//Returning query to farmer activity
        
	    final Intent returnIntent = new Intent();
	    searchInput = new ContentValues();
		
	    btn_search.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				String userInput = edt_farmer_search.getText().toString();
				
				if ( userInput.equals("") || userInput.length() < 1 ){
					Toast.makeText(PIDSearch.this, "Please enter a Property ID ", Toast.LENGTH_SHORT).show();
				}else{
					userInput = edt_farmer_search.getText().toString();
					//Toast.makeText(PIDSearch.this, "Property ID: " + userInput, Toast.LENGTH_SHORT).show();
					
					/*returnIntent.putExtra("selection", "1");
					returnIntent.putExtra("column", "Property ID");
					returnIntent.putExtra("value", userInput );*/
					
					searchInput.put(FARM_ID, userInput);
					returnIntent.putExtra(SEARCH_PARAMS, searchInput);
					returnIntent.putExtra(SEARCH_TYPE, PROPERTY_SEARCH);
					
					setResult(RESULT_OK,returnIntent);    	
			    	finish();
					
				}
			}
	    });
	    
	 }
	
	
	
	

}