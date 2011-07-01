package org.data.agroassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class agroAssistant extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }
    
    public void onActionOneClick(View v) {
        Toast.makeText(this, "Action 1", Toast.LENGTH_SHORT).show();
    }

    public void onActionTwoClick(View v) {
        Toast.makeText(this, "Action 2", Toast.LENGTH_SHORT).show();
    }

    public void onActionThreeClick(View v) {
        Toast.makeText(this, "Action 3", Toast.LENGTH_SHORT).show();
    }

    public void onActionFourClick(View v) {
        Toast.makeText(this, "Action 4", Toast.LENGTH_SHORT).show();
    }
    
}