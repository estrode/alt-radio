package com.estrode.altradio;

import java.util.ArrayList;
import java.util.List;

import com.estrode.altradio.pandora.Pandora;
import com.estrode.altradio.pandora.Song;
import com.estrode.altradio.pandora.Station;

import android.app.Application;

public class AltRadio extends Application {
	private Pandora pandora;
	private Station currentStation;
	
	  @Override
	  public void onCreate() {
	    super.onCreate();
	     
	    this.pandora = new Pandora();
	  }
	  public Pandora getPandora() {
		  return pandora;
	  }
	  public List<Station> getStations() {
		  return pandora.getStationList();
	  }
	  public void setCurrentStation(int position) {
		  currentStation = pandora.getStationList().get(position);
	  }
	  
	  public Boolean getPlaylist() {
		  return currentStation.getPlaylist();
	  }
	  
	  public List<Song> getSongs() {
		  return currentStation.getSongs();
	  }

}
