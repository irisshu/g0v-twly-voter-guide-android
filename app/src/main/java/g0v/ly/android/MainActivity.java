package g0v.ly.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
    private final int country_num = 26;
    public int pos = 0;
    static final FragmentProfile fragmentProfile = new FragmentProfile();
    Intent it = new Intent();
    private View mProfile ;



    public int get_bundle_msg()
    {
        return pos;
    }

    @Override
    public void onBackPressed() {
        if(mProfile.getVisibility()== View.VISIBLE){
            mProfile.setVisibility(View.GONE);
        }
        else{
            super.onBackPressed();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment =
                (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mProfile = findViewById(R.id.profile);


        for ( int i= 0; i< country_num; i++){

            final int finalI = i;


            findViewById(R.id.btn_county1 +i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                    popupMenu.setOnMenuItemClickListener(MainActivity.this);
                    popupMenu.inflate(R.menu.constituency_menu1 + finalI); //每個縣市都會分配到一個menu，有可能是空的

                    // 用i 去給對應的數字，拿到新的api 後再重構，改成都是動態產生ListView
                    if (popupMenu.getMenu().size() == 0) { //表示這個縣市沒有更細的分類
                        // 直接判斷是哪一區，然後進入顯示區域立委資料
                        pos = 3;
                        fragmentManager.beginTransaction().replace(R.id.profile, fragmentProfile).commit();
                        mProfile.setVisibility(View.VISIBLE);
                        // 進入 profile 頁面

                    }
                    else{
                        popupMenu.show();

                    }


                }
            });

        }


    }

    public boolean onMenuItemClick(MenuItem item) {

        final FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();




        switch (item.getItemId()) {

            case R.id.item_con_3_1:
                pos = 6;
                fragmentManager.beginTransaction().replace(R.id.profile, fragmentProfile).commit();
                mProfile.setVisibility(View.VISIBLE);
                // 進入 profile 頁面

                Toast.makeText(this, "item_con_3_1 Clicked", Toast.LENGTH_SHORT).show();
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
            case 6: // 表決紀錄 -> 回到主選單
                fragmentManager.beginTransaction().remove(fragmentProfile).commit();
                Toast.makeText(this, "重新選擇選區", Toast.LENGTH_SHORT).show();

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