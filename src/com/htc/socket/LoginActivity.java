package com.htc.socket;

import com.htc.operation.Operation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	Button login;
	Button register;
	EditText etusername;
	EditText etpassword;
	String username;
	String password;
	ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		init();
		register.setOnClickListener(new RegisterOnclick());
		login.setOnClickListener(new LoginOnclick());
	}

	private void init() {
		etusername = (EditText) findViewById(R.id.etusername);
		etpassword = (EditText) findViewById(R.id.etpassword);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);

		p = new ProgressDialog(LoginActivity.this);
		p.setTitle("登录中");
		p.setMessage("正在努力登录中，请稍后");
	}

	private class RegisterOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, Register.class);
			startActivity(intent);
		}

	}

	private class LoginOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			username = etusername.getText().toString().trim();
			if (username == null || username.length() <= 0) {
				etusername.requestFocus();
				etusername.setError("用户名不能为空");
				return;
			} else {
				username = etusername.getText().toString().trim();
			}
			password = etpassword.getText().toString().trim();
			if (password == null || password.length() <= 0) {
				etpassword.requestFocus();
				etpassword.setError("密码不能为空");
				return;
			} else {
				password = etpassword.getText().toString().trim();
			}
			p.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					Operation operation = new Operation();
					String result = operation.login("Login", username, password);  /////Operation-- login()
					Message message = new Message();
					message.obj = result;
					
					handler.sendMessage(message);

				}
			}).start();
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String string = (String) msg.obj;
			p.dismiss();
			Toast.makeText(LoginActivity.this, string, 0).show();
			if (string.equals("登录成功")) {
				Intent intent  = new Intent();
				intent.setClass(LoginActivity.this, Welcome_Activity.class);
				startActivity(intent);
			}	
			
			super.handleMessage(msg);
		}
	};

}
