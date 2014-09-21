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
	ProgressDialog pd = null;	//ProgressDialog��������
	View dialog_view = null;		//��ϵ��
	TextView UserName = null;
	TextView UserStatus = null;
	TextView UserID = null;
	ImageView UserHead = null;
	Bitmap head ;	//���ͷ��
	
	String uno = null;		//��¼�û�ID
	String username = null;
	String ustatus = null;
	String uhid = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutmyself);		//���õ�ǰ��Ļ
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		   uno = extras.getString("uno");
		   username = extras.getString("username");
		   ustatus = extras.getString("ustatus");
		   uhid = extras.getString("uhid");
		  }

		UserName = (TextView)findViewById(R.id.UserName);		//����û�����
		UserName.setText(username);
		UserStatus = (TextView)findViewById(R.id.UserStatus);
		UserStatus.setText(ustatus);
		UserID = (TextView)findViewById(R.id.UserID);
		UserID.setText(uno);
////////////////////////////////////////////////////////////
		//���ͷ��
////////////////////////////////////////////////////////////		
		try{
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				String msg = "<#GET_USER_HEAD#>"+uhid;
				mc.dout.writeUTF(msg);
				int headSize = mc.din.readInt();		//��ȡͷ���С
				byte[] buf = new byte[headSize];			//����������
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
		//�鿴����ÿ�&��ϵ��
////////////////////////////////////////////////////////////		
		Button btnVister = (Button)findViewById(R.id.btnVister);	//���ע����ť
		btnVister.setOnClickListener(Visterlistener);
		Button btnConnectUs = (Button)findViewById(R.id.btnConnectUs);	//����˳���ť
		btnConnectUs.setOnClickListener(Connectlistener);
////////////////////////////////////////////////////////////
		//�˳���ע��
////////////////////////////////////////////////////////////
		Button btnZhuXiao = (Button)findViewById(R.id.btnMereset);	//���ע����ť
		btnZhuXiao.setOnClickListener(Resetlistener);
		Button btnExit = (Button)findViewById(R.id.btnMeexit);	//����˳���ť
		btnExit.setOnClickListener(Exitlistener);
		
	}
	//��÷ÿͼ�����
	public OnClickListener Visterlistener= new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent0 = new Intent(AboutMyselfActivity.this, ContactsActivity.class);
			intent0.putExtra("type", 1);
			intent0.putExtra("uno", uno);	 
			startActivity(intent0);
		}};
	
	//��ϵ���Ǽ�����
		public OnClickListener Connectlistener= new OnClickListener(){

			public void onClick(View v) {
				
				LayoutInflater li = LayoutInflater.from(AboutMyselfActivity.this);
				dialog_view = li.inflate(R.layout.connectus, null);
				new AlertDialog.Builder(AboutMyselfActivity.this)
				.setTitle("��ϵElivina")
				.setIcon(R.drawable.p_status)
				.setView(dialog_view)
				.setPositiveButton(
					"����",
					new DialogInterface.OnClickListener() {				
						public void onClick(DialogInterface dialog, int which) {				
			
						    EditText etAdvice = (EditText)dialog_view.findViewById(R.id.etInputAdvice);
						    String emailAdvice = etAdvice.getEditableText().toString().trim();	//����û���������   

						    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
							intent.setType("plain/text");
							intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"elivinaw@gmail.com"});
							intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ShinyTour--����"+username+"���ʼ�");//���ñ�������
							intent.putExtra(android.content.Intent.EXTRA_TEXT, emailAdvice);
							startActivity(Intent.createChooser(intent,getResources().getString(R.string.str_message)));
						}

						
					})
				.setNegativeButton(
					"ȡ��",
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {}
					})
				.show();
				
			}};
	
	//ע��������
	public OnClickListener Resetlistener= new OnClickListener(){

             public void onClick(View v) {
 
            new AlertDialog.Builder(AboutMyselfActivity.this)
            .setIcon(R.drawable.alert_icon)
            .setTitle("��ʾ")
            .setMessage("ȷ��ע����")
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

            	public void onClick(DialogInterface dialog, int which) {
            		Intent intent1 = new Intent(AboutMyselfActivity.this, LoginActivity.class); 
            		startActivity(intent1);
            		finish();
                }
                
            })
             .setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

            	public void onClick(DialogInterface dialog, int which) {
                   
                }
                
            })
            .show();
            
        }   
    };
    //�˳�������
    public OnClickListener Exitlistener= new OnClickListener(){

        public void onClick(View v) {
     
       new AlertDialog.Builder(AboutMyselfActivity.this)
       .setIcon(R.drawable.alert_icon)
       .setTitle("��ʾ")
       .setMessage("ȷ���˳���")
       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

       	public void onClick(DialogInterface dialog, int which) {
       		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
       		am.restartPackage(ACTIVITY_SERVICE);
       		finish();
        }           
       })
        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

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


	

