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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_details);
		initMapView();
		
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
