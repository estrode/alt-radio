package com.estrode.altradio.pandora;

import org.json.JSONObject;

public class Station {

	private Pandora pandora;
	private String id;
	private String idToken;
	private String name;
		
	public Station(Pandora pandora, JSONObject station) {
		this.pandora = pandora;
		this.id = station.optString("stationId");
		this.idToken = station.optString("stationToken");
		this.name = station.optString("stationName");
	}
	
	public String getId() {
		return id;
	}
	public String getIdToken() {
		return idToken;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}

}
