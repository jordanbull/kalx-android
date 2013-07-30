package edu.berkeley.kalxstreaming;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class KALXMain extends Activity {

	Stream kalxStream;
	ImageButton playStopButton;
	SeekBar volumeBar;
	AudioManager audio;
	ImageButton muteUnmuteButton;
	int prevVol = 0;
	boolean muted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.kalx_layout);
		
		audio = (AudioManager) this.getSystemService(AUDIO_SERVICE);
		playStopButton = (ImageButton) findViewById(R.id.playStopButton);
		kalxStream = new Stream(this, playStopButton);
		muteUnmuteButton = (ImageButton) findViewById(R.id.muteUnmute);
		
		volumeBar = (SeekBar) findViewById(R.id.volumeBar);
		volumeBar.setMax(audio.getStreamMaxVolume(audio.STREAM_MUSIC));
		volumeBar.setProgress(audio.getStreamVolume(audio.STREAM_MUSIC));
		volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2) {
				audio.setStreamVolume(audio.STREAM_MUSIC, seekBar.getProgress(), 0);
			}
		});
		if (audio.getStreamVolume(audio.STREAM_MUSIC) == 0) {
			muteUnmuteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.unmutebutton));
			prevVol = audio.getStreamMaxVolume(audio.STREAM_MUSIC);
			muted = true;
		} else {
			prevVol = audio.getStreamVolume(audio.STREAM_MUSIC);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.kalxmain, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle MenuItem selection
	    switch (item.getItemId()) {
	        case R.id.Disclaimer:
	            showDisclaimer();
	            return true;
	        case R.id.Quit:
	            quit();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showDisclaimer() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.disclaimer).setTitle("");
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	private void quit() {
		if (kalxStream.at != null) {
			kalxStream.at.cancel(true);
		}
		kalxStream.stopStream();
		this.finish();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent e) {
		boolean b = super.onKeyUp(keyCode, e);
		if (volumeBar != null && audio != null) {
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
				volumeBar.setProgress(audio.getStreamVolume(audio.STREAM_MUSIC));
			} else if (keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
				//TODO
			}
		}
		return b;
	}
	
	public void muteUnmute(View v) {
		//TODO
		if (muted) {
			volumeBar.setProgress(prevVol);
			audio.setStreamVolume(audio.STREAM_MUSIC, prevVol, 0);
			muteUnmuteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mutebutton));
			muted = false;
		} else {
			prevVol = prevVol = audio.getStreamVolume(audio.STREAM_MUSIC);
			audio.setStreamVolume(audio.STREAM_MUSIC, 0, 0);
			volumeBar.setProgress(0);
			muteUnmuteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.unmutebutton));
			muted = true;
			
		}
	}
	
	public void openFacebook(View v) {
		Facebook.openKALXFacebook(this);
	}
	
	public void callIn(View v) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:5106425259" ));
		startActivity(intent);
	}
	public void openTwitter(View v) {
		try
		{
		    // Check if the Twitter app is installed on the phone.
		    getPackageManager().getPackageInfo("com.twitter.android", 0);

		    Intent intent = new Intent(Intent.ACTION_VIEW);
		    intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
		    // Don't forget to put the "L" at the end of the id.
		    intent.putExtra("user_id", 38070896L);
		    startActivity(intent);
		}
		catch (NameNotFoundException e)
		{
		    // If Twitter app is not installed, start browser.
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/KALXradio")));
		}
	}

}
