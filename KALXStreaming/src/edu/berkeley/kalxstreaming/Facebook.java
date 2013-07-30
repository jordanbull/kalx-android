package edu.berkeley.kalxstreaming;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class Facebook {

	public static void openKALXFacebook(Activity activity) {
		try {
			activity.getPackageManager().getApplicationInfo("com.facebook.android", 0 );
			String facebookScheme = "fb://profile/" + "8422996124";
			Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme)); 
			activity.startActivity(facebookIntent);
		} catch (NameNotFoundException e) {
		    // If Facebook app is not installed, start browser.
		    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/90.7fm")));
		}
	}
}
