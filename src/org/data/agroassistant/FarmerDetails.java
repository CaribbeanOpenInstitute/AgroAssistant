package org.data.agroassistant;

import static org.data.agroassistant.DBConstants.FARMER_FNAME;
import static org.data.agroassistant.DBConstants.FARMER_LNAME;
import static org.data.agroassistant.DBConstants.FARMER_SIZE;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FarmerDetails extends Activity{
	private String firstname, lastname, id, size;
	private AgroApplication agroApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//TODO Implement Farmer tab UI
		//TODO Grey and black UI icons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_info);
        
        agroApp = ((AgroApplication)getApplication());

        final TextView farmername = (TextView) findViewById(R.id.info_farmername);
        final TextView farmerid   = (TextView) findViewById(R.id.info_farmerid);
        final TextView farmersize   = (TextView) findViewById(R.id.info_farmersize);
        
        Bundle farmerinfo = getIntent().getExtras();
        
        id = farmerinfo.getString("farmerid" );
        
        try {
        	Cursor farmer = agroApp.agroData.getFarmer(id);
        	startManagingCursor(farmer);
            farmer.moveToFirst();
            
            firstname =  farmer.getString(farmer.getColumnIndex(FARMER_FNAME));
            lastname  =  farmer.getString(farmer.getColumnIndex(FARMER_LNAME));
            size	  =  farmer.getString(farmer.getColumnIndex(FARMER_SIZE));
            farmer.close();
            
        } catch (Exception e) {
        	Log.e("AgroAssistant", "FarmerView -> Farmer Info: " + e.toString());
        	firstname = lastname = size = " ";
        	Toast.makeText(FarmerDetails.this, "Sorry, unable to display requested farmer record", Toast.LENGTH_LONG).show();
        }
        
        farmername.setText(AgroApplication.UppercaseFirstLetters(firstname + " " + lastname)); 
        farmerid.setText(id);
        farmersize.setText(AgroApplication.UppercaseFirstLetters(size));
    }
}
