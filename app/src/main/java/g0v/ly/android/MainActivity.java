package g0v.ly.android;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import com.crashlytics.android.Crashlytics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class MainActivity extends Activity{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        Log.d("listDataChild", String.valueOf(listDataChild.size()));
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }


    private void prepareListData()
    {
        Resources res =getResources();
        String Rr = "R.array."+"header";
        String[] header = res.getStringArray(R.array.header);
        listDataHeader = new ArrayList<String>(Arrays.asList(header));
        listDataChild = new HashMap<String, List<String>>();
        List<String> districtList = new ArrayList<String>();

        XmlResourceParser xrp = res.getXml(R.xml.areas);
        int counter = 0;
        String lastAreaName = "";

        try {
            // 判断是否到了文件的结尾
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                //文件的内容的起始标签开始，注意这里的起始标签是test.xml文件里面<resources>标签下面的第一个标签
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagname = xrp.getName();
                    String CurAreaName;
                    String district;
                    if (tagname.endsWith("area")) {
                        counter++;
                        CurAreaName = xrp.getAttributeValue(0);
                        district = xrp.getAttributeValue(1);
                        Log.d("CurAreaName",CurAreaName);
                        if(CurAreaName.equals(lastAreaName))
                        {
                            Log.d("district",district);
                            districtList.add(district);
                        }
                        else
                        {
                            if(counter!=0)
                            {
                                Log.d("lastAreaName",lastAreaName);
                                Log.d("districtList_size",String.valueOf(districtList.size()));
                                listDataChild.put(lastAreaName, districtList);
                            }
                            districtList.clear();
                            Log.d("district",district);
                            districtList.add(district);
                        }

                    }
                }
                xrp.next();
            }
            //程序细节注意的地方，StringBuilder要条用toString()方法，不要粗心忘记了
        } catch (XmlPullParserException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}