package com.netease.myBlog.page;

import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.minblog.R;
import com.example.minblog.R.id;
import com.example.minblog.R.layout;
import com.example.minblog.R.menu;
import com.netease.myBlog.myStruct.BlogUnit;
import com.netease.myBlog.myStruct.GlobalParameters;
import com.netease.myBlog.network.HttpEngine;

import android.R.color;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.PictureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class BlogAddPage extends Activity implements OnClickListener{


	private EditText titleEText = null;
	private EditText contentEText = null;
	private Button backBtn = null;
	private Button selectImageBtn = null;
	private ImageView imageView = null;
	Button publishBtn = null;
	private String toPage = "";
	public final String TAG = "jcdebug";
	public List<String> pictureList = new ArrayList<String>();
	private Bitmap showBitmap;
	private static int RESULT_LOAD_IMAGE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_blog_edit_page);
		
		Intent intent = getIntent();

		toPage = intent.getStringExtra("fromPage");
		
		//获取控件名称
		publishBtn = (Button) findViewById(R.id.publish_blog_button);
//		//设置控件属性
//		publishBtn.setText("标题");
//		publishBtn.setBackgroundColor(color.background_dark);
//		publishBtn.setOnClickListener(this);	//监控控件
//		
		titleEText = (EditText) findViewById(R.id.title_EditeTextView);
		contentEText = (EditText) findViewById(R.id.context_editTextView);
		backBtn = (Button) findViewById(R.id.editPage_back_button);
		imageView = (ImageView) findViewById(R.id.image_textView);
		selectImageBtn = (Button) findViewById(R.id.select_pic_button);
		
		backBtn.setOnClickListener(this);
		publishBtn.setOnClickListener(this);
		selectImageBtn.setOnClickListener(this);
		
		
		Log.d(TAG, toPage);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_blog_button:
			new Thread(runCreateBlog).start();
			break;
		case R.id.editPage_back_button:
			toOtherPage(toPage);
			break;
		case R.id.select_pic_button:
			selectPicture();
			break;
		default:
			break;
		}
	}
	private void selectPicture() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
//			getPictrue(requestCode, resultCode, data);
			break;

		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
//	public void getPictrue(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK && null != data) {
//			ContentResolver cr = this.getContentResolver();
//			Bitmap bitmap;
//			Uri uri = data.getData();
//			try {
//				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//				Matrix matrix = new Matrix();    
//		        matrix.postScale(0.2f, 0.2f);    
//		        showBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),    
//		        		bitmap.getHeight(), matrix, true); 
//		        
//				publishBtn.setClickable(false);
//				ColorStateList grayColor=getResources().getColorStateList(R.color.gray_dark);
//				publishBtn.setTextColor(grayColor);
//		        
////		        new Thread(runUpLoadPictrue).start();
//		        imageView.setImageBitmap(showBitmap);
//				
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "" + e);
//				e.printStackTrace();
//			} 
//		}
//	}
//	Runnable runUpLoadPictrue = new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//
//			HttpEngine httpClient = new HttpEngine();
//			Bundle bundle = new Bundle();
//			bundle = httpClient.postMethodPicture(GlobalParameters.MiniBlogInterface.UPDATE_PICTURE, showBitmap);
//			Message msg = new Message();
//			msg.what = 1;
//			msg.setData(bundle);
//			handler.sendMessage(msg);
//			Log.d(TAG, "picture" + bundle.getString("body"));
//		}
//	};
	


	Runnable runCreateBlog = new Runnable() {
		
		@Override
		public void run() {
			HttpEngine httpEngine = new HttpEngine();
			Bundle bundle = new Bundle();
			String title = titleEText.getText().toString();
			String content = contentEText.getText().toString();
			
			JSONObject js = new JSONObject();
			try {
				js.put("title", title);
				js.put("content", content);
				JSONArray pic = new JSONArray();
				for (int i = 0; i < pictureList.size(); i++) {
					pic.put(pictureList.get(i));
				}
				js.put("pictures", pic);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String url = GlobalParameters.MiniBlogInterface.CREATE_BLOG;
			bundle = httpEngine.postMethodBundle(url, js.toString());
			Message msg = new Message();
			msg.what = 0;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	};
	

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				handlerAddBlog(msg);
				break;
			case 1:
				handlerSetPublicAble(msg);
				break;
			default:
				break;
			}
		}

		private void handlerSetPublicAble(Message msg) {
			publishBtn.setClickable(true);
			ColorStateList orageColor=getResources().getColorStateList(R.color.orange);
			publishBtn.setTextColor(orageColor);
			Bundle data = msg.getData();
			String body = data.getString("body");
			try {
				JSONObject root = new JSONObject(body);
				String url = root.getString("url");
				pictureList.add(url);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void handlerAddBlog(Message msg) {
			Bundle data = msg.getData();
			String blogId = "";
			int code = data.getInt("code");
			if (code == 200) {
				
				Toast.makeText(BlogAddPage.this, "添加blog成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(BlogAddPage.this, BlogDetailPage.class);
				try {
					JSONObject root = new JSONObject(data.getString("body"));
					blogId = root.getString("blogid");
					intent.putExtra("blogId", blogId);
					intent.putExtra("fromPage", "BlogAddPage");
					intent.putExtra("toPage", "BlogDetailPage");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);

			} else {
				Toast.makeText(BlogAddPage.this, "添加blog失败", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	
	private void toOtherPage(Object toPage2) {
		if(toPage.equals("BlogListPage")){
			Intent intent = new Intent(BlogAddPage.this, BlogListPage.class);
			intent.putExtra("fromPage", "BlogAddPage");
			intent.putExtra("toPage", "BlogListPage");
			startActivity(intent);
		}		
	}
	private void toBlogDetailPage(String token) {
		Map<String, String> map = new HashMap<String, String>();
		String title = titleEText.getText().toString();
		String content = contentEText.getText().toString();
		BlogUnit bUnit = new BlogUnit();
		bUnit.setTitle(title);
		bUnit.setContent(content);
		
		if (title.length() < 1) {
			Toast.makeText(BlogAddPage.this, "title不为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		map = postAddBlog(token, bUnit);
		if (Integer.valueOf(map.get("code")) == 0) {
			Intent intent = new Intent(BlogAddPage.this, BlogDetailPage.class);
			intent.putExtra("blogId", map.get("blogId"));
			intent.putExtra("fromPage", "BlogAddPage");
			intent.putExtra("toPage", "BlogDetailPage");
			startActivity(intent);
		} else {
			
		}
	}

	/**
	 * http 
	 * add blog
	 * @param token
	 * @param bUnit
	 * @return
	 */
	private Map<String, String> postAddBlog(String token, BlogUnit bUnit) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "0");
		map.put("blogId", "1");
		return map;
	}
}
