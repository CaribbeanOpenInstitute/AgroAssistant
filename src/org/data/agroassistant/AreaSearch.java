package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.*;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;

public class AreaSearch extends Activity {
	private final String TAG = AreaSearch.class.getSimpleName();
	private ContentValues searchInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.area_search);
	    
	    final RadioGroup rdg_area = (RadioGroup) findViewById(R.id.rdgrp_asearch);
	    final AutoCompleteTextView edt_farmer_search = (AutoCompleteTextView) findViewById(R.id.edt_area_search);
	    final Button btn_search = (Button) findViewById(R.id.btn_area_search);
	    
	    //AutoComplete for Area
	    AgroApplication agroApp = ((AgroApplication)getApplication());
	    String[] saArea = agroApp.agroData.getArea();
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list, saArea);
	    edt_farmer_search.setAdapter(adapter);

	    
	    final Intent returnIntent = new Intent();
	    searchInput = new ContentValues();
	    
		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String userInput = "";
				userInput = edt_farmer_search.getText().toString();
				switch(rdg_area.getCheckedRadioButtonId()) {
					case R.id.rdo_parish:
						searchInput.put(FARM_PARISH, userInput);
						returnIntent.putExtra(SEARCH_TYPE, PARISH_SEARCH);
						returnIntent.putExtra(SEARCH_PARAMS, searchInput);
						
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case R.id.rdo_extension:
						searchInput.put(FARM_EXTENSION, userInput);
						returnIntent.putExtra(SEARCH_TYPE, EXTENSION_SEARCH);
						returnIntent.putExtra(SEARCH_PARAMS, searchInput);
						
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case R.id.rdo_district:
						searchInput.put(FARM_DISTRICT, userInput);
						returnIntent.putExtra(SEARCH_TYPE, DISTRICT_SEARCH);
						returnIntent.putExtra(SEARCH_PARAMS, searchInput);
						
						setResult(RESULT_OK,returnIntent);    	
				    	finish();
						break;
					case -1:
						//Toast.makeText(AreaSearch.this, "Please check area to search", //Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
	}
}
