package g0v.ly.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import g0v.ly.android.bill.FragmentBillList;
import g0v.ly.android.legislator.FragmentProfile;
import g0v.ly.android.navigate.FragmentViewPager;
import g0v.ly.android.utility.FontManager;

/**
 * Created by iris on 2014/9/13.
 */
public class FragmentSelectPlace extends FragmentActivity implements PopupMenu.OnMenuItemClickListener{


    private final int country_num = 26;
    public int pos = 0;
    static final FragmentProfile fragmentProfile = new FragmentProfile();

    public int get_bundle_msg()
    {
        return pos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();


        for ( int i= 0; i< country_num; i++){

            final int finalI = i;


            findViewById(R.id.btn_county1 +i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(FragmentSelectPlace.this, view);
                    popupMenu.setOnMenuItemClickListener(FragmentSelectPlace.this);
                    popupMenu.inflate(R.menu.constituency_menu1 + finalI); //每個縣市都會分配到一個menu，有可能是空的

                    if (popupMenu.getMenu().size() == 0) { //表示這個縣市沒有更細的分類
                        // 直接判斷是哪一區，然後進入顯示區域立委資料
                        // 已用中斷點測試過

                        pos = 3;
                        fragmentManager.beginTransaction().replace(R.id.container, fragmentProfile).commit();
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

                //fragmentProfile.setupOnclickListeners(position);
                fragmentManager.beginTransaction().replace(R.id.container, fragmentProfile).commit();
                // 進入 profile 頁面

                pos = 4;
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


}
