package com.amanda;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;


public class FunctionTabActivity extends ActivityGroup{
	static final int MENU_SEARCH = 0;
	static final int MENU_EXIT = 1;
	String uno = null;
	
	String username = null;
	String ustatus = null;
	String uhid = null;

	private LinearLayout bodyView,headview;
	private LinearLayout Lay1, Lay2, Lay3, lay4,lay5;
	private int flag = 0; // 通过标记跳转不同的页面，显示不同的菜单项

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_main);
	
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		   uno = extras.getString("uno");
		   username = extras.getString("username");
		   ustatus = extras.getString("ustatus");
		   uhid = extras.getString("uhid");
		   flag = extras.getInt("flag");
		}
//		Log.d(ContactsActivity.ACTIVITY_SERVICE, "1:"+username);
		initMainView();
		showView(flag);
		
		Lay1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				flag = 0;
				showView(flag);
				Lay1.setBackgroundResource(R.drawable.frame_button_background);
				Lay2.setBackgroundResource(R.drawable.frame_button_nopressbg);
				Lay3.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay4.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay5.setBackgroundResource(R.drawable.frame_button_nopressbg);
			}
		});
		Lay2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				flag = 1;
				showView(flag);
				Lay2.setBackgroundResource(R.drawable.frame_button_background);
				Lay1.setBackgroundResource(R.drawable.frame_button_nopressbg);
				Lay3.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay4.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay5.setBackgroundResource(R.drawable.frame_button_nopressbg);
			}
		});
		Lay3.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				flag = 2;
				showView(flag);
				Lay3.setBackgroundResource(R.drawable.frame_button_background);
				Lay1.setBackgroundResource(R.drawable.frame_button_nopressbg);
				Lay2.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay4.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay5.setBackgroundResource(R.drawable.frame_button_nopressbg);
			}
		});
		lay4.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
				flag = 4;
				showView(flag);
				Lay3.setBackgroundResource(R.drawable.frame_button_nopressbg);
				Lay1.setBackgroundResource(R.drawable.frame_button_nopressbg);
				Lay2.setBackgroundResource(R.drawable.frame_button_nopressbg);
				lay4.setBackgroundResource(R.drawable.frame_button_background);
				lay5.setBackgroundResource(R.drawable.frame_button_nopressbg);
			}
		});
		lay5.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
			flag = 3;
			showView(flag);
			Lay3.setBackgroundResource(R.drawable.frame_button_nopressbg);
			Lay1.setBackgroundResource(R.drawable.frame_button_nopressbg);
			Lay2.setBackgroundResource(R.drawable.frame_button_nopressbg);
			lay4.setBackgroundResource(R.drawable.frame_button_nopressbg);
			lay5.setBackgroundResource(R.drawable.frame_button_background);
		}
	});
	}
	
