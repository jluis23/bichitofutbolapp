package com.dayscript.bichitofutbolapp.persistence.entity;


public class AlbumEntity extends BaseEntity{
public int _id;
public int id;
public String titulo, descripcion, imagen_portada,fechaPublicacion, numeroFotos;


public String getFechaPublicacion() {
	return fechaPublicacion;
}
public void setFechaPublicacion(String fechaPublicacion) {
	this.fechaPublicacion = fechaPublicacion;
}
public String getNumeroFotos() {
	return numeroFotos;
}
public void setNumeroFotos(String numeroFotos) {
	this.numeroFotos = numeroFotos;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitulo() {
	return titulo;
}
public void setTitulo(String titulo) {
	this.titulo = titulo;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public String getImagen_portada() {
	return imagen_portada;
}
public void setImagen_portada(String imagen_portada) {
	this.imagen_portada = imagen_portada;
}

@Override
public String toString() {
	return titulo;
}

}
