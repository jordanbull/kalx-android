package edu.berkeley.kalxstreaming;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class Stream {
	
	private static final String STREAMURL = "http://icecast.media.berkeley.edu:8000/kalx-128.mp3";
	//private static final String STREAMURL = "http://icecast.media.berkeley.edu:8000/kalx-128.ogg.m3u";
	private MediaPlayer mediaPlayer;
	private ImageButton playStopButton;
	private Context context;
	private boolean preparing = true; //flag that is true if a method is being executed
	private boolean playing = true; //flag that is true if playing
	protected AsyncTask at;
	
	public Stream(Context ctxt, ImageButton playStop) {
		this.context = ctxt;
		this.playStopButton = playStop;
		playStopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playing) {
					preparing = true;
					
					if (stopStream()) {
						playStopButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.playbutton));
						playing = false;
					}
					preparing = false;
					
				} else if (!preparing) {
					preparing = true;
					playing = true;
					playStopButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stopbutton));
					startStream();
				}
			}
		});
		startStream();
	}
	
	public void startStream() {
		
		at = new AsyncTask<Object, Object, MediaPlayer>() {

			@Override
			protected MediaPlayer doInBackground(Object... arg0) {
				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					try {
						mediaPlayer.setDataSource(STREAMURL);
					} catch (IllegalArgumentException e) {
						Log.e("KALX ERROR","", e);
					} catch (SecurityException e) {
						Log.e("KALX ERROR","", e);
					} catch (IllegalStateException e) {
						Log.e("KALX ERROR","", e);
					} catch (IOException e) {
						Log.e("KALX ERROR","", e);
					}
					
				}
				try {
					mediaPlayer.prepare();
					mediaPlayer.start();
					//playing = true;
				} catch (IllegalStateException e) {
					Log.e("KALX ERROR","", e);
				} catch (IOException e) {
					Log.e("KALX ERROR","", e);
				}
				//mediaPlayer.start();
				
				//playing = true;
				at = null;
				preparing = false;
				
				return mediaPlayer;
			}
			
		};
		at.execute(new Object[]{});
		
	}
	
	public boolean stopStream() {
		
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			return true;
		} else {
			return false;
		}
	}
	
}
