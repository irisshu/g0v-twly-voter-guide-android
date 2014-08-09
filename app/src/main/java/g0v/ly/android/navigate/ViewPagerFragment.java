package g0v.ly.android.navigate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import g0v.ly.android.R;

public class ViewPagerFragment extends Fragment {

    private static final int NUM_PAGES = 5;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private SparseArray<ViewPagerInnerFragment> innerFragments =
            new SparseArray<ViewPagerInnerFragment>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new TestViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

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
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