//初始化主界面底部的功能菜单：player，localList，onlineList,menu;
	public void initMainView() {
			headview=(LinearLayout) findViewById(R.id.head);
			bodyView=(LinearLayout) findViewById(R.id.body);
			Lay1=(LinearLayout) findViewById(R.id.player);
			Lay2=(LinearLayout) findViewById(R.id.localList);
			Lay3=(LinearLayout) findViewById(R.id.onlineList);
			lay4=(LinearLayout) findViewById(R.id.menu);
			lay5=(LinearLayout) findViewById(R.id.publish);
		}
	   // 在主界面中显示其他界面
	public void showView(int flag) {
		switch (flag) {
		case 0:
			bodyView.removeAllViews();
			Intent intent0 = new Intent(FunctionTabActivity.this, ContactsActivity.class);
			intent0.putExtra("type", 0);
			intent0.putExtra("uno", uno);			
			View v0 = getLocalActivityManager().startActivity("好友",intent0).getDecorView();
			
			DisplayMetrics dm0 = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm0);
			int dmheight0 = dm0.heightPixels;
			LayoutParams param0 = new LayoutParams(LayoutParams.FILL_PARENT,
					dmheight0 -85);
			v0.setLayoutParams(param0);
			bodyView.addView(v0);
			break;
		case 1:
			bodyView.removeAllViews();
			
			Intent intent1 = new Intent(FunctionTabActivity.this, MyAlbumListActivity.class);
			intent1.putExtra("uno", uno);
			intent1.putExtra("username", username);
			intent1.putExtra("ustatus", ustatus);
			intent1.putExtra("uhid", uhid);
			View v1 = getLocalActivityManager().startActivity("相册",intent1).getDecorView();
			
			DisplayMetrics dm1 = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm1);
			int dmheight1 = dm1.heightPixels;
			LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT,
					dmheight1 -85);
			v1.setLayoutParams(param1);
			bodyView.addView(v1);
			break;
		case 2:			
			bodyView.removeAllViews();
			Intent intent2 = new Intent(FunctionTabActivity.this, MyDiaryActivity.class);
			intent2.putExtra("uno", uno);
			intent2.putExtra("username", username);
			intent2.putExtra("ustatus", ustatus);
			intent2.putExtra("uhid", uhid);
			
			
			
			View v2 = getLocalActivityManager().startActivity("游记",intent2).getDecorView();
			
			DisplayMetrics dm2 = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm2);
			int dmheight2 = dm2.heightPixels;
			LayoutParams param2 = new LayoutParams(LayoutParams.FILL_PARENT,
					dmheight2 -85);
			v2.setLayoutParams(param2);
			bodyView.addView(v2);
			break;
		case 3:
			bodyView.removeAllViews();
			Intent intent3 = new Intent(FunctionTabActivity.this, PublishActivity.class);
			intent3.putExtra("uno", uno);
			
			intent3.putExtra("username", username);
			intent3.putExtra("ustatus", ustatus);
			intent3.putExtra("uhid", uhid);
			
			
			
			View v4 = getLocalActivityManager().startActivity("发布",intent3).getDecorView();
			
			
			DisplayMetrics dm4 = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm4);
			int dmheight4 = dm4.heightPixels;
			LayoutParams param4 = new LayoutParams(LayoutParams.FILL_PARENT,
					dmheight4 -85);
			v4.setLayoutParams(param4);
			bodyView.addView(v4);
			break;
		case 4:
			bodyView.removeAllViews();
			Intent intent4 = new Intent(FunctionTabActivity.this, AboutMyselfActivity.class);
			intent4.putExtra("uno", uno);
			intent4.putExtra("username", username);
			intent4.putExtra("ustatus", ustatus);
			intent4.putExtra("uhid", uhid);
			
			View v3 = getLocalActivityManager().startActivity("我",intent4).getDecorView();
			DisplayMetrics dm3 = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm3);
			int dmheight3 = dm3.heightPixels;
			LayoutParams param3 = new LayoutParams(LayoutParams.FILL_PARENT,
					dmheight3 -85);
			v3.setLayoutParams(param3);
			bodyView.addView(v3);
			break;
		default:
			break;
		}
	}
	
//以下的不动
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH, 0, "搜索")
			.setIcon(R.drawable.search);
		menu.add(0, MENU_EXIT, 0, "退出")
			.setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case MENU_SEARCH:				//按下搜索菜单选项
			Intent intent = new Intent(this,SearchActivity.class);		//创建Intent
			intent.putExtra("visitor", uno);
			startActivity(intent);
			break;
		case MENU_EXIT:					//按下退出菜单选项
			new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("确认退出吗？")
			.setIcon(R.drawable.alert_icon)
			.setPositiveButton(
					"确定",
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());		//结束进程
						}
					})
			.setNegativeButton(
					"取消",
					new DialogInterface.OnClickListener() {
					
						public void onClick(DialogInterface dialog, int which) {}
					})
			.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}