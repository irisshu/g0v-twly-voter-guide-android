package g0v.ly.android.navigate;

import android.graphics.Color;
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
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //scrollView = new SynchronizedScrollView(getActivity());
        logger.error("index {} onCreate", index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        logger.error("index {} onCreateView", index);

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

        // test set page width
        switch (index) {
            case 0:
                rootView.setBackgroundColor(Color.rgb(256, 0, 0));
                break;
            case 1:
                rootView.setBackgroundColor(Color.rgb(0, 256, 0));
                break;
            case 2:
                rootView.setBackgroundColor(Color.rgb(0, 0, 256));
                break;
            case 3:
                rootView.setBackgroundColor(Color.rgb(256, 0, 0));
                break;
            case 4:
                rootView.setBackgroundColor(Color.rgb(0, 256, 0));
                break;
        }

        scrollView = (SynchronizedScrollView) rootView.findViewById(R.id.scroll_view);
        scrollView.setIndex(index);
        setupOnClickListener();

        if (scrollViewListener == null) {
            logger.error("scrollViewListener == null");
        }
        else {
            scrollView.setScrollViewListener(scrollViewListener);
        }
/*
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
*/
        return rootView;
    }

    private void setupOnClickListener() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        y = getCurrentScrollY();
                        scrollViewListener.onScrollChanged(scrollView, index, y);
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
            return scrollView.getScrollY();
        }
        else {
            return 0;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setY(final int y) {
        logger.error("[index, y] = [{}, {}]", index, scrollView.getY());
        //logger.error("[index, y] = [{}, {}]", index, y);
        /*
        if (scrollView != null) {
            logger.error("{} scroll view {}", index, scrollView);
        }
        else {
            logger.error("{} scroll view = null", index);
        }
        */

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, y);
            }
        });

    }
}
