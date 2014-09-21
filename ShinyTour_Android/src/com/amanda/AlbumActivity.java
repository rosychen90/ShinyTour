package com.amanda;
import static com.amanda.ConstantUtil.IMAGESWITCHER_HEIGHT;
import static com.amanda.ConstantUtil.PHOTO_HEIGHT;
import static com.amanda.ConstantUtil.PHOTO_WIDTH;
import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;
import java.util.List;

import com.amanda.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.amanda.CustomItemizedOverlay;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlbumActivity extends MapActivity implements ViewSwitcher.ViewFactory{
	List<String []> photoInfoList = new ArrayList<String []>();
	Bitmap [] photoList;			//���ͼƬ������
	Gallery gl = null;				//Gallery���������
	ImageSwitcher is = null;		//ImageSwitcher���������
	Spinner sp = null;				//Spinner���������
	MyConnector mc = null;			//MyConnector���������
	String xid = "";				//�������ID
	String uno = "";				//����û�ID
	String visitor = "";			//��ŷ����ߵ�ID
	String pid = "";				//��ŵ�ǰ��ʾ����ƬID
	int from = -1;					//������Activity����Դ��0ΪMyAlbumListActivity��1ΪAlbumListActivity 
	List<String []> albumInfoList = new ArrayList<String []>();		//��������Ϣ��id���������

	String username = null;
	String ustatus = null;
	String uhid = null;
	
	List<Double>lat = new ArrayList<Double>();
	List<Double>lng = new ArrayList<Double>();
	ArrayList<Integer> latint = new ArrayList<Integer>();
	ArrayList<Integer> lngint = new ArrayList<Integer>();
	/////////////
	//Spinner��Adapter
	BaseAdapter baSpinner = new BaseAdapter() {
	
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(AlbumActivity.this);
			tv.setTextSize(18.5f);
			tv.setTextColor(Color.DKGRAY);
			String [] sa = albumInfoList.get(position);
			tv.setText(sa[1]);
			return tv;
		}

		public long getItemId(int position) {
			return 0;
		}
	
		public Object getItem(int position) {
			return null;
		}
	
		public int getCount() {
			return albumInfoList.size();
		}
	};
	//Gallery��Adapter
	BaseAdapter baGallery= new BaseAdapter() {
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = new ImageView(AlbumActivity.this);
			iv.setAdjustViewBounds(true);
			iv.setLayoutParams(new Gallery.LayoutParams(PHOTO_WIDTH, PHOTO_HEIGHT));
			iv.setMaxHeight(PHOTO_HEIGHT);						//����ImageView��ʾ�ĸ߶�
			iv.setMaxWidth(PHOTO_WIDTH);						//����ImageView��ʾ�Ŀ��	
			iv.setImageBitmap(photoList[position]);				//����ImageView��ʾ������
			return iv;
		}
	
		public long getItemId(int position) {
			return 0;
		}
	
		public Object getItem(int position) {
			return null;
		}
	
		public int getCount() {
			if(photoList != null){
				return photoList.length;
			}
			else{
				return 0;
			}
		}
	};
	//����Gallery�е�ͼƬ�󴥷����¼�������
	OnItemClickListener myListener = new OnItemClickListener() {	
	
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			if(is != null){			//���ImageSwitcher��Ϊ��
				Drawable d = new BitmapDrawable(photoList[position]);
				is.setImageDrawable(d);
			}
		}
	};
	Handler myHandler = new Handler(){
	
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				gl.setAdapter(baGallery);
				is.setImageDrawable(new BitmapDrawable(photoList[0]));
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album);					//���õ�ǰ��Ļ
		final Intent intent = getIntent();					//��ȡ������Activity��Intent
		uno = intent.getStringExtra("uno");				//���Extra�ֶε�uno
		username = intent.getStringExtra("username");
		ustatus = intent.getStringExtra("ustatus");
		uhid = intent.getStringExtra("uhid");
		
		visitor = intent.getStringExtra("visitor");
		from = intent.getIntExtra("from", -1);		//���Extra�ֶε�from
		int position = intent.getIntExtra("position", 0);	//��ñ�ѡ�е����
		String [] albumInfoArray = intent.getStringArrayExtra("albumlist");	//��������Ϣ����
		xid = intent.getStringExtra("xid");			//��ñ�ѡ�е������
		albumInfoList = new ArrayList<String []>();
		for(String s:albumInfoArray){			//������Ϣ����
			String [] sa = s.split("\\|");
			albumInfoList.add(sa);				//���������Ϣ�б�
		}

		sp = (Spinner)findViewById(R.id.spAlbum);		//���Spinner����
		sp.setAdapter(baSpinner);				//����Spinner�����Adapter
		sp.setSelection(position);				//ѡ����ǰһ��Activity�б�ѡ�е����
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {	//ΪSpinner��Ӽ�����
		
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				xid = albumInfoList.get(position)[0];			//�޸Ĵ洢���ID�ĳ�Ա����
							
			}
			
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	
		getPhotoList();		//�������е���Ƭ�б�
	
		gl = (Gallery)findViewById(R.id.galleryPhoto);		//���Gallery����
		gl.setOnItemClickListener(myListener);				//����Gallery��OnItemClickListener������
		is = (ImageSwitcher)findViewById(R.id.isPhoto);		//���ImageSwitcher����
		is.setFactory(this);								//����ImageSwitcher��Factory
		is.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));	//����ImageSwitcher��In����
		is.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));//����ImageSwitcher��Out����
		
	
		Button button1 = (Button)findViewById(R.id.sample_button1); 
		
		button1.setOnClickListener(new OnClickListener() {  
	        
	        public void onClick(View arg0) {  
	        	
	        	for(int i = 0; i<lat.size(); i++){
	        		double a = lat.get(i);
	        		double b = lng.get(i);
	    	        int latitudeE6=(int) (a*1000000);
	    	    	int longitudeE6=(int) (b*1000000);
	    	    	latint.add(latitudeE6);
	    	    	lngint.add(longitudeE6);
	        	}
	        	Intent intent1 = new Intent(AlbumActivity.this,SeeOnMapActivity.class);
			
				intent1.putIntegerArrayListExtra("latint", latint);
				intent1.putIntegerArrayListExtra("lngint", lngint);
				startActivity(intent1);
			//	finish();
              
	       }  
	    }); 
		
		Button btnBack = (Button)findViewById(R.id.btnAlbumBack);		//��÷��ذ�ťbtnBack
		btnBack.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				switch(from){		//�ж�������Activity����Դ
				case 0:			//��MyAlbumListActivity����
					Intent intent1 = new Intent(AlbumActivity.this,FunctionTabActivity.class);
					intent1.putExtra("uno", uno);
					intent1.putExtra("username", username);
					intent1.putExtra("ustatus", ustatus);
					intent1.putExtra("uhid", uhid);
					
					int flag = 1;
					intent1.putExtra("flag", flag);
					startActivity(intent1);	
				//	finish();
				//	AlbumActivity.this.setResult(RESULT_OK, intent);
					/* �������activity */
				//	AlbumActivity.this.finish();
				
					break;
				case 1:			//��AlbumListActivity����
					Intent intent2 = new Intent(AlbumActivity.this,HomePageActivity.class);
					intent2.putExtra("uno", uno);
					intent2.putExtra("visitor", visitor);
					intent2.putExtra("tab", "tab2");
					startActivity(intent2);		
					finish();
					break;
				}
			}
		});
		Button btnDeletePhoto = (Button)findViewById(R.id.btnDeletePhoto);
		if(visitor != null){		//���ǲ鿴�Լ������
			btnDeletePhoto.setVisibility(View.GONE);		//��������Լ�����ᣬ����ɾ����ť
		}
		btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				new AlertDialog.Builder(AlbumActivity.this)
					.setTitle("��ʾ")
					.setIcon(R.drawable.alert_icon)
					.setMessage("ȷ��ɾ������Ƭ��")
					.setPositiveButton(
						"ȷ��",
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog, int which) {
								int pos = gl.getSelectedItemPosition();
								pid = photoInfoList.get(pos)[0];
								deletePhoto();
							}
						})
					.setNegativeButton(
						"ȡ��", 
						null)
					.show();
			}
		});
	}
	
	public void getPhotoList(){
		
				try{
					mc = null;
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_ALBUM#>"+xid);
					int size = 0;		//�����᳤��
					size = mc.din.readInt();			//��ȡ���ĳ���
					if(size == 0){
						Toast.makeText(AlbumActivity.this, "����ỹδ�ϴ���Ƭ", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
					photoInfoList = new ArrayList<String []>(size);
					lat = new ArrayList<Double>(size);
					lng = new ArrayList<Double>(size);
					
					photoList = new Bitmap[size];
					for(int i=0;i<size;i++){			//ѭ����ȡͼƬ����
						String photoInfo = mc.din.readUTF();		//��ȡ��Ƭ��Ϣ
						String [] sa = photoInfo.split("\\|");			//�и��ַ���
						photoInfoList.add(sa);				//����Ƭ��Ϣ��ӵ��б���
						int photoLength = mc.din.readInt();		//��ȡ��Ƭ����
						byte [] buf = new byte[photoLength];		//������Ӧ���ȵ�����
		
						mc.din.readFully(buf);	//����ͼƬ����
						photoList[i] = BitmapFactory.decodeByteArray(buf, 0, photoLength);	//����Bitmap
						lat.add(mc.din.readDouble());
						lng.add(mc.din.readDouble());
					}
					myHandler.sendEmptyMessage(0);
					
				}catch(Exception e){
					e.printStackTrace();
				}

	}
	//������ɾ��ָ����Ƭ
	public void deletePhoto(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc ==null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String msg = "<#DELETE_PHOTO#>"+pid;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		
					if(reply.equals("<#DELETE_PHOTO_SUCCESS#>")){	
						Toast.makeText(AlbumActivity.this, "��Ƭɾ���ɹ���", Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(AlbumActivity.this, "ɾ��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
					}
					getPhotoList();
					Looper.loop();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
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
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
			mc = null;
		}
		super.onDestroy();
	}
}
	
