package com.amanda;

import static com.amanda.ConstantUtil.IMAGESWITCHER_HEIGHT;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SeeOnMapActivity extends MapActivity{
	
		
	MapView mapView;
	ArrayList<Integer> latint = new ArrayList<Integer>();
	ArrayList<Integer> lngint = new ArrayList<Integer>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);					//设置当前屏幕
		final Intent intent = getIntent();					//获取启动该Activity的Intent
	
		latint = intent.getIntegerArrayListExtra("latint");
		lngint = intent.getIntegerArrayListExtra("lngint");
		
	
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		
		final List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.location);
		final CustomItemizedOverlay itemizedOverlay =
		new CustomItemizedOverlay(drawable, this);
		Button button = (Button)findViewById(R.id.sample_button0); 
		
		button.setOnClickListener(new OnClickListener() {  
	        
	        public void onClick(View arg0) {  
	        	for(int i = 0; i<latint.size(); i++){
	        		
	    	        int latitudeE6=latint.get(i);
	    	    	int longitudeE6=lngint.get(i);
	    	  
	    			GeoPoint point = new GeoPoint(latitudeE6, longitudeE6);
	    			
	    			OverlayItem overlayitem =
	    				new OverlayItem(point, "Bingo", "在地图上看到自己很爽吧~!");
	    			itemizedOverlay.addOverlay(overlayitem);
	    			mapOverlays.add(itemizedOverlay);
	    			MapController mapController = mapView.getController();
	    			mapController.animateTo(point);
	    			mapController.setZoom(6);
	    		}
	        }
		});
		
		Button btnBack = (Button)findViewById(R.id.btnAlbumBack);		//获得返回按钮btnBack
		btnBack.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				SeeOnMapActivity.this.setResult(RESULT_OK, intent);
				/* 结束这个activity */
				SeeOnMapActivity.this.finish();
					//finish();
		}
		});

}

	
	public View makeView() {
		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		iv.setLayoutParams(new ImageSwitcher.LayoutParams(IMAGESWITCHER_HEIGHT,IMAGESWITCHER_HEIGHT));
		return iv;
	}
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}