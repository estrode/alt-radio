package com.estrode.altradio.pandora;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Station {

	private Pandora pandora;
	private String id;
	private String idToken;
	private String name;
	private List<Song> songs;
		
	public Station(Pandora pandora, JSONObject station) {
		this.pandora = pandora;
		this.id = station.optString("stationId");
		this.idToken = station.optString("stationToken");
		this.name = station.optString("stationName");
	}
	
	public void getPlaylist() {
		this.songs = new ArrayList<Song>();
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
	public List<Song> getSongs() {
		return songs;
	}
	public String toString() {
		return name;
	}

}
