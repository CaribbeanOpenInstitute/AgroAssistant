package org.data.agroassistant;

import static org.data.agroassistant.AgroConstants.CROP_PARISH_SEARCH;
import static org.data.agroassistant.AgroConstants.SEARCH_PARAMS;
import static org.data.agroassistant.AgroConstants.SEARCH_TYPE;
import static org.data.agroassistant.DBConstants.PRICE_CROPTYPE;
import static org.data.agroassistant.DBConstants.PRICE_PARISH;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CropParishSearch extends Activity {
	private ContentValues searchInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_parish_search);
		searchInput = new ContentValues();

		final AutoCompleteTextView edt_crop_search = (AutoCompleteTextView) findViewById(R.id.edt_crop_search);
		final Button btn_search = (Button) findViewById(R.id.btn_crop_search);

		// Logic for the Parishes Spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
				this, R.array.parishes_array,
				android.R.layout.simple_spinner_item);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spAdapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		// Crop Edit box Auto complete logic
		AgroApplication agroApp = ((AgroApplication) getApplication());
		String[] atxtCrop = agroApp.agroData.getCrop();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.autocomplete_list, atxtCrop);
		edt_crop_search.setAdapter(adapter);

		btn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();

				String userInput = edt_crop_search.getText().toString();

				if (userInput.equals("") || userInput.length() < 1) {
					Toast.makeText(CropParishSearch.this,
							"Please enter a Crop group or name",
							Toast.LENGTH_SHORT).show();
				} else {
					searchInput.put(PRICE_CROPTYPE, userInput);
					returnIntent.putExtra(SEARCH_PARAMS, searchInput);
					returnIntent.putExtra(SEARCH_TYPE, CROP_PARISH_SEARCH);
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}
		});
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			String parish = parent.getItemAtPosition(pos).toString();
			searchInput.put(PRICE_PARISH, parish);
			Log.d("AgroAssistant", "Changed Parish to " + parish);

		}

		@SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

}
