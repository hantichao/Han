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
		dialog.setTitle("�����ϴ���");
		dialog.setMessage("���Ժ�...");
	}

	private class EtusernameOnFocusChange implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!etusername.hasFocus()) {
				str = etusername.getText().toString().trim();
				if (str == null || str.length() <= 0) {
					// etusername.requestFocus();
					etusername.setError("�û�������Ϊ��");
				} else {
					new Thread(new Runnable() {
						// ����û�����Ϊ�գ���ô���û����ύ���������Ͻ�����֤�����û����Ƿ���ڣ�����JavaEE ajaxһ��
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
				etusername.setError("�û���" + str + "�Ѿ�����");
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
	 * ��д onActivityResult���� **************************** ���select ��
	 * ʹ�õ���startActivityForResult �����µ�activity �� startactivity
	 * ��������startActivity( ) ��������ת��Ŀ��ҳ�棬���������ص�ǰҳ�棬�������ʹ��һ��startActivity( )
	 * startActivityForResult( )
	 * ����һ��������������񣬵�����ִ�е���δ����ʱ�򣬼�����T1Activity��ת����һ��Text2Activity
	 * ���������Text2Activity������finish
	 * ()�����Ժ󣬳�����Զ���ת��T1Activity��������ǰһ��T1Activity�е�onActivityResult( )����
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			System.out.println("uri----->" + uri);

			filepath = uri.toString().substring(6);
			System.out.println("filepath--->>" + filepath);
			// �û���ͷ���ǲ���ͼƬ��ʽ
			if (filepath.endsWith("jpg") || filepath.endsWith("png")) {
				File file = new File(filepath);
				try {
					InputStream inputStream = new FileInputStream(file);
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					imgphoto.setImageBitmap(bitmap);// ����Ǿͽ�ͼƬ��ʾ����

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
		Dialog dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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
				sex = "��";
			} else {
				sex = "Ů";
			}

			age = etage.getText().toString().trim();
			if (age == null || age.length() <= 0) {
				etage.requestFocus();
				etage.setError("���䲻��Ϊ��ֵ");
				return;
			}

			System.out.println("��ʼ�ϴ�����");
			dialog.show();
			System.out.println("�Ի�����ʾ��");
			new Thread(new Runnable() {

				@Override
				public void run() {

					Operation operation = new Operation();
					File file = new File(filepath);
					String photo = operation.uploadFile(file, "ImgReciver");
					System.out.println("uploadFile2");
					// �Ƚ���ͼƬ�ϴ��Ĳ�����Ȼ�����������ͼƬ�����ڷ�������·����
					User user = new User(username, password, sex, age, photo);
					// ����һ��user����
					List<User> list = new ArrayList<User>();
					list.add(user);

					WriteJson writeJson = new WriteJson();
					// ��user����д��json��ʽ�ַ���
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
				Toast.makeText(Register.this, "ע��ɹ�", 0).show();
				Intent intent = new Intent();
				intent.setClass(Register.this, LoginActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(Register.this, "ע��ʧ��", 0).show();
			}

			super.handleMessage(msg);

		}
	};

}
