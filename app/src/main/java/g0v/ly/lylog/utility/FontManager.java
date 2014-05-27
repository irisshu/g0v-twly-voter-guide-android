package g0v.ly.lylog.utility;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

	private static	boolean		isContextGet	= false;
	private static 	FontManager instance 		= null;
	private 		Typeface 	robotoRegular;
	private 		Typeface 	robotoLight;

	private FontManager() {
	}

	public static FontManager getInstance() {
		synchronized (FontManager.class) {
			if (instance == null) {
				instance = new FontManager();
			} else if (!isContextGet) {
				//logger.error("Context == null, set FontManager's context before get typefaces.");
			}
		}
		return instance;
	}

	public void setContext(Context context) {
		isContextGet = true;
		this.robotoRegular 	= Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
		this.robotoLight 	= Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
	}

	public Typeface getTypefaceRobotoRegular() {
		return robotoRegular;
	}

	public Typeface getTypefaceRobotoLight() {
		return robotoLight;
	}
}
