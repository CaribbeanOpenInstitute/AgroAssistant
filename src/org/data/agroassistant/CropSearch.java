package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class CropSearch extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.crop_search);
	    
	    final RadioButton radio_cropType = (RadioButton) findViewById(R.id.rdo_cropType);
	    final RadioButton radio_cropGroup = (RadioButton) findViewById(R.id.rdo_cropGroup);
	    radio_cropType.setOnClickListener(radio_listener);
	    radio_cropGroup.setOnClickListener(radio_listener);
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	        RadioButton rb = (RadioButton) v;
	        Toast.makeText(CropSearch.this, rb.getText(), Toast.LENGTH_SHORT).show();
	        
	        //Build API query(?)
	        
	        
	        //Returning query to farmer activity
	        Intent returnIntent = new Intent();
	    	returnIntent.putExtra("Selected Location",rb.getText());	//Data to be returned
	    	setResult(RESULT_OK,returnIntent);    	
	    	finish();
	    }
	};
	
	

}
