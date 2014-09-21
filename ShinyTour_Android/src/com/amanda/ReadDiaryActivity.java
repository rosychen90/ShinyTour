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
	String [] diaryInfo = null;	//记录日志信息
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
		
		etModifyTitle = (TextView)findViewById(R.id.etModifyTitle);		//获得标题EditText
		etModifyTitle.setText(diaryInfo[1]);
		etModifyContent = (TextView)findViewById(R.id.etModifyDiary);		//获得内容EditText
		etModifyContent.setText(diaryInfo[2]);
		
		Button btnModifyDiary = (Button)findViewById(R.id.btnModifyDiary);	//获得修改按钮

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
	
	//方法：获取日志列表
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					
					int size = mc.din.readInt();		//读取日志的长度
					diaryList = null;
					diaryList = new ArrayList<String []>();		//初始化diaryLsit
					for(int i=0;i<size;i++){					//循环接受日志信息
						String diaryInfo = mc.din.readUTF();		//读取日志信息
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//将日志信息添加到列表中
				
					}
			
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}