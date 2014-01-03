package com.estrode.altradio;

import com.estrode.altradio.pandora.Station;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NowPlayingActivity extends ListActivity {

	private AltRadio globalState;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		globalState = (AltRadio)getApplicationContext();
		SongAdapter adapter = new SongAdapter(this, globalState.getPlaylist());
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

}
