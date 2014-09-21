package com.amanda;
import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import com.amanda.R;
import com.amanda.MyEditText;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PublishDiaryActivity extends Activity{
	MyConnector mc = null;
	ProgressDialog pd = null;
	String uno = null;
	
	EditText etTitle;
	EditText etDiary;
	//MyEditText etDiary;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.publish_diary);
		
		etTitle = (EditText)findViewById(R.id.etTitle);		//����ռǱ���EditText����
		etDiary = (EditText)findViewById(R.id.etDiary);				//����ռ�����EditText����
		//etDiary = (MyEditText) findViewById(R.id.etDiary);
		
		Button btnInsertPic;				
		btnInsertPic = (Button) findViewById(R.id.btnInsertPic);
	/*	
		btnInsertPic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etDiary.insertDrawable(R.drawable.easy);
			}
		});
	*/
		Button btnDiary = (Button)findViewById(R.id.btnDiary);			//��÷�����־��ť
		btnDiary.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				pd = ProgressDialog.show(PublishDiaryActivity.this, "���Ժ�", "���ڷ�����־...",true,true);
				publishDiary();							//������־
			}
		});
		Button btnDiaryBack = (Button)findViewById(R.id.btnDiaryBack);
		btnDiaryBack.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				finish();
			}
		});
	}
	//���������ӷ�������������־
	public void publishDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				
				String title = etTitle.getEditableText().toString().trim();		//����ռǱ���
				String diary = etDiary.getEditableText().toString().trim();		//����ռ�����
				 Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, title);
				 Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, diary);
	//			 Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, uno);
				if(title.equals("") || diary.equals("")){			//������������Ϊ��
					Toast.makeText(PublishDiaryActivity.this, "�뽫�μǵı����������д����", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//����һ��Socket����
					}
					Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, diary);
					String message = "<#NEW_DIARY#>" + title+"|"+diary+"|"+uno;
					mc.dout.writeUTF(message);				//������Ϣ
				 Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, message);
					String reply = mc.din.readUTF();		//������Ϣ

					pd.dismiss();
					if(reply.equals("<#DIARY_SUCCESS#>")){			//�����־�����ɹ�
						pd.dismiss();						//�رս��ȶԻ���
						Toast.makeText(PublishDiaryActivity.this, "�μ������ɹ��������μ�����в鿴", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else if(reply.equals("<#DIARY_FAIL#>")){		//�����־����ʧ��
						pd.dismiss();					//�رս��ȶԻ���
						Toast.makeText(PublishDiaryActivity.this, "�μ�����ʧ�ܣ����Ժ����ԣ�", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();		//����MyConnector��sayBye����
		}
		super.onDestroy();
	}
	
	
}