package com.estrode.altradio;

import com.estrode.altradio.StationListActivity.SongTask;
import com.estrode.altradio.pandora.Station;
import com.squareup.picasso.Picasso;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NowPlayingActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private AltRadio globalState;
	private SongTask songTask;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_now_playing);

		globalState = (AltRadio)getApplicationContext();
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(new ArrayAdapter<Station>(getActionBarThemedContextCompat(), android.R.layout.simple_list_item_1, globalState.getStations()), this);
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.now_playing, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		
		globalState.setCurrentStation(position);
		songTask = new SongTask();
		songTask.execute((Void) null);
		return true;
	}
	
	public void showSong() {
		Fragment fragment = new SongSectionFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class SongSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public SongSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			AltRadio app = (AltRadio)getActivity().getApplicationContext();
			View rootView = inflater.inflate(
					R.layout.fragment_now_playing_dummy, container, false);
			
			TextView songNameView = (TextView) rootView.findViewById(R.id.songNameView);
			TextView songArtistView = (TextView) rootView.findViewById(R.id.songArtistView);
			TextView stationView = (TextView) rootView.findViewById(R.id.stationView);
			ImageView albumArtView = (ImageView) rootView.findViewById(R.id.albumArtView);
			
	        songNameView.setText(app.getCurrentSong().getTitle());
	        songArtistView.setText(app.getCurrentSong().getDescription());
	        stationView.setText(app.getCurrentSong().getStationName());
	        Picasso.with(getActivity()).load(app.getCurrentSong().getAlbumArtUrl()).placeholder(R.drawable.ic_launcher).into(albumArtView);
	        
			return rootView;
		}
	}
	
	public class SongTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return globalState.getPlaylist();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				showSong();
			} 
		}

		@Override
		protected void onCancelled() {

		}
	}


}
