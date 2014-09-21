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
	MyConnector mc = null;	//进度对话框，用于点击登录按钮后的提示
	ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.login);//将屏幕设置为layout文件中的login.xml中的布局
        checkIfRemember();//检查是否在上一登录中已经存取当前用户的信息
        
        //获得了登录按钮的对象
        final Button btnLogin = (Button)findViewById(R.id.btnLogin);
     
        btnLogin.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				pd = ProgressDialog.show(LoginActivity.this, "Waiting", "正在连接服务器...", true, true);
				//login方法用来进行登录验证
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
    //方法：连接服务器进行登录
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					EditText etUid = (EditText)findViewById(R.id.etUid);	//获得帐号EditText
					EditText etPwd = (EditText)findViewById(R.id.etPwd);	//获得密码EditText
					String uid = etUid.getEditableText().toString().trim();	//获得输入的帐号
					String pwd = etPwd.getEditableText().toString().trim();	//获得输入的密码
					if(uid.equals("") || pwd.equals("")){		//判断输入是否为空
						Toast.makeText(LoginActivity.this, "请输入帐号或密码!", Toast.LENGTH_SHORT).show();//输出提示消息
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//组织要返回的字符串
					mc.dout.writeUTF(msg);										//发出消息到服务器
					String receivedMsg = mc.din.readUTF();		//读取服务器发来的反馈纤细消息
					pd.dismiss();		//将dialog关闭
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//收到的消息为登录成功消息
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
					//	Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "123uno:"+sa[0]);
						CheckBox cb = (CheckBox)findViewById(R.id.cbRemember);		//获得CheckBox对象
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
					//	Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "123u:"+sa[3]);
						//将屏幕转到功能面板
						Intent intent = new Intent(LoginActivity.this,FunctionTabActivity.class);
						//u_no,u_name,u_email,u_state,h_id
						intent.putExtra("uno", sa[0]);
						intent.putExtra("username", sa[1]);
						intent.putExtra("ustatus",sa[3]);
						intent.putExtra("uhid", sa[4]);

				//		Log.d(PublishDiaryActivity.ACTIVITY_SERVICE, "username:"+sa[1]);
						
						startActivity(intent);						//启动功能Activity
						finish();
					}
					else if(msg.startsWith("<#LOGIN_FAIL#>")){					//收到的消息为登录失败
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
    //方法：将用户的id和密码存入Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
    	SharedPreferences.Editor editor = sp.edit();			//获得Editor
    	editor.putString("uid", uid);							//将用户名存入Preferences
    	editor.putString("pwd", pwd);							//将密码存入Preferences
    	editor.commit();
    }
    //方法：从Preferences中读取用户名和密码
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
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