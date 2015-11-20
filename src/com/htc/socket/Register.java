package com.htc.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.htc.bean.User;
import com.htc.json.JsonUtil;
import com.htc.json.WriteJson;
import com.htc.operation.Operation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Region.Op;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class Register extends Activity {

	Button submit;
	Button select;
	EditText etusername;
	EditText etpassword;
	RadioButton ckman;
	RadioButton ckwoman;
	EditText etage;
	ImageView imgphoto;
	String str;
	String filepath = null;
	String jsonString = null;
	ProgressDialog dialog;
	private static final int REQUEST_EX = 1;
	String username = null;
	String password = null;
	String sex = null;
	String age = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		init();

		etusername.setOnFocusChangeListener(new EtusernameOnFocusChange());
		select.setOnClickListener(new SelectOnclick());
		submit.setOnClickListener(new SubmitOnclick());
	}

	private void init() {
		submit = (Button) findViewById(R.id.submit);
		select = (Button) findViewById(R.id.select);
		etusername = (EditText) findViewById(R.id.etuname);
		etpassword = (EditText) findViewById(R.id.etpassword);
		ckman = (RadioButton) findViewById(R.id.ckman);
		ckwoman = (RadioButton) findViewById(R.id.ckwoman);
		etage = (EditText) findViewById(R.id.etage);
		imgphoto = (ImageView) findViewById(R.id.imgphoto);
		dialog = new ProgressDialog(Register.this);
		dialog.setTitle("数据上传中");
		dialog.setMessage("请稍后...");
	}

	private class EtusernameOnFocusChange implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!etusername.hasFocus()) {
				str = etusername.getText().toString().trim();
				if (str == null || str.length() <= 0) {
					// etusername.requestFocus();
					etusername.setError("用户名不能为空");
				} else {
					new Thread(new Runnable() {
						// 如果用户名不为空，那么将用户名提交到服务器上进行验证，看用户名是否存在，就像JavaEE ajax一样
						@Override
						public void run() {
							Operation operation = new Operation();
							String result = operation.checkusername("Check",
									str);
							System.out.println("result---check-->>" + result);
							Message message = new Message();
							message.obj = result;
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String msgobj = msg.obj.toString();
			System.out.println(msgobj);
			System.out.println(msgobj.length());

			if (msgobj.equals("t")) {
				etusername.requestFocus();
				etusername.setError("用户名" + str + "已经存在");
			} else {
				etpassword.requestFocus();
			}
			super.handleMessage(msg);
		}
	};

	private class SelectOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("explorer_title",
					getString(R.string.dialog_read_from_dir));
			intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
			intent.setClass(Register.this, ExDialog.class);
			startActivityForResult(intent, REQUEST_EX);
		}

	}

	/**
	 * 重写 onActivityResult函数 **************************** 点击select 后
	 * 使用的是startActivityForResult 启动新的activity 和 startactivity
	 * 的区别在startActivity( ) 仅仅是跳转到目标页面，若是想跳回当前页面，则必须再使用一次startActivity( )
	 * startActivityForResult( )
	 * 可以一次性完成这项任务，当程序执行到这段代码的时候，假若从T1Activity跳转到下一个Text2Activity
	 * ，而当这个Text2Activity调用了finish
	 * ()方法以后，程序会自动跳转回T1Activity，并调用前一个T1Activity中的onActivityResult( )方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			System.out.println("uri----->" + uri);

			filepath = uri.toString().substring(6);
			System.out.println("filepath--->>" + filepath);
			// 用户的头像是不是图片格式
			if (filepath.endsWith("jpg") || filepath.endsWith("png")) {
				File file = new File(filepath);
				try {
					InputStream inputStream = new FileInputStream(file);
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					imgphoto.setImageBitmap(bitmap);// 如果是就将图片显示出来

				} catch (Exception e) {
					e.printStackTrace();
				}
				submit.setClickable(true);
			} else {
				submit.setClickable(false);
				alert();
			}
		}
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						filepath = null;
					}
				}).create();
		dialog.show();

	}

	private class SubmitOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			username = etusername.getText().toString().trim();
			password = etpassword.getText().toString().trim();

			if (ckman.isChecked()) {
				sex = "男";
			} else {
				sex = "女";
			}

			age = etage.getText().toString().trim();
			if (age == null || age.length() <= 0) {
				etage.requestFocus();
				etage.setError("年龄不能为空值");
				return;
			}

			System.out.println("开始上传数据");
			dialog.show();
			System.out.println("对话框显示中");
			new Thread(new Runnable() {

				@Override
				public void run() {

					Operation operation = new Operation();
					File file = new File(filepath);
					String photo = operation.uploadFile(file, "ImgReciver");
					System.out.println("uploadFile2");
					// 先进行图片上传的操作，然后服务器返回图片保存在服务器的路径，
					User user = new User(username, password, sex, age, photo);
					// 构造一个user对象
					List<User> list = new ArrayList<User>();
					list.add(user);

					WriteJson writeJson = new WriteJson();
					// 将user对象写出json形式字符串
					jsonString = writeJson.getJsonData(list);

					System.out.println("jsonString--->" + jsonString);
					String result = operation.UpData("Register", jsonString);

					Message message = new Message();
					System.out.println("result--123-->" + result);
					message.obj = result;
					handler2.sendMessage(message);
				}
			}).start();
		}

	}

	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			String msobj = msg.obj.toString();
			if (msobj.endsWith("t")) {
				Toast.makeText(Register.this, "注册成功", 0).show();
				Intent intent = new Intent();
				intent.setClass(Register.this, LoginActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(Register.this, "注册失败", 0).show();
			}

			super.handleMessage(msg);

		}
	};

}
