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

public class FragmentViewPager extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(FragmentViewPager.class);

    private static final int NUM_PAGES = 5;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private List<Fragment> testFragments = new ArrayList <Fragment>();
/*
    private SparseArray<ViewPagerInnerFragment> innerFragments =
            new SparseArray<ViewPagerInnerFragment>();
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new TestViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // debug toast
        Toast.makeText(view.getContext(), "FragmentViewPager", Toast.LENGTH_LONG).show();

        return view;
    }


    public interface ViewPagerInnerFragment {
        public void enter();
        //public void leave();
        //public boolean controlEnabled();
    }

    public class TestViewPagerAdapter extends FragmentPagerAdapter {

        public TestViewPagerAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            // TODO: get previous fragment's y position and pass to next fragment.

            return new FragmentTest(i, 300);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            logger.error("destroy {}", position);
        }
    }
}
