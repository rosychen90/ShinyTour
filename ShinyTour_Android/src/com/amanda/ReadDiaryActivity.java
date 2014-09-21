package com.amanda;

import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import com.amanda.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ReadDiaryActivity extends Activity{
	MyConnector mc = null;
	String [] diaryInfo = null;	//��¼��־��Ϣ
	TextView etModifyTitle = null;
	TextView etModifyContent = null;
	ListView lvDiary = null;
	String uno = null;
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	
	int diaryPosition = 0;
	ArrayList<String []> diaryList = new ArrayList<String []>();
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		diaryInfo = intent.getStringArrayExtra("diary_info");
		uno = intent.getStringExtra("uno");
		username = intent.getStringExtra("username");
		ustatus = intent.getStringExtra("ustatus");
		uhid = intent.getStringExtra("uhid");
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			diaryPosition = extras.getInt("position");
		  }
		getDiaryList();
Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "pos1:"+diaryPosition);
		
		setContentView(R.layout.read_diary);
		
		lvDiary = (ListView)findViewById(R.id.lvDiary);
		
		etModifyTitle = (TextView)findViewById(R.id.etModifyTitle);		//��ñ���EditText
		etModifyTitle.setText(diaryInfo[1]);
		etModifyContent = (TextView)findViewById(R.id.etModifyDiary);		//�������EditText
		etModifyContent.setText(diaryInfo[2]);
		
		Button btnModifyDiary = (Button)findViewById(R.id.btnModifyDiary);	//����޸İ�ť

		btnModifyDiary.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(ReadDiaryActivity.this,ModifyDiaryActivity.class);
				String [] data = diaryList.get(diaryPosition);
				Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "pos:"+diaryPosition);
				intent.putExtra("diary_info", data);
				intent.putExtra("uno", uno);
				intent.putExtra("username", username);
				intent.putExtra("ustatus", ustatus);
				intent.putExtra("uhid", uhid);
				
				startActivity(intent);
			}
		}
		
		);
		
		
		Button btnModifyBack = (Button)findViewById(R.id.btnModifyDiaryBack);
		
		btnModifyBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ReadDiaryActivity.this,FunctionTabActivity.class);
				intent.putExtra("uno", uno);
				intent.putExtra("flag", 2);
				intent.putExtra("username", username);
				intent.putExtra("ustatus", ustatus);
				intent.putExtra("uhid", uhid);
				startActivity(intent);
			}
		});
	}
	
	
	protected void onDestroy() {
		if(mc == null){
			mc.sayBye();
		}
		super.onDestroy();
	}
	
	//��������ȡ��־�б�
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					
					int size = mc.din.readInt();		//��ȡ��־�ĳ���
					diaryList = null;
					diaryList = new ArrayList<String []>();		//��ʼ��diaryLsit
					for(int i=0;i<size;i++){					//ѭ��������־��Ϣ
						String diaryInfo = mc.din.readUTF();		//��ȡ��־��Ϣ
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//����־��Ϣ��ӵ��б���
				
					}
			
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}