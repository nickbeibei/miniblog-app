package com.netease.myBlog.page;


import java.io.FileNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.minblog.R;
import com.netease.myBlog.myStruct.BlogUnit;
import com.netease.myBlog.myStruct.GlobalParameters;
import com.netease.myBlog.network.HttpEngine;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class BlogEditPage extends Activity implements OnClickListener {
	public final String TAG = "jcdebug";
	private String blogId = "";
	private EditText contentEText = null;
	private EditText titleEText = null;
	private Button backBtn = null;
	private Button publishBtn = null;
	private BlogUnit blogUnit = new BlogUnit();
	private String toPage = "";
	private String title = "";
	private String content = "";
	private String pictures = "";
	private String showPicture = "";
	private JSONArray picJsonArray = null;
//	private ImageView showImageView = null;
//	private Bitmap showBitmap = null;
	private static int RESULT_LOAD_IMAGE = 1;
	private Button selectImageBtn = null;
	private int showPictureIndex = -1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_edit_page);
		publishBtn = (Button) findViewById(R.id.publish_blog_button);
		backBtn = (Button) findViewById(R.id.editPage_back_button);
		titleEText = (EditText) findViewById(R.id.title_EditeTextView);
		contentEText = (EditText) findViewById(R.id.context_editTextView);
//		showImageView = (ImageView) findViewById(R.id.image_textView);
		selectImageBtn = (Button) findViewById(R.id.select_pic_button);
		
		publishBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		
		selectImageBtn.setOnClickListener(this);
		
