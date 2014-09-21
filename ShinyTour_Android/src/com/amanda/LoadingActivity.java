package com.amanda;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class LoadingActivity extends ActivityGroup{
	Timer timer;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		LinearLayout layout=(LinearLayout)findViewById(R.id.layout);  
		layout.setBackgroundResource(R.drawable.background);
		
		Start();

	}
	protected void onDestroy() {
		super.onDestroy();
	}
	 public void Start() {
         new Thread() {
                 public void run() {
                         try {
                                 Thread.sleep(1000);
                         } catch (InterruptedException e) {
                                 e.printStackTrace();
                         }
                         Intent intent = new Intent();
                         intent.setClass(LoadingActivity.this, LoginActivity.class);
                         startActivity(intent);
                         finish();
                 }
         }.start();
 }
}




          
   


