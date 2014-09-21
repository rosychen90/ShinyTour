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
				{ "���", "����" };
				AlertDialog dlg = new AlertDialog.Builder(ShootActivity.this).setTitle("ѡ��ͼƬ").setItems(items,
						new DialogInterface.OnClickListener()
						{
							public void onClick ( DialogInterface dialog , int item )
							{
								// ����item�Ǹ���ѡ��ķ�ʽ��
								// ��items�������涨�������ַ�ʽ�����յ��±�Ϊ1���Ծ͵������շ���
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
		// ��imageView�ؼ��󶨵���������
		imageView.setOnClickListener(imgViewListener);
		
		Button btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhoto);		//���Button����
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {									//����ȷ���ϴ���ť
				
///////////////////////////////////////////////////////////////////////////////////
				//������Ϣ�ɼ�
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
					Toast.makeText(ShootActivity.this, "��������һ����Ƭ��", Toast.LENGTH_LONG).show();
					System.out.println(lat);
					return;
				}
				
				Intent intent = ShootActivity.this.getIntent();
				intent.setClass(ShootActivity.this,UploadActivity.class);		//����Intent
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("data", photoData);			//��ͼƬ��������ΪExtra
				startActivity(intent);						//�����ϴ�ͼƬActivity
			}
		});
		
		Button btnReShoot = (Button)findViewById(R.id.btnShootBack);			//���ذ�ť
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
		 * ��Ϊ���ַ�ʽ���õ���startActivityForResult������
		 * �������ִ����󶼻�ִ��onActivityResult������ ����Ϊ�����𵽵�ѡ�����Ǹ���ʽ��ȡͼƬҪ�����жϣ�
		 * �����requestCode��startActivityForResult����ڶ���������Ӧ
		 */
		if (requestCode == 0)
		{
			try
			{
				// ���ͼƬ��uri
				Uri originalUri = data.getData();
				// ��ͼƬ���ݽ������ֽ�����
				photoData = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
				// ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����
				myBitmap = getPicFromBytes(photoData, null);
				// //�ѵõ���ͼƬ���ڿؼ�����ʾ
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
			// �ѵõ���ͼƬ���ڿؼ�����ʾ
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
		
		String addressString = "û���ҵ���ַ\n";
		
		if(location!=null){
			double lat  = location.getLatitude();
			double lng = location.getLongitude();
			
			latLongString = "γ�ȣ�"+lat+"\n���ȣ�"+lng;
			
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
			latLongString = "û���ҵ����ꡣ\n";
		}
		
		//myLocationText.setText("����ǰ���������£�\n"+latLongString+"\n"+addressString);
	}
	
	private LocationListener locationistener = new LocationListener(){
		
		//������ı�ʱ�����˺���
		public void onLocationChanged(Location location){
			updateWithNewLocation(location);
		}
		//Provider�����Ǵ����˺���          ����GPS���ر�
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//Provider�����Ǵ����˺���������Gps����
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		//Provider��״̬�ڿ��á���ʱ�����á����޷�������״ֱ̬���л�ʱ�����˺���
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	};
	
}