package com.netease.myBlog.page;


import org.json.JSONException;
import org.json.JSONObject;

import com.example.minblog.R;
//import com.example.minblog.TestPage;
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

public class MainActivity extends Activity implements OnClickListener {
	public final String TAG = "jcdebug";
	EditText accounteEditText = null;
	EditText passwordEditText = null;
	Button signInBtn = null;
	Button registerBtn = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		accounteEditText = (EditText) findViewById(R.id.account_editText);
		passwordEditText = (EditText) findViewById(R.id.password_editText);
		signInBtn = (Button) findViewById(R.id.signin_button);
		registerBtn = (Button) findViewById(R.id.register_button);
		signInBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.signin_button:
			//登录http线程
			new Thread(signInRun).start();
			break;
		case R.id.register_button:
			toRegisterPage();
			break;
		default:
			break;
		}
	}


//	private void toTestPage() {
//		Intent intent = new Intent(MainActivity.this, TestPage.class);
//		startActivity(intent);
//	}

	/**
	 * 注册页面
	 */
	private void toRegisterPage() {
		Intent intent = new Intent(MainActivity.this, RegisterPage.class);
		intent.putExtra("fromPage", "MainActivity");
		intent.putExtra("toPage", "RegisterPage");
		startActivity(intent);
	}

	/**
	 * 登录线程
	 */
	Runnable signInRun = new Runnable() {

		@Override
		public void run() {
			String account = accounteEditText.getText().toString();
			String pd = passwordEditText.getText().toString();
			Bundle rs = new Bundle();
			Message msg = new Message();
			if (account.length() > 1 && pd.length() > 1) {
				rs = loginRun(account, pd);
				
			} else {
				rs.putInt("code", GlobalParameters.ACCOUNT_PASSWORD_NULL);
			}
			msg.what = 0;
			msg.setData(rs);
			handler.sendMessage(msg);
		}

		public Bundle loginRun(String account, String pd) {
			Bundle rs = new Bundle();
			JSONObject js = new JSONObject();
			try {
				js.put("email", account);
				js.put("password", pd);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = GlobalParameters.MiniBlogInterface.LOGIN;
			HttpEngine httpEngine = new HttpEngine();
			rs = httpEngine.postMethodBundle(url, js.toString());
			Log.e("-------------", ""+rs);
			return rs;
		}
	};
	
	/**
	 * UI线程回调函数
	 */
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				loginHandler(msg.getData());
				
			default:
				
				break;
			}
		}

		/**
		 * 登录成功UI线程回调，存储token
		 * @param bundle
		 */
		private void loginHandler(Bundle bundle) {
			int code = bundle.getInt("code");
			if (code == 200) {
				Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				String body = bundle.getString("body");
				try {
					JSONObject js = new JSONObject(body);
					String token = js.getString("mini_blog_token");
					GlobalParameters.TOKEN = token;
					Intent intent = new Intent(MainActivity.this, BlogListPage.class);	
					intent.putExtra("fromPage", "MainActivity");
					intent.putExtra("toPage", "BlogListPage");
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}	else if (code == GlobalParameters.ACCOUNT_PASSWORD_NULL) {
				Toast.makeText(MainActivity.this, "请输入用户和密码", Toast.LENGTH_SHORT).show();
			} 
			else {
				Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			}
		}
		
	};

}
