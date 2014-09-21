package com.amanda;
import static com.amanda.ConstantUtil.SERVER_ADDRESS;
import static com.amanda.ConstantUtil.SERVER_PORT;

import com.amanda.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	MyConnector mc = null;	//���ȶԻ������ڵ����¼��ť�����ʾ
	ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.login);//����Ļ����Ϊlayout�ļ��е�login.xml�еĲ���
        checkIfRemember();//����Ƿ�����һ��¼���Ѿ���ȡ��ǰ�û�����Ϣ
        
        //����˵�¼��ť�Ķ���
        final Button btnLogin = (Button)findViewById(R.id.btnLogin);
     
        btnLogin.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				pd = ProgressDialog.show(LoginActivity.this, "Waiting", "�������ӷ�����...", true, true);
				//login�����������е�¼��֤
				login();
			}
		});
        Button btnReg = (Button)findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, com.amanda.RegActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }
    //���������ӷ��������е�¼
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					EditText etUid = (EditText)findViewById(R.id.etUid);	//����ʺ�EditText
					EditText etPwd = (EditText)findViewById(R.id.etPwd);	//�������EditText
					String uid = etUid.getEditableText().toString().trim();	//���������ʺ�
					String pwd = etPwd.getEditableText().toString().trim();	//������������
					if(uid.equals("") || pwd.equals("")){		//�ж������Ƿ�Ϊ��
						Toast.makeText(LoginActivity.this, "�������ʺŻ�����!", Toast.LENGTH_SHORT).show();//�����ʾ��Ϣ
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//��֯Ҫ���ص��ַ���
					mc.dout.writeUTF(msg);										//������Ϣ��������
					String receivedMsg = mc.din.readUTF();		//��ȡ�����������ķ�����ϸ��Ϣ
					pd.dismiss();		//��dialog�ر�
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//�յ�����ϢΪ��¼�ɹ���Ϣ
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
					//	Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "123uno:"+sa[0]);
						CheckBox cb = (CheckBox)findViewById(R.id.cbRemember);		//���CheckBox����
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
					//	Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "123u:"+sa[3]);
						//����Ļת���������
						Intent intent = new Intent(LoginActivity.this,FunctionTabActivity.class);
						//u_no,u_name,u_email,u_state,h_id
						intent.putExtra("uno", sa[0]);
						intent.putExtra("username", sa[1]);
						intent.putExtra("ustatus",sa[3]);
						intent.putExtra("uhid", sa[4]);

				//		Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "username:"+sa[1]);
						
						startActivity(intent);						//��������Activity
						finish();
					}
					else if(msg.startsWith("<#LOGIN_FAIL#>")){					//�յ�����ϢΪ��¼ʧ��
						Toast.makeText(LoginActivity.this, msg.substring(14), Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    //���������û���id���������Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
    	SharedPreferences.Editor editor = sp.edit();			//���Editor
    	editor.putString("uid", uid);							//���û�������Preferences
    	editor.putString("pwd", pwd);							//���������Preferences
    	editor.commit();
    }
    //��������Preferences�ж�ȡ�û���������
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
    	String uid = sp.getString("uid", null);
    	String pwd = sp.getString("pwd", null);
    	if(uid != null && pwd!= null){
    		EditText etUid = (EditText)findViewById(R.id.etUid);
    		EditText etPwd = (EditText)findViewById(R.id.etPwd);
    		CheckBox cbRemember = (CheckBox)findViewById(R.id.cbRemember);
    		etUid.setText(uid);
    		etPwd.setText(pwd);
    		cbRemember.setChecked(true);
    	}
    }
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
}