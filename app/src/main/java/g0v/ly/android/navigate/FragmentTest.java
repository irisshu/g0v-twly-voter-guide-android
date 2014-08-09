package g0v.ly.android.navigate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import g0v.ly.android.R;


public class FragmentTest extends Fragment { // implements FragmentViewPager
// .ViewPagerInnerFragment {
    private ScrollView scrollView;

    private int index;
    private int scrollTo;

    public FragmentTest(int index, int scrollTo) {
        this.index = index;
        this.scrollTo = scrollTo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        TextView title1 = (TextView) view.findViewById(R.id.title1);
        TextView title2 = (TextView) view.findViewById(R.id.title2);
        TextView title3 = (TextView) view.findViewById(R.id.title3);
        TextView title4 = (TextView) view.findViewById(R.id.title4);

        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);

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

    public int getCurrentScrollY() {
        return scrollView.getScrollY();
    }
}
