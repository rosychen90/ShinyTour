package com.amanda;
import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.amanda.R;

import android.location.Geocoder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends Activity{
	MyConnector mc = null;
	List<String []> albumList = new ArrayList<String []>();
	String uno = null;			//����û����
	byte [] data = null;		//�洢ͼƬ���ֽ�����
	String newAlbum = null;		//�������������
	String xid = null;			//Ҫ�ϴ��������ID
	String name = null;			//�ϴ�����Ƭ����
	String des = null;			//�ϴ�����Ƭ����
	ProgressDialog pd = null;	//�������ȶԻ������
	Spinner sp = null;
	public Activity self = UploadActivity.this;
	
	double lat  ;
	double lng ;
	
	BaseAdapter baSpinner = new BaseAdapter() {
		
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(UploadActivity.this);
			tv.setTextSize(18.5f);
			tv.setTextColor(R.color.character);
			String [] sa = albumList.get(position);
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
			return albumList.size();
		}
	};
	Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				sp.setAdapter(baSpinner);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);		//������ʾ��Ļ
		Intent intent = getIntent();			//���Intent
		uno = intent.getStringExtra("uno");			//��ȡ�û����
		data = intent.getByteArrayExtra("data");	//��ȡ�ֽ�����
		lat = intent.getDoubleExtra("lat", 0.0)  ;
		lng = intent.getDoubleExtra("lng", 0.0) ;
		getAlbumList();							//�������б�
		sp = (Spinner)findViewById(R.id.AlbumSpinner);				//���Spinner����
		sp.setAdapter(baSpinner);											//����Spiiner��Adapter
		
		
