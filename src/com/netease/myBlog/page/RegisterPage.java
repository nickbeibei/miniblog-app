package com.netease.myBlog.page;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.minblog.R;
import com.netease.myBlog.myStruct.GlobalParameters;
import com.netease.myBlog.network.HttpEngine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/*
 * android的主线程，即UI现在设计为非安全线程
 * android4.0开始不再推荐在主线发送http协议，推荐另起一个线程
 * 另起一个线程，线程处理发起网络请求，请求后将数据封装，通过Message回调给UI线程，这一过程由handler更新UI线程
 */

public class RegisterPage extends Activity implements OnClickListener {
	public final String TAG = "jcdebug";
	public static final String USERINFO_KEY = "UserInfo.class";
	EditText accountEditText = null;
	EditText password1EditText = null;
	EditText password2EditText = null;
	EditText nicknameEditText = null;
	Button signin = null;
	Button register = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_page);
		signin = (Button) findViewById(R.id.signin_button);
		register = (Button) findViewById(R.id.register_button);
		accountEditText = (EditText) findViewById(R.id.account_editText);
		password1EditText = (EditText) findViewById(R.id.password_editText);
		password2EditText = (EditText) findViewById(R.id.password_editText2);
		nicknameEditText = (EditText) findViewById(R.id.nickname_editText);
		signin.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signin_button:
			signIn();
			break;
		case R.id.register_button:
			new Thread(registerRun).start();
			break;
		default:
			break;
		}
	}

	private void signIn() {
		Intent intent = new Intent(RegisterPage.this, MainActivity.class);
		intent.putExtra("fromPage", "MainActivity");
		intent.putExtra("toPage", "RegisterPage");
		startActivity(intent);
	}

	Runnable registerRun = new Runnable() {

		@Override
		public void run() {
			String account = accountEditText.getText().toString();
			String pd1 = password1EditText.getText().toString();
			String pd2 = password2EditText.getText().toString();
			String nickname = nicknameEditText.getText().toString();
			Map<String, String> rs = new HashMap<String, String>();
			if (pd1.equals(pd2) && pd1.length() > 1) {
				postRegister(account, pd1, nickname);
			} else {
				Log.d(TAG, "密码不一致或为空！");
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}

		}

		/**
		 * 注册请求
		 * @param account	账号
		 * @param pd1	密码
		 * @param nickname 昵称
		 */
		public void postRegister(String account, String pd1, String nickname) {
			Map<String, String> rs = new HashMap<String, String>();
			HttpEngine hEngine = new HttpEngine();
			
			JSONObject params = new JSONObject();
			try {
				params.put("email", account);
				params.put("nickname", nickname);
				params.put("password", pd1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			rs = hEngine.postMethod(
					GlobalParameters.MiniBlogInterface.CREATE_USERR,
					params.toString());
			
			Message msg = new Message();
			//定义消息为1
			msg.what = 1;
			Bundle data = new Bundle();
			data.putString("code", rs.get("code"));
			data.putString("body", rs.get("body"));
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};


	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(RegisterPage.this, "密码输入不一致", Toast.LENGTH_SHORT)
				.show();
				break;
			case 1:
				registerHandler(msg);
				break;
				
			default:
				break;
			}
		}

		private void registerHandler(Message msg) {
			Bundle data = msg.getData();
			String code = data.getString("code");
			String rs = data.getString("body");
			Toast.makeText(RegisterPage.this, code, Toast.LENGTH_SHORT)
			.show();
			Log.d(TAG, "body" + rs);
			Intent intent = new Intent(RegisterPage.this, MainActivity.class);
			intent.putExtra("fromPage", "MainActivity");
			intent.putExtra("toPage", "RegisterPage");
			startActivity(intent);
		}

	};

}
