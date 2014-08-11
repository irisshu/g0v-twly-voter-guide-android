package g0v.ly.android.navigate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g0v.ly.android.R;


public class FragmentTest extends Fragment { // implements FragmentViewPager
// .ViewPagerInnerFragment {

    private static final Logger logger = LoggerFactory.getLogger(FragmentTest.class);

    private ScrollView scrollView;
    private int index;
    private int scrollTo;

    private int y = 0;

    public FragmentTest(int index, int scrollTo) {
        this.index = index;
        this.scrollTo = scrollTo;
    }
/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser) {
            logger.error("visible hint[{}, {}]", index, getCurrentScrollY());

        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        TextView title1 = (TextView) view.findViewById(R.id.title1);
        TextView title2 = (TextView) view.findViewById(R.id.title2);
        TextView title3 = (TextView) view.findViewById(R.id.title3);
        TextView title4 = (TextView) view.findViewById(R.id.title4);

        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        y = getCurrentScrollY();
                        logger.error("onTouch y {}", y);
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        logger.error("pointer up");
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        logger.error("hover up");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        logger.error("onCreateView [{}, {}]", index, scrollTo);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollTo);
            }
        });

        title1.setText("Title 1\n");
        title1.append("I'm num " + index + " fragment");

        title2.setText("Title 2\n");
        title2.append("I'm num " + index + " fragment");

        title3.setText("Title 3\n");
        title3.append("I'm num " + index + " fragment");

        title4.setText("Title 4\n");
        title4.append("I'm num " + index + " fragment");

        return view;
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

    public int getY() {
        return y;
    }
/*
    public void setScrollViewY(final int y) {
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, y);
            }
        });
    }
*/
}
