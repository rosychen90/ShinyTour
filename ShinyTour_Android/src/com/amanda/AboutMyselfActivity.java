package com.amanda;

import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class AboutMyselfActivity extends Activity{
	MyConnector mc = null;	//
	ProgressDialog pd = null;	//ProgressDialog对象引用
	View dialog_view = null;		//联系我
	TextView UserName = null;
	TextView UserStatus = null;
	TextView UserID = null;
	ImageView UserHead = null;
	Bitmap head ;	//存放头像
	
	String uno = null;		//记录用户ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutmyself);		//设置当前屏幕
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		   uno = extras.getString("uno");
		   username = extras.getString("username");
		   ustatus = extras.getString("ustatus");
		   uhid = extras.getString("uhid");
		  }

		UserName = (TextView)findViewById(R.id.UserName);		//获得用户名字
		UserName.setText(username);
		UserStatus = (TextView)findViewById(R.id.UserStatus);
		UserStatus.setText(ustatus);
		UserID = (TextView)findViewById(R.id.UserID);
		UserID.setText(uno);
////////////////////////////////////////////////////////////
		//获得头像
////////////////////////////////////////////////////////////		
		try{
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				String msg = "<#GET_USER_HEAD#>"+uhid;
				mc.dout.writeUTF(msg);
				int headSize = mc.din.readInt();		//读取头像大小
				byte[] buf = new byte[headSize];			//创建缓冲区
				InputStream is = new ByteArrayInputStream(buf); 
				mc.din.readFully(buf);
				head = BitmapFactory.decodeStream(new FlushedInputStream(is));
			/*
				head = 
	                 BitmapFactory.decodeStream(
	                   new BufferedInputStream(is));
				head = BitmapFactory.decodeByteArray(buf, 0, headSize);
			
				UserHead = (ImageView)findViewById(R.id.UserHead);
				UserHead.setImageBitmap(head);
			*/
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("ooo"+head);
		UserHead = (ImageView)findViewById(R.id.UserHead);
		UserHead.setImageBitmap(head);
////////////////////////////////////////////////////////////
		//查看最近访客&联系我
////////////////////////////////////////////////////////////		
		Button btnVister = (Button)findViewById(R.id.btnVister);	//获得注销按钮
		btnVister.setOnClickListener(Visterlistener);
		Button btnConnectUs = (Button)findViewById(R.id.btnConnectUs);	//获得退出按钮
		btnConnectUs.setOnClickListener(Connectlistener);
////////////////////////////////////////////////////////////
		//退出和注销
////////////////////////////////////////////////////////////
		Button btnZhuXiao = (Button)findViewById(R.id.btnMereset);	//获得注销按钮
		btnZhuXiao.setOnClickListener(Resetlistener);
		Button btnExit = (Button)findViewById(R.id.btnMeexit);	//获得退出按钮
		btnExit.setOnClickListener(Exitlistener);
		
	}
	//获得访客监听器
	public OnClickListener Visterlistener= new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent0 = new Intent(AboutMyselfActivity.this, ContactsActivity.class);
			intent0.putExtra("type", 1);
			intent0.putExtra("uno", uno);	 
			startActivity(intent0);
		}};
	
	//联系我们监听器
		public OnClickListener Connectlistener= new OnClickListener(){

			public void onClick(View v) {
				
				LayoutInflater li = LayoutInflater.from(AboutMyselfActivity.this);
				dialog_view = li.inflate(R.layout.connectus, null);
				new AlertDialog.Builder(AboutMyselfActivity.this)
				.setTitle("联系Elivina")
				.setIcon(R.drawable.p_status)
				.setView(dialog_view)
				.setPositiveButton(
					"发送",
					new DialogInterface.OnClickListener() {				
						public void onClick(DialogInterface dialog, int which) {				
			
						    EditText etAdvice = (EditText)dialog_view.findViewById(R.id.etInputAdvice);
						    String emailAdvice = etAdvice.getEditableText().toString().trim();	//获得用户反馈内容   

						    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
							intent.setType("plain/text");
							intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"elivinaw@gmail.com"});
							intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ShinyTour--来自"+username+"的邮件");//设置标题内容
							intent.putExtra(android.content.Intent.EXTRA_TEXT, emailAdvice);
							startActivity(Intent.createChooser(intent,getResources().getString(R.string.str_message)));
						}

						
					})
				.setNegativeButton(
					"取消",
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {}
					})
				.show();
				
			}};
	
	//注销监听器
	public OnClickListener Resetlistener= new OnClickListener(){

             public void onClick(View v) {
 
            new AlertDialog.Builder(AboutMyselfActivity.this)
            .setIcon(R.drawable.alert_icon)
            .setTitle("提示")
            .setMessage("确认注销？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener(){

            	public void onClick(DialogInterface dialog, int which) {
            		Intent intent1 = new Intent(AboutMyselfActivity.this, LoginActivity.class); 
            		startActivity(intent1);
            		finish();
                }
                
            })
             .setNegativeButton("取消", new DialogInterface.OnClickListener(){

            	public void onClick(DialogInterface dialog, int which) {
                   
                }
                
            })
            .show();
            
        }   
    };
    //退出监听器
    public OnClickListener Exitlistener= new OnClickListener(){

        public void onClick(View v) {
     
       new AlertDialog.Builder(AboutMyselfActivity.this)
       .setIcon(R.drawable.alert_icon)
       .setTitle("提示")
       .setMessage("确认退出？")
       .setPositiveButton("确认", new DialogInterface.OnClickListener(){

       	public void onClick(DialogInterface dialog, int which) {
       		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
       		am.restartPackage(ACTIVITY_SERVICE);
       		finish();
        }           
       })
        .setNegativeButton("取消", new DialogInterface.OnClickListener(){

       	public void onClick(DialogInterface dialog, int which) {               
        }           
       })
       .show();       
   }   
};

static class FlushedInputStream extends FilterInputStream {
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public long skip(long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                int b = read();
                if (b < 0) {
                    break;  // we reached EOF
                } else {
                    bytesSkipped = 1; // we read one byte
                }
            }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
}

}


	

