package com.estrode.altradio;

import com.estrode.altradio.pandora.Pandora;
import com.estrode.altradio.pandora.Station;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class StationListActivity extends ListActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AltRadio app = (AltRadio)getApplicationContext();
		ArrayAdapter<Station> adapter = new ArrayAdapter<Station>(this, android.R.layout.simple_list_item_1,app.getPandora().getStationList());
		setListAdapter(adapter);
	}

}
