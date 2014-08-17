package g0v.ly.android.navigate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SynchronizedScrollView extends ScrollView {

    public interface ScrollViewListener {
        void onScrollChanged(SynchronizedScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public SynchronizedScrollView(Context context) {
        super(context);
    }
/*
    public SynchronizedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SynchronizedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
*/
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
