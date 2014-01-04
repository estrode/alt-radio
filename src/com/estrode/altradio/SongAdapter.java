package com.estrode.altradio;

import java.util.List;

import com.estrode.altradio.pandora.Song;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends ArrayAdapter<Song> {
	 
    private final Context context;
    private final List<Song> songsArrayList;

    public SongAdapter(Context context, List<Song> songsArrayList) {

        super(context, R.layout.song_item, songsArrayList);

        this.context = context;
        this.songsArrayList = songsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater 
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.song_item, parent, false);

        // 3. Get the two text view from the rowView
        TextView songNameView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView songDetailView = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView albumArtView = (ImageView) rowView.findViewById(R.id.icon);

        // 4. Set the text for textView 
        songNameView.setText(songsArrayList.get(position).getTitle());
        songDetailView.setText(songsArrayList.get(position).getDescription());
        Picasso.with(context).load(songsArrayList.get(position).getAlbumArtUrl()).resize(50, 50).centerCrop().placeholder(R.drawable.ic_launcher).into(albumArtView);

        // 5. return rowView
        return rowView;
    }
} 
