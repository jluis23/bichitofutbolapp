package com.dayscript.bichitofutbolapp.persistence.entity;


public class NewsItem extends BaseEntity{
public int _id;
public int id;
public String img, title, summary,extendedImg,fullText,fechaCreacion;
public String getFullText() {
	return fullText;
}

public String getFechaCreacion() {
	return fechaCreacion;
}

public void setFechaCreacion(String fechaCreacion) {
	this.fechaCreacion = fechaCreacion;
}

public void setFullText(String fullText) {
	this.fullText = fullText;
}

public int getId() {
	return id;
}

public String getExtendedImg() {
	return extendedImg;
}

public void setExtendedImg(String extendedImg) {
	this.extendedImg = extendedImg;
}

public void setId(int id) {
	this.id = id;
}
public String getImg() {
	return img;
}
public void setImg(String img) {
	this.img = img;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getSummary() {
	return summary;
}
public void setSummary(String summary) {
	this.summary = summary;
}
@Override
public String toString() {
	return "NewsItem [id=" + id + ", img=" + img + ", title=" + title
			+ ", summary=" + summary + "]";
}

}
