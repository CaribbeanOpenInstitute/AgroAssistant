package org.data.agroassistant;

//import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
//import android.content.Intent;
//import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 *
 * @author Gebre
 */
public class Crops extends ListActivity{

	static final int CROP_SEARCH = 20;
	static final int PARISH_SEARCH = 21;
	static final int FNAME_SEARCH = 22;
	static final int PROPERTY_SEARCH = 23;
	static final int LOCATION_SEARCH = 24;
	static final int DETAILED_SEARCH = 25;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_main);

        String[] cropItems = getResources().getStringArray(R.array.ary_crops_main);
		this.setListAdapter(new AgroArrayAdapter(this, cropItems));
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.listTitle, farmerItems));

		ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		      // When clicked, show a toast with the TextView text
		    	
		    	Intent cropSearchIntent = new Intent();
		    	
		    	switch (position) {
		    	case 0:
					Toast.makeText(Crops.this, "You selected to Search by Crop Type or Crop Group", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, CropSearch.class);
					startActivityForResult(cropSearchIntent,CROP_SEARCH);
					break;
		    	case 1:
					Toast.makeText(Crops.this, "You selected to Search by Parish", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, AreaSearch.class);
					startActivityForResult(cropSearchIntent,PARISH_SEARCH);
					break;
		    	case 2:
					Toast.makeText(Crops.this, "You selected to Search by Farmer Name", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, FarmerSearch.class);
					startActivityForResult(cropSearchIntent,FNAME_SEARCH);
					//Intent farmerIntent = new Intent(Farmers.this, FarmerNameSearch.class);
					//startActivity(farmerIntent);
					break;
				case 3: 
					Toast.makeText(Crops.this, "You selected to Search by Property ID", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, PIDSearch.class);
					startActivityForResult(cropSearchIntent,PROPERTY_SEARCH);
					break;
				case 4:
					Toast.makeText(Crops.this, "You selected to Search by Location", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, LocationSearch.class);
					startActivityForResult(cropSearchIntent,LOCATION_SEARCH);
					break;
				case 5:
					Toast.makeText(Crops.this, "You selected to Search by Detailed search", Toast.LENGTH_SHORT).show();
					cropSearchIntent.setClass(Crops.this, FarmerView.class);
					startActivityForResult(cropSearchIntent,DETAILED_SEARCH);
					break; 
				default:
					Toast.makeText(Crops.this, "Error: The option you selected does not exist", Toast.LENGTH_SHORT).show();
					break;
				}
		      
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
        		//intent.
        		Toast.makeText(Crops.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        	}
        	else if( resultCode == RESULT_CANCELED) {
        		Toast.makeText(Crops.this, "Error: There was a problem requesting search", Toast.LENGTH_SHORT).show();
        		
        	}
        }
        
    }
}
