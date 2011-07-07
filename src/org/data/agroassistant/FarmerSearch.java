package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class FarmerSearch extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.farmer_search);
	    
	    final RadioButton radio_parish = (RadioButton) findViewById(R.id.rdo_fname);
	    final RadioButton radio_extension = (RadioButton) findViewById(R.id.rdo_fid);
	    radio_parish.setOnClickListener(radio_listener);
	    radio_extension.setOnClickListener(radio_listener);
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	        RadioButton rb = (RadioButton) v;
	        Toast.makeText(FarmerSearch.this, rb.getText(), Toast.LENGTH_SHORT).show();
	        
	        //Build API query(?)
	        
	        
	        //Returning query to farmer activity
	        Intent returnIntent = new Intent();
	    	returnIntent.putExtra("Selected Location",rb.getText());	//Data to be returned
	    	setResult(RESULT_OK,returnIntent);    	
	    	finish();
	    }
	};
	
	

}
