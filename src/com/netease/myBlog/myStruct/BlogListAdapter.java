package com.netease.myBlog.myStruct;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minblog.R;
import com.netease.myBlog.network.HttpEngine;
import com.netease.myBlog.page.BlogDetailPage;
import com.netease.myBlog.page.BlogEditPage;
import com.netease.myBlog.page.BlogListPage;

/**
 * blog 列表页Adapter
 * 
 * @author hzjiangcong
 *
 */
public class BlogListAdapter extends BaseAdapter {
	public final String TAG = "jcdebug";
	private LayoutInflater myInflater = null;
	private Context mycontext = null;
	private int blogIndex;
	
	OnClickListener myListener;
	
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

	
	public BlogListAdapter(Context myContext,
			ArrayList<HashMap<String, Object>> data, OnClickListener onClickListener) {
		this.myInflater = LayoutInflater.from(myContext);// 得到LayoutInlater对象，来导入布局
		this.data = data;// 显示数据初始化
		this.mycontext = myContext;
		myListener = onClickListener;
	}

	@Override
	public int getCount() {
		return data.size();// 返回数据个数
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder;// 提高listView性能，

		if (convertView == null) {

			convertView = myInflater.inflate(R.layout.activity_blog_item_page,
					null);// 导入布局
			vHolder = new ViewHolder();
			vHolder.title = (TextView) convertView
					.findViewById(R.id.title_textView);
			vHolder.abstruct = (TextView) convertView
					.findViewById(R.id.abstruct_textView);
			vHolder.updateTime = (TextView) convertView
					.findViewById(R.id.updatetime_textView);

			vHolder.layoutView = convertView.findViewById(R.id.linearLayout2);

			vHolder.delete_button = (Button) convertView
					.findViewById(R.id.delete_button);
			convertView.setTag(vHolder);// 已绘制view存在内存
		} else {
			vHolder = (ViewHolder) convertView.getTag();// 将已绘制的view从内存中读取
		}
		vHolder.title.setText(data.get(position).get("title").toString());// 数据赋值到具体控件
		vHolder.abstruct.setText(data.get(position).get("abstruct").toString());
		vHolder.updateTime.setText(data.get(position).get("createTime")
				.toString());
		// 将位置信息赋值对应的button,给button额外添加一个属性,只有一个哦，再次调用会覆盖原有值，赋值可以给任何对象
		vHolder.delete_button.setTag(position);
		vHolder.layoutView.setTag(position);
		// button触发click，回调myListener
		//？？？？？？？？？？？？，是否可以放在外部进行的page进行监听
//		vHolder.delete_button.setOnClickListener(myListener);
		vHolder.layoutView.setOnClickListener(myListener);
		return convertView;
	}

	// 存放控件，提高listview滑动的性能（重用控件）
	public final class ViewHolder {
		public TextView title;
		public TextView abstruct;
		public TextView updateTime;
		public Button delete_button;
		public View layoutView;

	}

//	private class MyListener implements OnClickListener {
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.delete_button:
//				deleteBtnFun(v);
//				break;
//			case R.id.linearLayout2:
//				toBlogDetailPageFun(v);
//				break;
//			default:
//				break;
//			}
//		}
//
//		public void deleteBtnFun(View v) {
//			blogIndex = (Integer) v.getTag();
//			Log.d(TAG, "" + blogIndex);
//			Toast.makeText(mycontext,
//					(CharSequence) data.get(blogIndex).get("title"),
//					Toast.LENGTH_SHORT).show();
//		}
//
//		public void toBlogDetailPageFun(View v) {
//			blogIndex = (Integer) v.getTag();
//			Intent intent = new Intent(mycontext, BlogDetailPage.class);
//			intent.putExtra("toPage", "BlogEditPage");
//			intent.putExtra("fromPage", "BlogListPage");
//			intent.putExtra("blogId", data.get(blogIndex).get("blogId").toString());
//			mycontext.startActivity(intent);
//		}
//	}
	
//	Runnable runDelBlog = new Runnable() {
//		
//		@Override
//		public void run() {
//			HttpEngine httpEngine = new HttpEngine();
//			Bundle rs = new Bundle();
//			String url = GlobalParameters.MiniBlogInterface.DELETE_BLOG;
//			url += "?blogid=" + data.get(blogIndex).get("blogId");
//			rs = httpEngine.getMethodBundle(url);
//			Message msg = new Message();
//			msg.what = 0;
//			handler.sendMessage(msg);
//		}
//	};
//	
//	Runnable runBlogDetail = new Runnable() {
//		
//		@Override
//		public void run() {
//			HttpEngine httpEngine = new HttpEngine();
//			Bundle rs = new Bundle();
//			String url = GlobalParameters.MiniBlogInterface.GET_BLOG_DETAIL;
//			url += "?blogid=" + data.get(blogIndex).get("blogId");
//			rs = httpEngine.getMethodBundle(url);
//			Message msg = new Message();
//			msg.what = 1;
//			handler.sendMessage(msg);
//		}
//	};
//	
//	Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				handlerDelBlog(msg);
//				break;
//			case 1:
//				handlerBlogDetail(msg);
//				break;
//			default:
//				break;
//			}
//		}
//
//		private void handlerBlogDetail(Message msg) {
//			Bundle bundle = msg.getData();
//			int code = bundle.getInt("code");
//			if (code == 200) {
//				Toast.makeText(mycontext, "删除成功，请刷新", Toast.LENGTH_SHORT)
//				.show();
//			} else {
//				Toast.makeText(mycontext, "删除失败", Toast.LENGTH_SHORT)
//				.show();
//			}			
//		}
//
//		private void handlerDelBlog(Message msg) {
//			Bundle bundle = msg.getData();
//			int code = bundle.getInt("code");
//			if (code == 200) {
//				Toast.makeText(mycontext, "删除成功，请刷新", Toast.LENGTH_SHORT)
//				.show();
//				
//				notifyDataSetChanged();
//			} else {
//				Toast.makeText(mycontext, "删除失败", Toast.LENGTH_SHORT)
//				.show();
//			}
//		}
//	};

}
