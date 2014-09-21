package com.amanda;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShootActivity extends Activity
{
	/** Called when the activity is first created. */
	private ImageView imageView;
	private OnClickListener imgViewListener;
	private Bitmap myBitmap;
	private byte[] photoData;
	
	public Activity self = ShootActivity.this;
	double lat = 0  ;
	double lng = 0 ;

	public void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoot);
		imageView = (ImageView) findViewById(R.id.imageView);
		imgViewListener = new OnClickListener()
		{
			public void onClick ( View v )
			{
				final CharSequence[] items =
				{ "相册", "拍照" };
				AlertDialog dlg = new AlertDialog.Builder(ShootActivity.this).setTitle("选择图片").setItems(items,
						new DialogInterface.OnClickListener()
						{
							public void onClick ( DialogInterface dialog , int item )
							{
								// 这里item是根据选择的方式，
								// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
								if (item == 1)
								{
									Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
									startActivityForResult(getImageByCamera, 1);
								} else
								{
									Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
									getImage.addCategory(Intent.CATEGORY_OPENABLE);
									getImage.setType("image/jpeg");
									startActivityForResult(getImage, 0);
								}
							}
						}).create();
				dlg.show();
			}
		};
		// 给imageView控件绑定点点击监听器
		imageView.setOnClickListener(imgViewListener);
		
		Button btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhoto);		//获得Button对象
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {									//按下确定上传按钮
				
///////////////////////////////////////////////////////////////////////////////////
				//地理信息采集
//////////////////////////////////////////////////////////////////////////////////	
			try{
				LocationManager locationManager;
				
				String context = Context.LOCATION_SERVICE;
				locationManager = (LocationManager)getSystemService(context);
				
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(false);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				
				String provider = locationManager.getBestProvider(criteria, true);
				
				Location location = locationManager.getLastKnownLocation(provider);
				
				updateWithNewLocation(location);
				
				locationManager.requestLocationUpdates(provider, 3000, 0, locationistener);
			}catch(Exception e){
				
			}
////////////////////////////////////////////////////////////////////////////////////////				
				if(photoData == null){
					Toast.makeText(ShootActivity.this, "请先拍摄一张照片！", Toast.LENGTH_LONG).show();
					System.out.println(lat);
					return;
				}
				
				Intent intent = ShootActivity.this.getIntent();
				intent.setClass(ShootActivity.this,UploadActivity.class);		//创建Intent
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("data", photoData);			//将图片数据设置为Extra
				startActivity(intent);						//启动上传图片Activity
			}
		});
		
		Button btnReShoot = (Button)findViewById(R.id.btnShootBack);			//返回按钮
		btnReShoot.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				ShootActivity.this.finish();
			}
		});

	}

	protected void onActivityResult ( int requestCode , int resultCode , Intent data )
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		ContentResolver resolver = getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法，
		 * 这个方法执行完后都会执行onActivityResult方法， 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		if (requestCode == 0)
		{
			try
			{
				// 获得图片的uri
				Uri originalUri = data.getData();
				// 将图片内容解析成字节数组
				photoData = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
				// 将字节数组转换为ImageView可调用的Bitmap对象
				myBitmap = getPicFromBytes(photoData, null);
				// //把得到的图片绑定在控件上显示
				imageView.setImageBitmap(myBitmap);
			} catch ( Exception e )
			{
				System.out.println(e.getMessage());
			}

		} else if (requestCode == 1)
		{
			try
			{
				super.onActivityResult(requestCode, resultCode, data);
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				photoData = baos.toByteArray();
			} catch ( Exception e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 把得到的图片绑定在控件上显示
			imageView.setImageBitmap(myBitmap);
		}
	}

	public static Bitmap getPicFromBytes ( byte[] bytes , BitmapFactory.Options opts )
	{
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream ( InputStream inStream ) throws Exception
	{
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	private void updateWithNewLocation(Location location) {
		// TODO Auto-generated method stub
		String latLongString;
	//	TextView myLocationText = (TextView)findViewById(R.id.myLocation);
		
		String addressString = "没有找到地址\n";
		
		if(location!=null){
			double lat  = location.getLatitude();
			double lng = location.getLongitude();
			
			latLongString = "纬度："+lat+"\n经度："+lng;
			
			double latitude  = location.getLatitude();
			double longitude = location.getLongitude();
			this.lat = latitude;
			this.lng = longitude;
			
			Geocoder gc = new Geocoder(self,Locale.getDefault());
			
			try {
				List<Address> addresses = gc.getFromLocation(latitude,longitude,1);
				StringBuilder sb = new StringBuilder();
				
				if(addresses.size()>0){
					Address address = addresses.get(0);
					for(int i=0;i<address.getMaxAddressLineIndex();i++){
						sb.append(address.getAddressLine(i)).append("\n");
					}
					sb.append(address.getLocality()).append("\n");
					sb.append(address.getLocality()).append("\n");
					sb.append(address.getCountryName());
					addressString = sb.toString();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			latLongString = "没有找到坐标。\n";
		}
		
		//myLocationText.setText("您当前的坐标如下：\n"+latLongString+"\n"+addressString);
	}
	
	private LocationListener locationistener = new LocationListener(){
		
		//当坐标改变时触发此函数
		public void onLocationChanged(Location location){
			updateWithNewLocation(location);
		}
		//Provider禁用是触发此函数          比如GPS被关闭
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//Provider启用是触发此函数，比如Gps被打开
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		//Provider的状态在可用、暂时不可用、和无服务三个状态直接切换时触发此函数
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	};
	
}