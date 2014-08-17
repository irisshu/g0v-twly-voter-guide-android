package g0v.ly.android.navigate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import g0v.ly.android.R;

public class FragmentViewPager extends Fragment implements
        SynchronizedScrollView.ScrollViewListener {

    private static final Logger logger = LoggerFactory.getLogger(FragmentViewPager.class);

    private static final int NUM_PAGES = 5;

    private List<FragmentTest> fragments = new ArrayList<FragmentTest>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create FragmentTest and add to list
        for (int i = 0; i < NUM_PAGES; i++) {
            fragments.add(new FragmentTest(0, 0, this));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new TestViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // debug toast
        Toast.makeText(view.getContext(), "FragmentViewPager", Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onScrollChanged(
            SynchronizedScrollView scrollView, int x, int y, int oldx, int oldy) {
        for (FragmentTest fragmentTest : fragments) {
            logger.error("ViewPager onScrollChange, index = {}", fragmentTest.getIndex());
            //fragmentTest.setY(y);
        }
    }

    public class TestViewPagerAdapter extends FragmentPagerAdapter{

        public TestViewPagerAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            // TODO: get previous fragment's y position and pass to next fragment.
            return fragments.get(i);//fragmentTest;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
