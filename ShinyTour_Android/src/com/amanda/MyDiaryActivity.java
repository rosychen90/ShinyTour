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
	ListView lvDiary = null;			//声明ListView对象
	int positionToDelete = -1;
	String uno = null;		//记录用户ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);		//设置当前屏幕
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
			ll.setGravity(Gravity.CENTER_VERTICAL);		//设置子控件的对齐方式
			LinearLayout llDiary = new LinearLayout(MyDiaryActivity.this);
			llDiary.setOrientation(LinearLayout.VERTICAL);
			tvTitle.setTextAppearance(MyDiaryActivity.this, R.style.title);
			tvTitle.setGravity(Gravity.LEFT);			//设置TextView的对齐方式
			tvTitle.setText(diaryList.get(position)[1]);
			
			//设置显示的内容
			TextView tvContent = new TextView(MyDiaryActivity.this);
			tvContent.setTextAppearance(MyDiaryActivity.this, R.style.content);
			tvContent.setGravity(Gravity.LEFT);		//设置TextView的对齐方式
			String content = diaryList.get(position)[2];
			int i = (content.length()>15?15:content.length());
			tvContent.setText(content.substring(0,i)+"...");		
			
			//设置游迹的时间
			TextView tvTime = new TextView(MyDiaryActivity.this);
			tvTime.setTextAppearance(MyDiaryActivity.this, R.style.time);
			tvTime.setGravity(Gravity.LEFT);		//设置TextView的对齐方式
			tvTime.setText(diaryList.get(position)[3]);
			
			llDiary.addView(tvTitle);
			llDiary.addView(tvContent);
			llDiary.addView(tvTime);
			ll.addView(llDiary);			//添加到总线性布局中
			LinearLayout llButton = new LinearLayout(MyDiaryActivity.this);
			llButton.setOrientation(LinearLayout.HORIZONTAL);		//设置布局方式
			llButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			llButton.setGravity(Gravity.RIGHT);
	//查看日志
			Button btnReadDiary = new Button(MyDiaryActivity.this);			//创建查看按钮
			btnReadDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnReadDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnReadDiary.setText(R.string.btnRead);
			btnReadDiary.setBackgroundResource(R.drawable.button_back4);
			btnReadDiary.setId(position);	//设置Button的ID
			btnReadDiary.setOnClickListener(listenerToRead);		//设置按钮的监听器
	//删除日志		
			Button btnDeleteDiary = new Button(MyDiaryActivity.this);			//创建删除按钮
			btnDeleteDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnDeleteDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnDeleteDiary.setText(R.string.btnDelete);
			btnDeleteDiary.setBackgroundResource(R.drawable.button_back4);
			btnDeleteDiary.setId(position);	//设置Button的ID
			btnDeleteDiary.setOnClickListener(listenerToDelete);		//设置按钮的监听器
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
			positionToDelete = v.getId();		//获得ID
			new AlertDialog.Builder(MyDiaryActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.alert_icon)
				.setMessage("确认删除该篇日志？")
				.setPositiveButton(
						"确定",
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog, int which) {
								deleteDiary();
							}
						})
				.setNegativeButton(
						"取消", 
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
					String reply = mc.din.readUTF();		//读取返回信息
					if(reply.equals("<#DELETE_DIARY_SUCCESS#>")){			//删除成功
						Toast.makeText(MyDiaryActivity.this, "删除游迹成功！", Toast.LENGTH_LONG).show();
						getDiaryList();
						Looper.loop();
					}
					else{			//删除失败
						Toast.makeText(MyDiaryActivity.this, "删除失败，请重试！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
		
		
	}
	//方法：获取日志列表
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "uno:"+uno);
					int size = mc.din.readInt();		//读取日志的长度
					diaryList = null;
					diaryList = new ArrayList<String []>();		//初始化diaryLsit
					for(int i=0;i<size;i++){					//循环接受日志信息
						String diaryInfo = mc.din.readUTF();		//读取日志信息
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//将日志信息添加到列表中
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