//		updateWithNewLocation(lat,lng);
		TextView myLocationText = (TextView)findViewById(R.id.etPhotoLocation);
		myLocationText.setText(lat+"\n"+lng);
		
		
		Button btnNewAlbum = (Button)findViewById(R.id.btnNewAlbum);		//����������ᰴť
		btnNewAlbum.setOnClickListener(new View.OnClickListener() {			//Ϊ��ť��Ӽ�����
			
			public void onClick(View v) {						//��дonClick����
				final View dialog_view = LayoutInflater.from(UploadActivity.this).inflate(R.layout.new_album, null);	//��öԻ�����ͼ
				new AlertDialog.Builder(UploadActivity.this)
				.setView(dialog_view)
				.setPositiveButton(
						R.string.btnOk, 
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog, int which) {
								EditText et = (EditText)dialog_view.findViewById(R.id.etAlbumName);
								newAlbum = et.getEditableText().toString().trim();
								if(newAlbum.equals("")){		//���
									Toast.makeText(UploadActivity.this, "����������������", Toast.LENGTH_LONG).show();
									return;
								}
								createNewAlbum();
								
							}
						}).show();
			}
		});
		Button btnUpload = (Button)findViewById(R.id.btnConfirmUpload);			//�ϴ���ť
		btnUpload.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {						//�����ϴ���ť�����
				int position = sp.getSelectedItemPosition();
				if( position < 0){
					Toast.makeText(UploadActivity.this, "����û��ѡ���ϴ���ᣡ", Toast.LENGTH_LONG).show();
					return;
				}				
				
				xid = albumList.get(position)[0];		//�������id
				EditText etName = (EditText)findViewById(R.id.etPhotoName);			//�����Ƭ���Ƶ�EditText�ؼ�
				EditText etDes = (EditText)findViewById(R.id.etPhotoDes);			//�����Ƭ������EditText�ؼ�
				name = etName.getEditableText().toString().trim();		//�����Ƭ����
				des = etDes.getEditableText().toString().trim();			//�����Ƭ����
				if(name.equals("") || des.equals("")){			//û�������κ���Ϣ
					Toast.makeText(UploadActivity.this, "��������Ƭ ����", Toast.LENGTH_LONG).show();
					return;
				}
				pd = ProgressDialog.show(UploadActivity.this, "���Ժ�", "�����ϴ�ͼƬ...",true,true);
				uploadMyPhoto();			//���÷����ϴ�ͼƬ
			}
		});
		Button btnUploadBack = (Button)findViewById(R.id.btnUploadBack);
		btnUploadBack.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				UploadActivity.this.finish();				//������Activity
			}
		});
	}
	//�������������б�
	public void getAlbumList(){
		new Thread(){
			public void run(){
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					mc.dout.writeUTF("<#GET_ALBUM_LIST#>"+uno);					//������ȡ����б�����
					String reply = mc.din.readUTF();					//��ȡ����б�
					if(reply.equals("<#NO_ALBUM#>")){			//��������
						Toast.makeText(UploadActivity.this, "����û�д����κ���ᣡ", Toast.LENGTH_LONG).show();
						albumList = new ArrayList<String []>();
						albumList.add(new String[]{null,"�����"});
						return;
					}
					String albumArray [] = reply.split("\\$");				//�и��ַ���
					albumList = null;
					albumList = new ArrayList<String []>();
					for(String s:albumArray){
						String [] sa = s.split("\\|");					//�и��ַ���
						albumList.add(sa);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				myHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	//�����������������������һ�������
	public void createNewAlbum(){
		new Thread(){
			public void run(){
				Looper.prepare();			//����һ����Ϣѭ��
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					mc.dout.writeUTF("<#NEW_ALBUM#>"+newAlbum+"|"+uno);		//�������������������������
					//Ϊ�´�����������÷���
					mc.dout.writeInt(data.length);
					for(int i=0;i<data.length;i++){
						mc.dout.writeByte(data[i]);			//һ��һ���ֽڵķ�
					}
					String reply = mc.din.readUTF();			//���շ���������Ϣ
					if(reply.equals("<#NEW_ALBUM_SUCCESS#>")){			//��������ɹ�
						getAlbumList();		//���»������б�
						Toast.makeText(UploadActivity.this, "��ᴴ���ɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();			//ִ�б��߳��е���Ϣ����
					}
					else{
						Toast.makeText(UploadActivity.this, "��ᴴ��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();			//���߳����ر���Ϣ����
			}
		}.start();
	}
	//��������������ϴ���Ƭ
	public void uploadMyPhoto(){
		new Thread(){
			public void run(){
				Looper.prepare();			//Ϊ���̳߳�ʼ��һ����Ϣ����
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					String msg = "<#NEW_PHOTO#>"+name+"|"+des+"|"+xid;
					mc.dout.writeUTF(msg);			//������Ϣ
					mc.dout.writeInt(data.length);
					for(int i=0;i<data.length;i++){
						mc.dout.writeByte(data[i]);			//һ��һ���ֽڵķ�
					}
					mc.dout.writeDouble(lat);
					mc.dout.writeDouble(lng);
					mc.dout.flush();						//????????????????????????????????????????????????/
					String reply = mc.din.readUTF();	//���ջظ�
					if(reply.equals("<#NEW_PHOTO_SUCCESS#>")){			//�ϴ��ɹ�
						pd.dismiss();
						Toast.makeText(UploadActivity.this, "��Ƭ�ϴ��ɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else{
						pd.dismiss();
						Toast.makeText(UploadActivity.this, "��Ƭ�ϴ�ʧ�ܣ�", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
		}.start();
	}

	private void updateWithNewLocation(double lat, double lng) {
		// TODO Auto-generated method stub
		String latLongString;
		TextView myLocationText = (TextView)findViewById(R.id.etPhotoLocation);
		
		String addressString = "û���ҵ���ַ\n";
		
		if(lat!=0.0){			
			latLongString = "γ�ȣ�"+lat+"\n���ȣ�"+lng;
			
		//	double latitude  = location.getLatitude();
		//	double longitude = location.getLongitude();
			
			Geocoder gc = new Geocoder(self,Locale.getDefault());
						
			try {
				List<Address> addresses = gc.getFromLocation(lat,lng,1);
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
		
		myLocationText.setText(latLongString+"\n"+addressString);
	}

	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
	
}