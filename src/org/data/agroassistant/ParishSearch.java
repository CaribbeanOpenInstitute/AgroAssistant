package org.data.agroassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class ParishSearch extends Activity{
	private String parish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.parish_search);

	    Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.parishes_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	    
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
            View view, int pos, long id) {
        	parish = parent.getItemAtPosition(pos).toString();
          Toast.makeText(parent.getContext(), "The parish is " + parish, Toast.LENGTH_LONG).show();
          Intent returnIntent = new Intent();
          
          returnIntent.putExtra("Parish", parish);
          setResult(RESULT_OK,returnIntent);    	
          finish();
          
        }

        @SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
    }
	
	
}
