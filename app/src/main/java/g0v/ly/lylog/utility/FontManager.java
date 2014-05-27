package g0v.ly.lylog.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class FontManager {

	private static	boolean		isContextGet	= false;
	private static 	FontManager instance 		= null;
	private 		Typeface 	robotoRegular;
	private 		Typeface 	robotoLight;
	private			Typeface	droidSansFallback;

	private FontManager() {
	}

	public static FontManager getInstance() {
		synchronized (FontManager.class) {
			if (instance == null) {
				instance = new FontManager();
			} else if (!isContextGet) {
				//logger.error("Context == null, set FontManager's context before get typefaces.");
				Log.e("FontManager", "!isContextGet");
			}
		}
		return instance;
	}

	public void setContext(Activity activity) {
		if (activity != null){
			isContextGet = true;
			this.robotoRegular 		= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
			this.robotoLight 		= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
			this.droidSansFallback 	= Typeface.createFromAsset(activity.getAssets(), "DroidSansFallback.ttf");
		}
		else {
			Log.e("FontManager", "activity == null");
		}
	}

	public Typeface getRobotoRegular() {
		return robotoRegular;
	}

	public Typeface getRobotoLight() {
		if (robotoLight != null) {
			Log.e("FontManager", "roboto light not null");
		}
		else {
			Log.e("FontManager", "roboto light = null");
		}
		return robotoLight;
	}

	public Typeface getDroidSansFallback() {
		if (droidSansFallback != null) {
			Log.e("FontManager", "droidSansFallback not null");
		}
		else {
			Log.e("FontManager", "droidSansFallback = null");
		}
		return  droidSansFallback;
	}
}
