package org.data.agroassistant;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Prices extends ListActivity {
	static final int CROP_SEARCH = 0;
	static final int PARISH_SEARCH = 1;
	static final int LOCATION_SEARCH = 2;
	static final int DETAILED_SEARCH = 3;
	
	//private LayoutInflater mInflater;
	//private Vector<RowData> data;
	//RowData rd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prices_main);
		
		String[] priceItems = getResources().getStringArray(R.array.ary_prices_main);
		this.setListAdapter(new AgroArrayAdapter(this, priceItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					Toast.makeText(Prices.this, "You selected to Search by Crop Type", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, CropSearch.class);
					startActivityForResult(farmerSearchIntent,CROP_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					Toast.makeText(Prices.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, ParishSearch.class);
					startActivityForResult(farmerSearchIntent,PARISH_SEARCH);
					break;
				case 2:
					Toast.makeText(Prices.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Prices.this, LocationSearch.class);
					startActivityForResult(farmerSearchIntent,LOCATION_SEARCH);
					break;
				case 3:
					Toast.makeText(Prices.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(Prices.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
					break;
				}
		      // When clicked, show a toast with the TextView text
		      
		    }
		  });

	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //TODO: Receive query from search functions
        if (requestCode == CROP_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        	}
        	else if( resultCode == RESULT_CANCELED) {
        		Toast.makeText(Prices.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        	}
        }
        
    }
	
	

}
