package g0v.ly.android.legislator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.net.URISyntaxException;

import static android.content.Intent.parseUri;

/**
 * Created by iris on 2014/9/11.
 */
public class ProfileActivity extends Activity{

    FragmentProfile fragmentProfile = new FragmentProfile();
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //resources = getResources();

        //接收 intent

        Intent it =getIntent();
        int iData_pos = it.getIntExtra("DATA_POS",0);
        Toast.makeText(this, "Movies Clicked "+iData_pos, Toast.LENGTH_SHORT).show();



    }



}
