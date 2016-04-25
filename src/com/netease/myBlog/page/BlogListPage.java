package com.netease.myBlog.page;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.minblog.R;
import com.netease.myBlog.myStruct.BlogListAdapter;
import com.netease.myBlog.myStruct.BlogUnit;
import com.netease.myBlog.myStruct.GlobalParameters;
import com.netease.myBlog.myStruct.commonFuntion;
import com.netease.myBlog.network.HttpEngine;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogListPage extends Activity implements OnClickListener {
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	public final String TAG = "jcdebug";
	private String token = "";
	public ListView listView = null;
	private int blogIndex;
	BlogListAdapter bListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_list_page);

		
		new Thread(runBlogList).start();
		
		// 初始化listView
		listView = (ListView) findViewById(R.id.blog_listView);
		bListAdapter = new BlogListAdapter(this, listItem, mMyListener);
		listView.setAdapter(bListAdapter);

		// button
		Button signOutBtn = (Button) findViewById(R.id.signout_button);
		Button refleshBtn = (Button) findViewById(R.id.reflesh_button);
		Button writeBlogBtn = (Button) findViewById(R.id.writeblog_button);
		TextView nickNameTv = (TextView) findViewById(R.id.nickname_textView);

		nickNameTv.setText("龙猫大侠");
		// 添加监听
		signOutBtn.setOnClickListener(this);
		refleshBtn.setOnClickListener(this);
		writeBlogBtn.setOnClickListener(this);

	}

	
	OnClickListener mMyListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.delete_button:
				deleteBtnFun(v);
				break;
			case R.id.linearLayout2:
				toBlogDetailPageFun(v);
				break;
			default:
				break;
			}			
		}
	};
	
	public void deleteBtnFun(View v) {
		blogIndex = (Integer) v.getTag();
		new Thread(runDelBlog).start();
	}

	public void toBlogDetailPageFun(View v) {
		blogIndex = (Integer) v.getTag();
		Intent intent = new Intent(BlogListPage.this, BlogDetailPage.class);
		intent.putExtra("toPage", "BlogEditPage");
		intent.putExtra("fromPage", "BlogListPage");
		String blogId = listItem.get(blogIndex).get("blogId").toString();
		intent.putExtra("blogId", listItem.get(blogIndex).get("blogId").toString());
		startActivity(intent);
	}
	
	
	

	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				blogListHandle(msg);
				break;
			case 1:
				blogListHandle(msg);
				break;
			case 2:
				handlerDelBlog(msg);
				break;
			default:
				break;
			}
		}
		

		private void handlerDelBlog(Message msg) {
			Bundle bundle = msg.getData();
			int code = bundle.getInt("code");
			if (code == 200) {
				Toast.makeText(BlogListPage.this, "删除成功", Toast.LENGTH_SHORT)
				.show();
				listItem.remove(blogIndex);
				bListAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(BlogListPage.this, "删除失败", Toast.LENGTH_SHORT)
				.show();
			}
		}
		

		private void blogListHandle(Message msg) {
			Bundle data = msg.getData();
			int code = data.getInt("code");
			if (code == 200) {
				String body = data.getString("body");
				try {
					JSONObject root = new JSONObject(body);
					JSONArray blogList = root.getJSONArray("blogs");
					if (blogList.length() == 0) {
						Toast.makeText(BlogListPage.this, "用户暂无blog！",
								Toast.LENGTH_SHORT);
					} else {
						listItem.removeAll(listItem);
						for (int i = 0; i < blogList.length(); i++) {
							JSONObject blog = blogList.getJSONObject(i);
							initialBlogAdapterItem(blog);
						}
					}
					bListAdapter.notifyDataSetChanged();
					if (msg.what  == 1) {
						Toast.makeText(BlogListPage.this, "刷新成功", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(BlogListPage.this, "获取blog异常",
						Toast.LENGTH_SHORT);
			}
		}

		public void initialBlogAdapterItem(JSONObject blog)
				throws JSONException {
			String blogid = blog.getString("blogid");
			String title = blog.getString("title");
			String summary = blog.getString("summary");
			String createTime = blog.getString("createTime");
			createTime = commonFuntion.fromatTime(createTime);
			String modifyTime = blog.getString("modifyTime");
			modifyTime = commonFuntion.fromatTime(modifyTime);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("blogId", blogid);
			map.put("title", title);
			map.put("abstruct", summary);
			map.put("createTime", createTime);
			listItem.add(map);
		}

	};

	Runnable runBlogList = new Runnable() {

		@Override
		public void run() {
			String url = GlobalParameters.MiniBlogInterface.GET_BLOG_LIST;
			HttpEngine httpEngine = new HttpEngine();
			Bundle data = new Bundle();
			data = httpEngine.getMethodBundle(url);
			Message msg = new Message();
			msg.setData(data);
			msg.what = 0;
			msg.setData(data);
			handler.sendMessage(msg);

		}
	};
	
	Runnable runDelBlog = new Runnable() {
		
		@Override
		public void run() {
			String blogId = (String) listItem.get(blogIndex).get("blogId");
			String url = GlobalParameters.MiniBlogInterface.DELETE_BLOG + "?blogid=" + blogId;
			Log.d(TAG, url);
			HttpEngine httpEngine = new HttpEngine();
			Bundle data = new Bundle();
			data = httpEngine.getMethodBundle(url);
			Message msg = new Message();
			msg.setData(data);
			msg.what = 2;
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};
	
	Runnable runRefleshBlogList = new Runnable() {

		@Override
		public void run() {
			String url = GlobalParameters.MiniBlogInterface.GET_BLOG_LIST;
			HttpEngine httpEngine = new HttpEngine();
			Bundle data = new Bundle();
			data = httpEngine.getMethodBundle(url);
			Message msg = new Message();
			msg.setData(data);
			msg.what = 1;
			handler.sendMessage(msg);

		}
	};

	/**
	 * 获取用户的所有blog
	 * 
	 * @param token
	 */
	public void getUserBlogList(String token) {
		for (int i = 0; i < 30; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("blogId", "blogId" + i);
			map.put("title", "title" + i);
			map.put("abstruct", "abstruct" + i);
			map.put("updateTime", "updateTime" + i);
			listItem.add(map);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signout_button:
			toRegisterPage();
			break;
		case R.id.reflesh_button:
			new Thread(runRefleshBlogList).start();
			break;

		case R.id.writeblog_button:
			toWriteBlogPage();
			break;
		default:
			break;
		}
	}

	private void toWriteBlogPage() {
		Intent intent = new Intent(BlogListPage.this, BlogAddPage.class);
		intent.putExtra("fromPage", "BlogListPage");
		intent.putExtra("toPage", "BlogAddPage");
		intent.putExtra("token", token);
		startActivity(intent);
	}

	private void toRegisterPage() {
		Intent intent = new Intent(BlogListPage.this, MainActivity.class);
		intent.putExtra("fromPage", "BlogListPage");
		intent.putExtra("toPage", "RegisterPage");
		startActivity(intent);
	}

}
