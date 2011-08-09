package org.data.agroassistant;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class FarmDetails extends MapActivity{
	private MapView mapView;
	private MapController mapController;
	private LocationManager mgr;
	private GeoPoint currentPoint;
	private Location currentLoc;

	//Map Items
	private List<Overlay> mapOverlays;
	private OverlayItem overlayitem;
	Drawable drawable;
	ItemizedOverlay itemizedoverlay;
	private String firstname, lastname, farmid, farmsize, farmer_id, parishStr, ext, dist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_details);
		initMapView();
		
		
		final TextView farmername     = (TextView) findViewById(R.id.dtl_farm_farmername);
        final TextView farmerid       = (TextView) findViewById(R.id.dtl_farm_farmerid);
        final TextView propertysize   = (TextView) findViewById(R.id.dtl_farm_size);
        final TextView propertyid     = (TextView) findViewById(R.id.dtl_farm_id);
        final TextView parish         = (TextView) findViewById(R.id.dtl_farm_parish);
        final TextView extension      = (TextView) findViewById(R.id.dtl_farm_ext);
        final TextView district       = (TextView) findViewById(R.id.dtl_farm_dist);
        
        Bundle farminfo = getIntent().getExtras();
        
        firstname   =  farminfo.getString("firstname"  );
        lastname    =  farminfo.getString("lastname"   );
        farmer_id   =  farminfo.getString("farmerid"   );
        farmsize	=  farminfo.getString("size"       );
		farmid		=  farminfo.getString("farmid"     );
		parishStr 	=  farminfo.getString("parish"     );
		ext			=  farminfo.getString("extension"  );
		dist		=   farminfo.getString("district"  );
		
		
		farmername.setText(firstname + " " + lastname);
        farmerid.setText(farmer_id);
        propertysize.setText(farmsize);
        propertyid.setText(farmid);
        parish.setText(parishStr);
        extension.setText(ext);
        district.setText(dist);
        
        
	}	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.farmDetailsMapView);
		mapController = mapView.getController();
		mapController.setZoom(16);
	    mapView.setBuiltInZoomControls(true);
	    
	    
	    //itemizedoverlay.onTap(index)
	    
	    mapOverlays = mapView.getOverlays();
	}

}
