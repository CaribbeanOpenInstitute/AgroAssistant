package org.data.agroassistant;

import android.app.ListActivity;
import android.os.Bundle;

public class ResultView extends ListActivity {
	
	private static final int FARMER_SEARCH = 0;
	private static final int FARM_SEARCH = 1;
	private static final int CROP_SEARCH = 2;
	private static final int PRICE_SEARCH = 3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.result_search);
		Bundle b = getIntent().getExtras();
        final int searchType = b.getInt("searchType");
        final String searchResponse = b.getString("searchType");
        
        switch(b.getInt("searchType")) {
        case(FARMER_SEARCH): 
        	
    		break;
        case (FARM_SEARCH):
        	break;
        case (CROP_SEARCH):
        	break;
        case (PRICE_SEARCH):
        	break;
        default:
        	break;
        }
	}

}
