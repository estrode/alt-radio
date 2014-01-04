package com.estrode.altradio.pandora;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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
	
	public Boolean getPlaylist() {
		this.songs = new ArrayList<Song>();
		JSONObject requestBody = new JSONObject();
		try {
			requestBody.put("stationToken", idToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject response = pandora.sendJsonRequest("station.getPlaylist", requestBody, true, true);
		System.out.println(response);
		JSONObject result = response.optJSONObject("result");
		JSONArray jsonSongs = result.optJSONArray("items");
		for (int i = 0; i < jsonSongs.length(); i++) {
			JSONObject item = jsonSongs.optJSONObject(i);
			if (item.has("songName")) { //checks for ads
				songs.add(new Song(pandora, this, item));
			}
		}
		return (response.optString("stat").equals("ok"));
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
	public Song popSong() {
		Song song = songs.get(0);
		songs.remove(0);
		return song;
	}

}
