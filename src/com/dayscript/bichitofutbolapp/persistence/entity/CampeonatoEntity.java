package com.dayscript.bichitofutbolapp.persistence.entity;

import java.util.ArrayList;

public class CampeonatoEntity extends BaseEntity{
	public int id;
	public String nombreCampeonato;
	public int anio;
	public String descripcion;
	public int fecha_actual;
	public String estado;
	public int ganador;
	public String codigo_campeonato;
	public ArrayList<BaseEntity> fechas;
	
	@Override
	public String toString() {
		return nombreCampeonato;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombreCampeonato;
	}
	public void setNombre(String nombre) {
		this.nombreCampeonato = nombre;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getFecha_actual() {
		return fecha_actual;
	}
	public void setFecha_actual(int fecha_actual) {
		this.fecha_actual = fecha_actual;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getGanador() {
		return ganador;
	}
	public void setGanador(int ganador) {
		this.ganador = ganador;
	}
	public String getCodigo_campeonato() {
		return codigo_campeonato;
	}
	public void setCodigo_campeonato(String codigo_campeonato) {
		this.codigo_campeonato = codigo_campeonato;
	}
	public ArrayList<BaseEntity> getFechas() {
		return fechas;
	}
	public void setFechas(ArrayList<BaseEntity> fechas) {
		this.fechas = fechas;
	}
	
	
}
