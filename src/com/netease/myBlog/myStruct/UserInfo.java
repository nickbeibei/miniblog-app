package com.netease.myBlog.myStruct;

import android.os.Parcel;
import android.os.Parcelable;


public class UserInfo implements Parcelable{
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private String account = "";
	private String password = "";
	private String nickName = "";
	private String session = "";
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Creator<UserInfo>() {

		@Override
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		@Override
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(account);
		dest.writeString(password);
		dest.writeString(nickName);
		dest.writeString(session);
	}
	
	public UserInfo() {
		
	}
	private UserInfo(Parcel in) {
		account = in.readString();
		password = in.readString();
		nickName = in.readString();
		session = in.readString();
	}

}
