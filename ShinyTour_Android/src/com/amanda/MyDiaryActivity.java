package com.amanda;

import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import com.amanda.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MyDiaryActivity extends Activity{
	MyConnector mc = null;//
	ArrayList<String []> diaryList = new ArrayList<String []>();
	ListView lvDiary = null;			//����ListView����
	int positionToDelete = -1;
	String uno = null;		//��¼�û�ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);		//���õ�ǰ��Ļ
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		   uno = extras.getString("uno");
		   username = extras.getString("username");
		   ustatus = extras.getString("ustatus");
		   uhid = extras.getString("uhid");
		  }
		lvDiary = (ListView)findViewById(R.id.lvDiary);
		getDiaryList();
	}
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lvDiary.setAdapter(ba);
				break;
			}
			super.handleMessage(msg);
		}
	};
	BaseAdapter ba = new BaseAdapter() {
	
		public View getView(int position, View convertView, ViewGroup parent) {
		//	System.out.println(diaryList.get(position)[3]);
			
			LinearLayout ll = new LinearLayout(MyDiaryActivity.this);
			TextView tvTitle = new TextView(MyDiaryActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);		//�����ӿؼ��Ķ��뷽ʽ
			LinearLayout llDiary = new LinearLayout(MyDiaryActivity.this);
			llDiary.setOrientation(LinearLayout.VERTICAL);
			tvTitle.setTextAppearance(MyDiaryActivity.this, R.style.title);
			tvTitle.setGravity(Gravity.LEFT);			//����TextView�Ķ��뷽ʽ
			tvTitle.setText(diaryList.get(position)[1]);
			
			//������ʾ������
			TextView tvContent = new TextView(MyDiaryActivity.this);
			tvContent.setTextAppearance(MyDiaryActivity.this, R.style.content);
			tvContent.setGravity(Gravity.LEFT);		//����TextView�Ķ��뷽ʽ
			String content = diaryList.get(position)[2];
			int i = (content.length()>15?15:content.length());
			tvContent.setText(content.substring(0,i)+"...");		
			
			//�����μ���ʱ��
			TextView tvTime = new TextView(MyDiaryActivity.this);
			tvTime.setTextAppearance(MyDiaryActivity.this, R.style.time);
			tvTime.setGravity(Gravity.LEFT);		//����TextView�Ķ��뷽ʽ
			tvTime.setText(diaryList.get(position)[3]);
			
			llDiary.addView(tvTitle);
			llDiary.addView(tvContent);
			llDiary.addView(tvTime);
			ll.addView(llDiary);			//��ӵ������Բ�����
			LinearLayout llButton = new LinearLayout(MyDiaryActivity.this);
			llButton.setOrientation(LinearLayout.HORIZONTAL);		//���ò��ַ�ʽ
			llButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			llButton.setGravity(Gravity.RIGHT);
	//�鿴��־
			Button btnReadDiary = new Button(MyDiaryActivity.this);			//�����鿴��ť
			btnReadDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnReadDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnReadDiary.setText(R.string.btnRead);
			btnReadDiary.setBackgroundResource(R.drawable.button_back4);
			btnReadDiary.setId(position);	//����Button��ID
			btnReadDiary.setOnClickListener(listenerToRead);		//���ð�ť�ļ�����
	//ɾ����־		
			Button btnDeleteDiary = new Button(MyDiaryActivity.this);			//����ɾ����ť
			btnDeleteDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnDeleteDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnDeleteDiary.setText(R.string.btnDelete);
			btnDeleteDiary.setBackgroundResource(R.drawable.button_back4);
			btnDeleteDiary.setId(position);	//����Button��ID
			btnDeleteDiary.setOnClickListener(listenerToDelete);		//���ð�ť�ļ�����
			llButton.addView(btnReadDiary);
			llButton.addView(btnDeleteDiary);
			ll.addView(llButton);
			return ll;	
		}
	
		public long getItemId(int position) {
			return 0;
		}
	
		public Object getItem(int position) {
			return null;
		}
	
		public int getCount() {
			return diaryList.size();
		}
	};
	View.OnClickListener listenerToRead = new View.OnClickListener() {
	
		public void onClick(View v) {
			Intent intent = new Intent(MyDiaryActivity.this,ReadDiaryActivity.class);
			int pos = v.getId();
			String [] data = diaryList.get(pos);
			intent.putExtra("diary_info", data);
			intent.putExtra("uno", uno);
			intent.putExtra("position", pos);
			intent.putExtra("username", username);
			intent.putExtra("ustatus", ustatus);
			intent.putExtra("uhid", uhid);
// Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "pos:"+pos);
			startActivity(intent);
		}
	};
	View.OnClickListener listenerToDelete = new View.OnClickListener() {
		
		public void onClick(View v) {
			positionToDelete = v.getId();		//���ID
			new AlertDialog.Builder(MyDiaryActivity.this)
				.setTitle("��ʾ")
				.setIcon(R.drawable.alert_icon)
				.setMessage("ȷ��ɾ����ƪ��־��")
				.setPositiveButton(
						"ȷ��",
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog, int which) {
								deleteDiary();
							}
						})
				.setNegativeButton(
						"ȡ��", 
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog,
									int which) {}
						}
						).show();
				
		}
	};
	

	public void deleteDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String rid = diaryList.get(positionToDelete)[0];
					String msg = "<#DELETE_DIARY#>"+rid;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//��ȡ������Ϣ
					if(reply.equals("<#DELETE_DIARY_SUCCESS#>")){			//ɾ���ɹ�
						Toast.makeText(MyDiaryActivity.this, "ɾ���μ��ɹ���", Toast.LENGTH_LONG).show();
						getDiaryList();
						Looper.loop();
					}
					else{			//ɾ��ʧ��
						Toast.makeText(MyDiaryActivity.this, "ɾ��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
		
		
	}
	//��������ȡ��־�б�
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "uno:"+uno);
					int size = mc.din.readInt();		//��ȡ��־�ĳ���
					diaryList = null;
					diaryList = new ArrayList<String []>();		//��ʼ��diaryLsit
					for(int i=0;i<size;i++){					//ѭ��������־��Ϣ
						String diaryInfo = mc.din.readUTF();		//��ȡ��־��Ϣ
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//����־��Ϣ��ӵ��б���
					}
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
	
}