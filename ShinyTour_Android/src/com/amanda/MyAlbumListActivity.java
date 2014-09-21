package com.amanda;

import static com.amanda.ConstantUtil.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amanda.R;
import com.amanda.AboutMyselfActivity.FlushedInputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MyAlbumListActivity extends Activity{
	MyConnector mc = null;		//����MyConnector����
	ListView lvAlbumList = null;	//ListView���������
	List<String []> albumInfoList = null;	//��������Ϣ��List
	String albumInfoArray [] = null;		//��������Ϣ��Array
	String uno = null;				//����û���ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	Bitmap [] cover ;	//����������ͼ
	
	
	int newAccess = -1;		//��¼�����õ�Ȩ��
	String [] accessOptions={
		"����","���ѿɼ�","�����˿ɼ�"	
	};
	int albumIndexToChange = -1;	//��¼Ҫ����Ȩ�޵��������Ϣ�б��е�����
	String albumToChange = null;	//��¼Ҫ����Ȩ�޵����ID
	String accessToChange = null;	//��¼Ҫ���ĵ�Ȩ��
	BaseAdapter ba = new BaseAdapter() {
	
		public View getView(int position, View arg1, ViewGroup arg2) {
			
			LinearLayout ll = new LinearLayout(MyAlbumListActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);			//�������Բ��ֵķֲ���ʽ
			
			LinearLayout llAlbum = new LinearLayout(MyAlbumListActivity.this);
			llAlbum.setOrientation(LinearLayout.VERTICAL);
			llAlbum.setMinimumWidth(250);
			
			//����TextView��ʾ������
			TextView tvName = new TextView(MyAlbumListActivity.this);
			tvName.setTextAppearance(MyAlbumListActivity.this, R.style.title);
			//tvName.setPadding(5, 0, 0, 0);
			tvName.setText(albumInfoList.get(position)[1]);		//TC: tvName.setText(albumInfoList.get(position)[1]+"    ");		
			tvName.setLayoutParams(new LinearLayout.LayoutParams(200, LayoutParams.WRAP_CONTENT));//TC 275
			tvName.setGravity(10);
			
			//������Ὠ����ʱ��
			TextView tvTime = new TextView(MyAlbumListActivity.this);
			tvTime.setTextAppearance(MyAlbumListActivity.this, R.style.time);
			tvTime.setGravity(Gravity.LEFT);		//����TextView�Ķ��뷽ʽ
			tvTime.setText(albumInfoList.get(position)[3]);
			
			LinearLayout ll2 = new LinearLayout(MyAlbumListActivity.this);
			ll2.setOrientation(LinearLayout.HORIZONTAL);		//�������Բ��ֵķֲ���ʽ
			ll2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll2.setGravity(Gravity.LEFT);
		
			Button btnSee = new Button(MyAlbumListActivity.this);	//�����鿴�����Ƭ��Button����
			btnSee.setId(position);									//����Button��ID
			btnSee.setText("�鿴");
			btnSee.setBackgroundResource(R.drawable.button_back4);
			btnSee.setTextAppearance(MyAlbumListActivity.this, R.style.button);
			btnSee.setOnClickListener(listenerToDetail);			//����Button�ļ�����
			btnSee.setLayoutParams(new LinearLayout.LayoutParams(60, 40));
						//LayoutParams(60, LayoutParams.WRAP_CONTENT));
			ll2.addView(btnSee);
			
			Button btnChangeAccess = new Button(MyAlbumListActivity.this);	//�����޸����Ȩ�޵�Button����
			btnChangeAccess.setId(position);						//����Button��ID
			btnChangeAccess.setText("�޸ķ���Ȩ��");
			btnChangeAccess.setBackgroundResource(R.drawable.button_back4);
			btnChangeAccess.setTextAppearance(MyAlbumListActivity.this, R.style.button);	//���ð�ť��ʽ
			btnChangeAccess.setOnClickListener(listenerToAcess);		//��Ӽ�����
			btnChangeAccess.setLayoutParams(new LinearLayout.LayoutParams(100, 40));//TC 140���ò��ֲ���
			ll2.addView(btnChangeAccess);

			llAlbum.addView(tvName);
			llAlbum.addView(tvTime);
			llAlbum.addView(ll2);
			
			//�����������ͼ
			ImageView tvImage = new ImageView(MyAlbumListActivity.this);
			tvImage.setMaxHeight(64);
			tvImage.setMaxWidth(64);
			tvImage.setBackgroundResource(R.drawable.imageview_background);
			tvImage.setAdjustViewBounds(true);
			tvImage.setImageBitmap(cover[position]);
		
			
			LinearLayout ll3 = new LinearLayout(MyAlbumListActivity.this);
			ll3.setOrientation(LinearLayout.HORIZONTAL);		//�������Բ��ֵķֲ���ʽ
			ll3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll3.setGravity(Gravity.RIGHT);
			ll3.addView(tvImage);
			
			
			ll.addView(llAlbum);
			ll.addView(ll3);
			return ll;
		}
	
		public long getItemId(int arg0) {
			return 0;
		}
	
		public Object getItem(int arg0) {
			return null;
		}
	
		public int getCount() {
			return albumInfoList.size();
		}
	};
	View.OnClickListener listenerToDetail = new View.OnClickListener() {	//���²鿴��ť�󴥷��ļ�����
	
		public void onClick(View v) {					//��дonClick����
			Intent intent = new Intent(MyAlbumListActivity.this,AlbumActivity.class);	//����Intent����
			intent.putExtra("uno", uno);
			intent.putExtra("albumlist", albumInfoArray);
			intent.putExtra("xid", albumInfoList.get(v.getId())[0]);
			intent.putExtra("position", v.getId());
			intent.putExtra("from", 0);
			
			intent.putExtra("username", username);
			intent.putExtra("ustatus", ustatus);
			intent.putExtra("uhid", uhid);
			
			startActivity(intent);
			//finish();
		}
	};
	View.OnClickListener listenerToAcess = new View.OnClickListener() {
	
		public void onClick(View v) {
			albumIndexToChange = v.getId();
			albumToChange = albumInfoList.get(v.getId())[0];
			newAccess = Integer.valueOf(albumInfoList.get(v.getId())[2]);
			showMyDialog();
		}
	};
	Handler myHandler = new Handler(){
	
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lvAlbumList.setAdapter(ba);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_list);					//���õ�ǰ��Ļ
		Intent intent = getIntent();			//���������Activity��Intent
		uno = intent.getStringExtra("uno");		//���Intent�е�uno��ֵ
		username = intent.getStringExtra("username");
		ustatus = intent.getStringExtra("ustatus");
		uhid = intent.getStringExtra("uhid");
		
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//���ListView����
		getAlbumList();	//���ָ���û�������б�
	}

	

	public void getAlbumList(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){		//���MyConnector�����Ƿ�Ϊ��
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);			//����MyConnector����
					}
					
					mc.dout.writeUTF("<#GET_MY_ALBUM_LIST#>"+uno);					//������ȡ����б�����
					

					int count = mc.din.readInt();
					System.out.println("count"+count);
					cover = new Bitmap[count];
					for(int i = 0;i<count;i++){
						int coverSize = mc.din.readInt();		//��ȡͷ���С
						System.out.println("coverSize:"+coverSize);
						byte[] buf = new byte[coverSize];			//����������
						InputStream is = new ByteArrayInputStream(buf); 
						mc.din.readFully(buf);
						Bitmap c = BitmapFactory.decodeStream(new FlushedInputStream(is));	
						System.out.println("c:"+c);
						cover[i] = c;
						System.out.println("cover:"+i+""+cover[i]);
						//= BitmapFactory.decodeStream(new FlushedInputStream(is));							
					}				
					String reply = mc.din.readUTF();					//��ȡ����б�
					System.out.println("reply"+reply);
					
					if(reply.equals("<#NO_ALBUM#>")){							//�ж�����б��Ƿ�Ϊ��
						Toast.makeText(MyAlbumListActivity.this, "����û���ϴ�����Ƭ", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
				
					albumInfoArray = reply.split("\\$");				//�и��ַ���
					albumInfoList = new ArrayList<String []>();
					for(String s:albumInfoArray){					
						String [] sa = s.split("\\|");					//�и��ַ���
						albumInfoList.add(sa);
						
					}
					System.out.println("ListSize:"+albumInfoList.size());
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	//��������ʾ�޸�Ȩ�޶Ի���
	public void showMyDialog(){
		new AlertDialog.Builder(this)
		.setSingleChoiceItems(accessOptions, newAccess, new OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {
				newAccess = which;
			}
		})
		.setPositiveButton("ȷ��", new OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {
				accessToChange=newAccess+"";
				changeAlbumAccess();
			}
		}).show();
	}
	//�������޸����Ȩ��
	public int changeAlbumAccess(){
		int result = 0;
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//����MyConnector
					}
					String msg = "<#CHANGE_ALBUM_ACCESS#>"+albumToChange+"|"+accessToChange;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//���շ���������
					if(reply.equals("<#ALBUM_ACCESS_SUCCESS#>")){				//����Ȩ�޳ɹ�
						Toast.makeText(MyAlbumListActivity.this, "���Ȩ�޸��³ɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();			//ִ����Ϣ�����е���Ϣ
					}
					else{								//����Ȩ��ʧ��
						Toast.makeText(MyAlbumListActivity.this, "���Ȩ�޸���ʧ�ܣ�", Toast.LENGTH_LONG).show();
						Looper.loop();			//ִ����Ϣ�����е���Ϣ
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		return result;
	}
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
			mc = null;
		}
		super.onDestroy();
	}
	
}