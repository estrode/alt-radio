package com.estrode.altradio;

import java.io.IOException;

import com.squareup.picasso.Picasso;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private AltRadio globalState;
	private MediaPlayer mMediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		globalState = (AltRadio)getApplicationContext();
		globalState.popSong();
		
        TextView songNameView = (TextView) findViewById(R.id.songTitleView);
        TextView songDetailView = (TextView) findViewById(R.id.songDescriptionView);
        TextView stationDetailView = (TextView) findViewById(R.id.stationNameView);
        ImageView albumArtView = (ImageView) findViewById(R.id.albumArtView);

        // 4. Set the text for textView 
        songNameView.setText(globalState.getCurrentSong().getTitle());
        songDetailView.setText(globalState.getCurrentSong().getDescription());
        stationDetailView.setText(globalState.getCurrentSong().getStationName());
        Picasso.with(this).load(globalState.getCurrentSong().getAlbumArtUrl()).placeholder(R.drawable.ic_launcher).into(albumArtView);
		findViewById(R.id.skipButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptPlay();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void attemptPlay() {
		try {
			mMediaPlayer.setDataSource(globalState.getCurrentSong().getAudioUrl());
			mMediaPlayer.prepareAsync();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() 
		{
			@Override
			public void onPrepared(MediaPlayer mp) 
			{
				mp.start();
			}
		});
	}

}
