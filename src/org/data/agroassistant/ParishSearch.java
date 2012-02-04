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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class ParishSearch extends Activity {
	private ContentValues searchInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.parish_search);

		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.parishes_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			final Intent returnIntent = new Intent();
			searchInput = new ContentValues();
			
			String parish = parent.getItemAtPosition(pos).toString();
			searchInput.put(PRICE_PARISH, parish);
			// Toast.makeText(parent.getContext(), "The parish is " + parish,
			// Toast.LENGTH_LONG).show();

			returnIntent.putExtra(SEARCH_PARAMS, searchInput);
			returnIntent.putExtra(SEARCH_TYPE, CROP_PARISH_SEARCH);
			
			// setResult(RESULT_OK,returnIntent);
			// finish();

		}

		@SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

}
