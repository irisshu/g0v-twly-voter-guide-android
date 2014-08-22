package g0v.ly.android.navigate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g0v.ly.android.R;


public class FragmentTest extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(FragmentTest.class);

    private SynchronizedScrollView scrollView;
    private int index = 0;
    private int y = 0;

    private SynchronizedScrollView.ScrollViewListener scrollViewListener;

    public FragmentTest(int index, SynchronizedScrollView.ScrollViewListener scrollViewListener) {
        this.index = index;
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        // XXX hard code colors for 4 way ViewPager demo.
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout);
        switch (index) {
            case 0:
                linearLayout.setBackgroundColor(Color.RED);
                break;
            case 1:
                linearLayout.setBackgroundColor(Color.YELLOW);
                break;
            case 2:
                linearLayout.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                linearLayout.setBackgroundColor(Color.BLUE);
                break;
            case 4:
                linearLayout.setBackgroundColor(Color.BLACK);
                break;
        }

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

    public void setY(int y) {
        scrollView.scrollTo(0, y);
    }
}
