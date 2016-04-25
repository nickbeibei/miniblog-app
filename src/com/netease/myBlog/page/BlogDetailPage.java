package com.netease.myBlog.page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.minblog.R;
import com.example.minblog.R.id;
import com.example.minblog.R.layout;
import com.example.minblog.R.menu;
import com.netease.myBlog.myStruct.BlogUnit;
import com.netease.myBlog.myStruct.GlobalParameters;
import com.netease.myBlog.myStruct.commonFuntion;
import com.netease.myBlog.network.HttpEngine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogDetailPage extends Activity implements OnClickListener{
	public final String TAG = "jcdebug";
	private String blogId = "";
	private TextView titleTextView = null;
	private TextView contentTextView = null;
	private TextView timeTextView = null;
	private Bitmap showBitmap = null;
	private Bundle data = new Bundle();
	private ImageView showImageView = null;
	private String bitMapurl = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_detail_page);
		
		titleTextView = (TextView) findViewById(R.id.title_TextView);
		contentTextView = (TextView) findViewById(R.id.context_textView);
		timeTextView = (TextView) findViewById(R.id.updatetime_TextView);
		Button backBtn = (Button) findViewById(R.id.editPage_back_button);
		Button edit = (Button) findViewById(R.id.edit_blog_button);
		showImageView = (ImageView) findViewById(R.id.image_textView);
		
		edit.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		
		Intent intent = getIntent();
		blogId = intent.getStringExtra("blogId");
		new Thread(runBlogDatail).start();
	}
	
	
	Runnable runBlogDatail = new Runnable() {
		
		@Override
		public void run() {
			HttpEngine httpEngine = new HttpEngine();
			String url = GlobalParameters.MiniBlogInterface.GET_BLOG_DETAIL;
			url += "?blogid=" + blogId;
			data = httpEngine.getMethodBundle(url);
			Message msg = new Message();
			msg.what = 0;
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				handlerBlogDetail(msg);
				break;
			case 1:
				handlerShowImage(msg);
				break;
			default:
				break;
			}
		}

		private void handlerShowImage(Message msg) {
			showImageView.setImageBitmap(showBitmap);			
		}

		private void handlerBlogDetail(Message msg) {
			Bundle data = msg.getData();
			int code = data.getInt("code");
			if (code == 200) {
				String body = data.getString("body");
				try {
					JSONObject root = new JSONObject(body);
					String title = root.getString("title");
//					int title = root.getInt("title");
					String content = root.getString("content");
					String createTime = root.getString("createTime");
					createTime = commonFuntion.fromatTime(createTime);
					JSONArray pictures = root.getJSONArray("pictures");
					if (pictures.length()-1 >= 0) {
						bitMapurl = pictures.get(pictures.length()-1).toString();
						new Thread(runShowImageView).start();
					}

					
					
					titleTextView.setText(title);
					contentTextView.setText(content);
					Log.d(TAG, "content" + content);
					timeTextView.setText(createTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(BlogDetailPage.this, "获取blog详情失败", Toast.LENGTH_SHORT).show();
			}
			

		}
		
	};
	
	Runnable runShowImageView = new Runnable() {

		@Override
		public void run() {
			HttpEngine httpEngine = new HttpEngine();
			showBitmap = httpEngine.getMethodPicture(bitMapurl);
			Message msg = new Message();
			Bundle data = new Bundle();
			if (showBitmap == null) {
				data.putString("code", "0");
			} else {
				data.putString("code", "1");
			}
			msg.setData(data);
			msg.what = 1;
			handler.sendMessage(msg);
			
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_blog_button:
			toBlogEditPage();
			break;
		case R.id.editPage_back_button:
			toBlogListPage();
			break;
		default:
			break;
		}
		
	}


	private void toBlogListPage() {
		Intent intent = new Intent(BlogDetailPage.this, BlogListPage.class);
		intent.putExtra("fromPage", "BlogDetailPage");
		intent.putExtra("toPage", "BlogListPage");
		startActivity(intent);		
	}

	/**
	 * 跳转blog编辑页
	 */
	private void toBlogEditPage() {
		Intent intent = new Intent(BlogDetailPage.this, BlogEditPage.class);
		intent.putExtra("fromPage", "BlogDetailPage");
		intent.putExtra("toPage", "BlogEditPage");
//		intent.putExtra("blogId", blogId);
		String body = data.getString("body");
		
		try {
			JSONObject js = new JSONObject(body);
			String title = js.getString("title");
			String content = js.getString("content");
			String pic = js.getJSONArray("pictures").toString();
			
//			intent.putExtra("title", title);
//			intent.putExtra("content", content);
//			intent.putExtra("pictures", pic);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		startActivity(intent);
	}
	
	/**
	 * http
	 * 获取blog详情
	 * @param token
	 * @param blogId
	 * @return
	 */
	private BlogUnit postBlogDetail(String token, String blogId) {
		BlogUnit blogUnit = new BlogUnit();
//		blogUnit.setAbstruct("abstruct" + "blog详情页");
		blogUnit.setBlogId(blogId);
		blogUnit.setContent("content" + "blog详情页");
		blogUnit.setTitle("title" + "blog详情页");
		blogUnit.setUpdateTime("updateTime" + "blog详情页");
		blogUnit.setCreateTime("createTime" + "blog详情页");
		return blogUnit;
	}

}
