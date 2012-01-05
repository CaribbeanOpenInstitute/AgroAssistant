package org.data.agroassistant;

import java.util.Arrays;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FarmerDetails extends Activity{
	private String firstname, lastname, id, size;
	private AgroAssistantDB agroDB;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//TODO Implement Farmer tab UI
		//TODO Grey and black UI icons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_info);
        
        agroDB = new AgroAssistantDB(this);

        final TextView farmername = (TextView) findViewById(R.id.info_farmername);
        final TextView farmerid   = (TextView) findViewById(R.id.info_farmerid);
        final TextView farmersize   = (TextView) findViewById(R.id.info_farmersize);
        
        Bundle farmerinfo = getIntent().getExtras();
        
        
        id = farmerinfo.getString("farmerid" );
        
        try {
        	Cursor farmer = agroDB.getFarmer(id);
        	startManagingCursor(farmer);
            farmer.moveToFirst();
            //Log.d("AgroAssistant", "Farmer columns " + Arrays.toString(farmer.getColumnNames()));
            //Log.d("AgroAssistant", "Farmer ID " + farmer.getString(1));
            //firstname =  farmerinfo.getString("firstname");
            //lastname  =  farmerinfo.getString("lastname" );
            
            firstname =  farmer.getString(farmer.getColumnIndex("firstname"));
            lastname  =  farmer.getString(farmer.getColumnIndex("lastname"));
            //size	  =  farmer.getString(farmer.getColumnIndex("farmersize"));
            farmer.close();
        } catch (Exception e) {
        	Log.e("AgroAssistant", "FarmerView -> Farmer Info: " + e.toString());
        	firstname = lastname = size = " ";
        	Toast.makeText(FarmerDetails.this, "Sorry, unable to display requested farmer record", Toast.LENGTH_LONG).show();
        }
        
        farmername.setText(firstname + " " + lastname);
        farmerid.setText(id);
        farmersize.setText(size);
    }
}
