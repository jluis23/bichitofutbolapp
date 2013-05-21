package com.dayscript.bichitofutbolapp.persistence.entity;

public class ChampionshipDate extends BaseEntity {

		int id,number;
		String date,description;
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		@Override
		public String toString()
		{
			return description + " " + date;
		}
}
