package g0v.ly.android;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import g0v.ly.android.bill.FragmentBillList;
import g0v.ly.android.legislator.FragmentProfile;
import g0v.ly.android.navigate.FragmentViewPager;
import g0v.ly.android.utility.FontManager;



@SuppressWarnings("ALL")


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, PopupMenu.OnMenuItemClickListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment =
                (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });
    }

    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_comedy:
                Toast.makeText(this, "Comedy Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_movies:
                Toast.makeText(this, "Movies Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_music:
                Toast.makeText(this, "Music Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
            case 4:
                //Log.d("MainActivity", "Title row clicked");
                break;
            case 1:
                FragmentProfile fragmentProfile = new FragmentProfile();
                fragmentManager.beginTransaction().replace(R.id.container, fragmentProfile).commit();
                break;
            case 2:
                FragmentViewPager fragmentViewPager = new FragmentViewPager();
                fragmentManager.beginTransaction().replace(R.id.container,
                        fragmentViewPager).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(
                        position + 1)).commit();
                break;
            case 5:
                FragmentBillList fragmentBillList = new FragmentBillList();
                fragmentManager.beginTransaction().replace(R.id.container, fragmentBillList).commit();
                break;
            case 6:
                fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(
                        position + 1)).commit();
                break;
            case 7:
                fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(
                        position + 1)).commit();
                break;

        }
    }

    public void onSectionAttached(int number) {
        String[] titleStrArray = getResources().getStringArray(R.array.navigation_drawer_list);
        switch (number) {
            case 1:
            case 5:
                //mTitle = titleStrArray[number-1];
                break;
            case 2:
            case 3:
            case 4:
                //mTitle = getString(R.string.title_section1);
                mTitle = titleStrArray[0] + " : " + titleStrArray[number - 1];
                break;

            case 6:
            case 7:
                //mTitle = getString(R.string.title_section1);
                mTitle = titleStrArray[4] + " : " + titleStrArray[number - 1];
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            assert rootView != null;
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            //ImageView imgLegislation = (ImageView) rootView.findViewById(R.id.legislation_img);

            return rootView;


        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

            // TODO set context not work
            // Prepare font manager
            FontManager fontManager = FontManager.getInstance();
            fontManager.setContext(activity);
        }
    }
}