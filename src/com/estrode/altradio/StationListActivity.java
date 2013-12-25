package com.estrode.altradio;

import com.estrode.altradio.pandora.Pandora;
import com.estrode.altradio.pandora.Station;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class StationListActivity extends ListActivity {
	private Pandora pandora;

	protected void onCreate(Bundle savedInstanceState, Pandora pandora) {
		super.onCreate(savedInstanceState);
		
		this.pandora = pandora;
		ArrayAdapter<Station> adapter = new ArrayAdapter<Station>(this, android.R.layout.simple_list_item_1, pandora.getStationList());
		setListAdapter(adapter);
	}

}
