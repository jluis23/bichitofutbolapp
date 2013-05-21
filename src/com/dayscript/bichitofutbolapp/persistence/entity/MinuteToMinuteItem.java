package com.dayscript.bichitofutbolapp.persistence.entity;

public class MinuteToMinuteItem extends BaseEntity{
String tipo, equipo, logoEquipo, nombreJugador, minuto;

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

public String getEquipo() {
	return equipo;
}

public void setEquipo(String equipo) {
	this.equipo = equipo;
}

public String getLogoEquipo() {
	return logoEquipo;
}

public void setLogoEquipo(String logoEquipo) {
	this.logoEquipo = logoEquipo;
}

public String getNombreJugador() {
	return nombreJugador;
}

public void setNombreJugador(String nombreJugador) {
	this.nombreJugador = nombreJugador;
}

public String getMinuto() {
	return minuto;
}

public void setMinuto(String minuto) {
	this.minuto = minuto;
}


}