//		showImageView.setOnClickListener(this);
		
		
//		showImageView.setOnLongClickListener(new PicOnLongClick());
		Intent intent = getIntent();
		blogId = intent.getStringExtra("blogId");
		title = intent.getStringExtra("title");
		content = intent.getStringExtra("content");
		pictures = intent.getStringExtra("pictures");
		try {
			picJsonArray = new JSONArray(pictures);
			showPictureIndex = picJsonArray.length() -1;
			if (showPictureIndex >= 0) {
				showPicture = picJsonArray.getString(picJsonArray.length() -1);
//				new Thread(runShowImage).start();
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		titleEText.setText(title);
		contentEText.setText(content);
	}
	
//	 private class PicOnLongClick implements OnLongClickListener{
//		
//		@Override
//		public boolean onLongClick(View v) {
//			Log.d(TAG, "inininin");
//			Builder build = new AlertDialog.Builder(BlogEditPage.this);
//			build.setTitle("确认框");
//			build.setMessage("是否删除图片");
//			build.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//				
//				@SuppressLint("NewApi")
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					picJsonArray.remove(showPictureIndex);
//					if (showPictureIndex >= 0) {
//						try {
//							showPicture = picJsonArray.getString(picJsonArray.length() -1);
//							new Thread(runShowImage).start();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						if (picJsonArray.length() == 0 ) {
//							showImageView.setVisibility(View.INVISIBLE);
//							
//						}
//					}
//				}
//			});
//			build.setNegativeButton("no", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					
//				}
//			});
//			build.create().show();
//			return false;
//		}  
//		 
//	 }
	
//	Runnable runShowImage = new Runnable() {
//
//		@Override
//		public void run() {
//			HttpEngine httpEngine = new HttpEngine();
//			showBitmap = httpEngine.getMethodPicture(showPicture);
//			Message msg = new Message();
//			Bundle data = new Bundle();
//			if (showBitmap == null) {
//				data.putString("code", "0");
//			} else {
//				data.putString("code", "1");
//			}
//			msg.setData(data);
//			msg.what = 2;
//			handler.sendMessage(msg);
//			
//		}
//	};
	

	Runnable getBlogDetail = new Runnable() {

		@Override
		public void run() {
			HttpEngine httpEngine = new HttpEngine();
			Bundle data = new Bundle();
			String url = GlobalParameters.MiniBlogInterface.GET_BLOG_DETAIL;
			url += "?blogid=" + blogId;
			data = httpEngine.getMethodBundle(url);
			Message msg = new Message();
			msg.what = 0;
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	Runnable updateBlogDetail = new Runnable() {

		@Override
		public void run() {
			HttpEngine httpEngine = new HttpEngine();
			Bundle data = new Bundle();
			String url = GlobalParameters.MiniBlogInterface.UPDATE_BLOG
					+ "?blogid=" + blogId;
			Log.d(TAG, url);
			String title = titleEText.getText().toString();
			String content = contentEText.getText().toString();
			JSONObject js = new JSONObject();
			try {
				js.put("title", title);
				js.put("content", content);
				Log.d(TAG, "test1:" + picJsonArray.toString());
				js.put("pictures", picJsonArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "update:" + js.toString());
			data = httpEngine.postMethodBundle(url, js.toString());
			Message msg = new Message();
			msg.what = 1;
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
				handlerBlogDetail(msg);
				break;
			case 1:
				handlerUpdateBlog(msg);
				break;
			case 2:
				handlerShowImage(msg);
				break;
			case 3:
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
				picJsonArray.put(url);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		private void handlerShowImage(Message msg) {
//			showImageView.setImageBitmap(showBitmap);
		}

		private void handlerUpdateBlog(Message msg) {
			Bundle rs = msg.getData();
			int code = 0;
			code = rs.getInt("code");
			if (code == 200) {
				Toast.makeText(BlogEditPage.this, "更新成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(BlogEditPage.this,
						BlogDetailPage.class);
				intent.putExtra("blogId", blogId);
				intent.putExtra("fromPage", "BlogEditPage");
				intent.putExtra("toPage", "BlogDetailPage");
				startActivity(intent);
			} else {
				Toast.makeText(BlogEditPage.this, "更新失败", Toast.LENGTH_SHORT)
						.show();
			}
		}

		private void handlerBlogDetail(Message msg) {
			Bundle data = msg.getData();
			int code = data.getInt("code");
			if (code == 200) {
				String body = data.getString("body");
				try {
					JSONObject root = new JSONObject(body);
					String title = root.getString("title");
					String content = root.getString("content");
					String createTime = root.getString("createTime");
					titleEText.setText(title);
					contentEText.setText(content);
					titleEText.setText(createTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(BlogEditPage.this, "获取blog详情失败",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_blog_button:
			new Thread(updateBlogDetail).start();
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
//		        new Thread(runUpLoadPictrue).start();
//		        showImageView.setVisibility(View.VISIBLE);
//		        showImageView.setImageBitmap(showBitmap);
//				
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "" + e);
//				e.printStackTrace();
//			} 
//		}
//	}
	
	
//	Runnable runUpLoadPictrue = new Runnable() {
//		@Override
//		public void run() {
//			HttpEngine httpClient = new HttpEngine();
//			Bundle bundle = new Bundle();
//			bundle = httpClient.postMethodPicture(GlobalParameters.MiniBlogInterface.UPDATE_PICTURE, showBitmap);
//			Message msg = new Message();
//			msg.what = 3;
//			msg.setData(bundle);
//			handler.sendMessage(msg);
//			Log.d(TAG, "picture" + bundle.getString("body"));
//		}
//	};
	
	private void toOtherPage(String toPage) {
		Log.d(TAG, "fromPage:" + toPage);
		Intent intent = new Intent(BlogEditPage.this, BlogDetailPage.class);
		intent.putExtra("fromPage", "BlogEditPage");
		intent.putExtra("toPage", "BlogDetailPage");
		intent.putExtra("blogId", blogId);
		startActivity(intent);
	}

	/**
	 * 更新blog
	 * 
	 * @param token
	 * @param blogId
	 */
	public int postUpdateBlog(String token, String blogId) {
		int rs = 0;
		String title;
		String content;
		title = titleEText.getText().toString();
		content = contentEText.getText().toString();
		blogUnit.setTitle(title);
		blogUnit.setContent(content);
		return rs;
	}

}
