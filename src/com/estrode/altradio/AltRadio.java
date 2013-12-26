package com.estrode.altradio;

import java.util.List;

import com.estrode.altradio.pandora.Pandora;
import com.estrode.altradio.pandora.Station;

import android.app.Application;

public class AltRadio extends Application {
	private Pandora pandora;
	
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

}
