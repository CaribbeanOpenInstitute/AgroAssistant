package org.data.agroassistant;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Farmers extends ListActivity {
	static final int FNAME_SEARCH = 0;
	static final int PARISH_SEARCH = 1;
	
	//private LayoutInflater mInflater;
	//private Vector<RowData> data;
	//RowData rd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmers_main);
		
		String[] farmerItems = getResources().getStringArray(R.array.ary_farmers_main);
		this.setListAdapter(new AgroArrayAdapter(this, farmerItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent farmerSearchIntent = new Intent();
		    	switch (position) {
				case 0:
					Toast.makeText(Farmers.this, "You selected to Search by Farmer name", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, FarmerSearch.class);
					startActivityForResult(farmerSearchIntent,FNAME_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 1:
					Toast.makeText(Farmers.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					farmerSearchIntent.setClass(Farmers.this, AreaSearch.class);
					startActivityForResult(farmerSearchIntent,PARISH_SEARCH);
					break;
				case 2:
					Toast.makeText(Farmers.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(Farmers.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(Farmers.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
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
        if (requestCode == FNAME_SEARCH) {
        	if( resultCode == RESULT_OK) {
        		//Call function to pull data from query
        	}
        	else if( resultCode == RESULT_CANCELED) {
        		Toast.makeText(Farmers.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        	}
        }
        
    }
	
	
}
