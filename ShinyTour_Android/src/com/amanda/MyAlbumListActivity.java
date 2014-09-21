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
	MyConnector mc = null;		//声明MyConnector对象
	ListView lvAlbumList = null;	//ListView对象的引用
	List<String []> albumInfoList = null;	//存放相册信息的List
	String albumInfoArray [] = null;		//存放相册信息的Array
	String uno = null;				//存放用户的ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	Bitmap [] cover ;	//存放相册缩略图
	
	
	int newAccess = -1;		//记录新设置的权限
	String [] accessOptions={
		"公开","好友可见","仅个人可见"	
	};
	int albumIndexToChange = -1;	//记录要更改权限的相册在信息列表中的索引
	String albumToChange = null;	//记录要更改权限的相册ID
	String accessToChange = null;	//记录要更改的权限
	BaseAdapter ba = new BaseAdapter() {
	
		public View getView(int position, View arg1, ViewGroup arg2) {
			
			LinearLayout ll = new LinearLayout(MyAlbumListActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);			//设置线性布局的分布方式
			
			LinearLayout llAlbum = new LinearLayout(MyAlbumListActivity.this);
			llAlbum.setOrientation(LinearLayout.VERTICAL);
			llAlbum.setMinimumWidth(250);
			
			//设置TextView显示的内容
			TextView tvName = new TextView(MyAlbumListActivity.this);
			tvName.setTextAppearance(MyAlbumListActivity.this, R.style.title);
			//tvName.setPadding(5, 0, 0, 0);
			tvName.setText(albumInfoList.get(position)[1]);		//TC: tvName.setText(albumInfoList.get(position)[1]+"    ");		
			tvName.setLayoutParams(new LinearLayout.LayoutParams(200, LayoutParams.WRAP_CONTENT));//TC 275
			tvName.setGravity(10);
			
			//设置相册建立的时间
			TextView tvTime = new TextView(MyAlbumListActivity.this);
			tvTime.setTextAppearance(MyAlbumListActivity.this, R.style.time);
			tvTime.setGravity(Gravity.LEFT);		//设置TextView的对齐方式
			tvTime.setText(albumInfoList.get(position)[3]);
			
			LinearLayout ll2 = new LinearLayout(MyAlbumListActivity.this);
			ll2.setOrientation(LinearLayout.HORIZONTAL);		//设置线性布局的分布方式
			ll2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll2.setGravity(Gravity.LEFT);
		
			Button btnSee = new Button(MyAlbumListActivity.this);	//创建查看相册照片的Button对象
			btnSee.setId(position);									//设置Button的ID
			btnSee.setText("查看");
			btnSee.setBackgroundResource(R.drawable.button_back4);
			btnSee.setTextAppearance(MyAlbumListActivity.this, R.style.button);
			btnSee.setOnClickListener(listenerToDetail);			//设置Button的监听器
			btnSee.setLayoutParams(new LinearLayout.LayoutParams(60, 40));
						//LayoutParams(60, LayoutParams.WRAP_CONTENT));
			ll2.addView(btnSee);
			
			Button btnChangeAccess = new Button(MyAlbumListActivity.this);	//创建修改相册权限的Button对象
			btnChangeAccess.setId(position);						//设置Button的ID
			btnChangeAccess.setText("修改访问权限");
			btnChangeAccess.setBackgroundResource(R.drawable.button_back4);
			btnChangeAccess.setTextAppearance(MyAlbumListActivity.this, R.style.button);	//设置按钮样式
			btnChangeAccess.setOnClickListener(listenerToAcess);		//添加监听器
			btnChangeAccess.setLayoutParams(new LinearLayout.LayoutParams(100, 40));//TC 140设置布局参数
			ll2.addView(btnChangeAccess);

			llAlbum.addView(tvName);
			llAlbum.addView(tvTime);
			llAlbum.addView(ll2);
			
			//设置相册缩略图
			ImageView tvImage = new ImageView(MyAlbumListActivity.this);
			tvImage.setMaxHeight(64);
			tvImage.setMaxWidth(64);
			tvImage.setBackgroundResource(R.drawable.imageview_background);
			tvImage.setAdjustViewBounds(true);
			tvImage.setImageBitmap(cover[position]);
		
			
			LinearLayout ll3 = new LinearLayout(MyAlbumListActivity.this);
			ll3.setOrientation(LinearLayout.HORIZONTAL);		//设置线性布局的分布方式
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
	View.OnClickListener listenerToDetail = new View.OnClickListener() {	//点下查看按钮后触发的监听器
	
		public void onClick(View v) {					//重写onClick方法
			Intent intent = new Intent(MyAlbumListActivity.this,AlbumActivity.class);	//创建Intent对象
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
		setContentView(R.layout.album_list);					//设置当前屏幕
		Intent intent = getIntent();			//获得启动该Activity的Intent
		uno = intent.getStringExtra("uno");		//获得Intent中的uno的值
		username = intent.getStringExtra("username");
		ustatus = intent.getStringExtra("ustatus");
		uhid = intent.getStringExtra("uhid");
		
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//获得ListView对象
		getAlbumList();	//获得指定用户的相册列表
	}

	

	public void getAlbumList(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){		//检查MyConnector对象是否为空
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);			//创建MyConnector对象
					}
					
					mc.dout.writeUTF("<#GET_MY_ALBUM_LIST#>"+uno);					//发出获取相册列表请求
					

					int count = mc.din.readInt();
					System.out.println("count"+count);
					cover = new Bitmap[count];
					for(int i = 0;i<count;i++){
						int coverSize = mc.din.readInt();		//读取头像大小
						System.out.println("coverSize:"+coverSize);
						byte[] buf = new byte[coverSize];			//创建缓冲区
						InputStream is = new ByteArrayInputStream(buf); 
						mc.din.readFully(buf);
						Bitmap c = BitmapFactory.decodeStream(new FlushedInputStream(is));	
						System.out.println("c:"+c);
						cover[i] = c;
						System.out.println("cover:"+i+""+cover[i]);
						//= BitmapFactory.decodeStream(new FlushedInputStream(is));							
					}				
					String reply = mc.din.readUTF();					//读取相册列表
					System.out.println("reply"+reply);
					
					if(reply.equals("<#NO_ALBUM#>")){							//判断相册列表是否为空
						Toast.makeText(MyAlbumListActivity.this, "您还没有上传过照片", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
				
					albumInfoArray = reply.split("\\$");				//切割字符串
					albumInfoList = new ArrayList<String []>();
					for(String s:albumInfoArray){					
						String [] sa = s.split("\\|");					//切割字符串
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

	//方法：显示修改权限对话框
	public void showMyDialog(){
		new AlertDialog.Builder(this)
		.setSingleChoiceItems(accessOptions, newAccess, new OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {
				newAccess = which;
			}
		})
		.setPositiveButton("确定", new OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {
				accessToChange=newAccess+"";
				changeAlbumAccess();
			}
		}).show();
	}
	//方法：修改相册权限
	public int changeAlbumAccess(){
		int result = 0;
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//创建MyConnector
					}
					String msg = "<#CHANGE_ALBUM_ACCESS#>"+albumToChange+"|"+accessToChange;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//接收服务器反馈
					if(reply.equals("<#ALBUM_ACCESS_SUCCESS#>")){				//更新权限成功
						Toast.makeText(MyAlbumListActivity.this, "相册权限更新成功！", Toast.LENGTH_LONG).show();
						Looper.loop();			//执行消息队列中的消息
					}
					else{								//更新权限失败
						Toast.makeText(MyAlbumListActivity.this, "相册权限更新失败！", Toast.LENGTH_LONG).show();
						Looper.loop();			//执行消息队列中的消息
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