package com.estrode.altradio;

import com.estrode.altradio.pandora.Pandora;

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

}
