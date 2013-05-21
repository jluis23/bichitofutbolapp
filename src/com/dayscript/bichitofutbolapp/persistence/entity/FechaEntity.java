package com.dayscript.bichitofutbolapp.persistence.entity;

public class FechaEntity extends BaseEntity {
	public int id;
	public int number;
	public String description;
	public String date;
	public String actualSiguiente;
	public int getId() {
		return id;
	}
	public String getActualSiguiente() {
		return actualSiguiente;
	}
	public void setActualSiguiente(String actualSiguiente) {
		this.actualSiguiente = actualSiguiente;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return description;
	}
}
