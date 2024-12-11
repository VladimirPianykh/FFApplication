package com.application.workers;

public enum TeamType{
	SAWING("Бригада по распиловке"),
	DRYING("Бригада по сушке"),
	PROCESSING("Бригада по обработке"),
	PELLET("Пеллетная бригада");
	private String translation;
	private TeamType(String translation){this.translation=translation;}
	public String toString(){return translation;}
}
