package g0v.ly.android.navigate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g0v.ly.android.R;


public class FragmentTest extends Fragment { // implements FragmentViewPager
// .ViewPagerInnerFragment {

    private static final Logger logger = LoggerFactory.getLogger(FragmentTest.class);

    private SynchronizedScrollView scrollView;
    private int index;
    private int scrollTo;

    private int y = 0;

    private SynchronizedScrollView.ScrollViewListener scrollViewListener;

    public FragmentTest(int index, int scrollTo, SynchronizedScrollView.ScrollViewListener scrollViewListener) {
        this.index = index;
        this.scrollTo = scrollTo;
        if (scrollViewListener == null) {
            logger.error("scrollViewListener == null");
        }
        else {
            this.scrollViewListener = scrollViewListener;
            scrollView.setScrollViewListener(scrollViewListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        TextView title1 = (TextView) rootView.findViewById(R.id.title1);
        TextView title2 = (TextView) rootView.findViewById(R.id.title2);
        TextView title3 = (TextView) rootView.findViewById(R.id.title3);
        TextView title4 = (TextView) rootView.findViewById(R.id.title4);

        title1.setText("Title 1\n");
        title1.append("I'm num " + index + " fragment");
        title2.setText("Title 2\n");
        title2.append("I'm num " + index + " fragment");
        title3.setText("Title 3\n");
        title3.append("I'm num " + index + " fragment");
        title4.setText("Title 4\n");
        title4.append("I'm num " + index + " fragment");

        scrollView = (SynchronizedScrollView) rootView.findViewById(R.id.scroll_view);
        setupOnClickListener();

        if (scrollTo != 0) {
            logger.error("scrollTo != 0");
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollTo);
                }
            });
        }
        else {
            logger.error("scrollTo == 0");
        }

        return rootView;
    }

    private void setupOnClickListener() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //y = getCurrentScrollY();
                        //logger.error("ACTION_UP y {}", y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        y = getCurrentScrollY();
                        logger.error("ACTION_MOVE y {}", y);
                        scrollViewListener.onScrollChanged(scrollView, 0, y, 0, 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private int getCurrentScrollY() {
        if (scrollView != null) {
            int y = scrollView.getScrollY();
            logger.error("y = {}", y);
            return y;
        }
        else {
            return 0;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setY(int y) {
        scrollView.setY(y);
    }
}
