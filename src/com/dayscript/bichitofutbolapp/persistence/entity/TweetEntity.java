package com.dayscript.bichitofutbolapp.persistence.entity;


public class TweetEntity extends BaseEntity{
public int _id;
public String user,userName,text,profileImage,dateFormat;
public int date;
public String getUser() {
	return user;
}
public void setUser(String user) {
	this.user = user;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public String getProfileImage() {
	return profileImage;
}
public void setProfileImage(String profileImage) {
	this.profileImage = profileImage;
}
public String getDateFormat() {
	return dateFormat;
}
public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
}
public int getDate() {
	return date;
}
public void setDate(int date) {
	this.date = date;
}


}
