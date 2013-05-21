package com.dayscript.bichitofutbolapp.persistence.entity;


public class MediaEntity extends BaseEntity{
public int _id;
public int id;
public String descripcion, url;
public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}

@Override
public String toString() {
	return "MediaEntity [id=" + id + ", descripcion=" + descripcion + ", url="
			+ url + "]";
}


}
