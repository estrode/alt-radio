package com.estrode.altradio.pandora;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Song {

	private Pandora pandora;
	private String id;
	private String idToken;
	private String name;
	private String albumName;
	private String artistName;
	private JSONObject audioUrlMap;
	private String trackToken;
	private String songRating;
	private String stationId;
	private String songName;
	private String songDetailUrl;
	private String songExplorerUrl;
	private String albumArtUrl;
	private Station station;
		
	public Song(Pandora pandora, Station station, JSONObject song) {
		this.pandora = pandora;
		this.albumName = song.optString("albumName");
		this.artistName = song.optString("artistName");
		this.audioUrlMap = song.optJSONObject("audioUrlMap");
		this.trackToken = song.optString("trackToken");
		this.songRating = song.optString("songRating");
		this.stationId = song.optString("stationId");
		this.songName = song.optString("songName");
		this.songDetailUrl = song.optString("songDetailUrl");
		this.songExplorerUrl = song.optString("songExplorerUrl");
		this.albumArtUrl = song.optString("albumArtUrl");
		this.station = station;
	}
	
	public String toString() {
		return songName;
	}
	
	public String getTitle() {
		return songName;
	}
	
	public String getDescription() {
		return ("by " + artistName + " on " + albumName);
	}
	
	public String getAlbumArtUrl() {
		return albumArtUrl;
	}

}
