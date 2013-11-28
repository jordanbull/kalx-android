package edu.berkeley.kalxstreaming;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class Facebook {

	public static void openKALXFacebook(Activity activity) {

		try {
			final String urlFb = "fb://page/"+"8422996124";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlFb));
            activity.startActivity(intent);
		} catch (Exception e) {
		    // If Facebook app is not installed, start browser.
		    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/90.7fm")));
		}
	}
}
