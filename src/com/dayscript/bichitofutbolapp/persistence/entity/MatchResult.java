package com.dayscript.bichitofutbolapp.persistence.entity;


public class MatchResult extends BaseEntity {

	
	public int _id;
	public int id;
	public String date,team1,team2,goalsTeam1,goalsTeam2,stadium, month,time,logoTeam1, logoTeam2,estado;
	public String getLogoTeam1() {
		return logoTeam1;
	}
	public void setLogoTeam1(String logoTeam1) {
		this.logoTeam1 = logoTeam1;
	}
	public String getLogoTeam2() {
		return logoTeam2;
	}
	public void setLogoTeam2(String logoTeam2) {
		this.logoTeam2 = logoTeam2;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTeam1() {
		return team1;
	}
	public void setTeam1(String team1) {
		this.team1 = team1;
	}
	public String getTeam2() {
		return team2;
	}
	public void setTeam2(String team2) {
		this.team2 = team2;
	}
	public String getGoalsTeam1() {
		return goalsTeam1;
	}
	public void setGoalsTeam1(String goalsTeam1) {
		this.goalsTeam1 = goalsTeam1;
	}
	public String getGoalsTeam2() {
		return goalsTeam2;
	}
	public void setGoalsTeam2(String goalsTeam2) {
		this.goalsTeam2 = goalsTeam2;
	}
	
	public String getStadium() {
		return stadium;
	}
	public void setStadium(String stadium) {
		this.stadium = stadium;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "MatchResult [_id=" + _id + ", id=" + id + ", date=" + date
				+ ", team1=" + team1 + ", team2=" + team2 + ", goalsTeam1="
				+ goalsTeam1 + ", goalsTeam2=" + goalsTeam2 + ", stadium="
				+ stadium + ", month=" + month + ", time=" + time
				+ ", logoTeam1=" + logoTeam1 + ", logoTeam2=" + logoTeam2 + "]";
	}
	
}
