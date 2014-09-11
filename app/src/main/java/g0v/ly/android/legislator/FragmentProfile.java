package g0v.ly.android.legislator;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import g0v.ly.android.MainActivity;
import g0v.ly.android.R;
import g0v.ly.android.rest.RESTMethods;
import g0v.ly.android.utility.androidcharts.SpiderWebChart;
import g0v.ly.android.utility.androidcharts.TitleValueEntity;

import static android.content.Intent.parseUri;

public class FragmentProfile extends Fragment implements RESTMethods.RestApiCallback {
    private static final Logger logger = LoggerFactory.getLogger(FragmentProfile.class);

    // Legislators' profile info title
    private static final int PROFILE_INFO_COUNT = 7;
    private static final int PROFILE_INFO_AD = 0;
    private static final int PROFILE_INFO_GENDER = 1;
    private static final int PROFILE_INFO_PARTY = 2;
    private static final int PROFILE_INFO_COUNTY = 3;
    private static final int PROFILE_INFO_EDUCATION = 4;
    private static final int PROFILE_INFO_EXPERIENCE = 5;
    private static final int PROFILE_INFO_PHOTO = 6;

    // RadarChart number
    private static final int ABSENT_COUNT = 5;
    private static final int NOT_VOTE_COUNT = 0;
    private static final int CONSCIENCE_VOTE_COUNT = 1;
    private static final int PRIMARY_PROPOSER_COUNT = 2;
    private static final int LY_ABSENT_COUNT = 3;
    private static final int COMMITTEE_ABSENT_COUNT = 4;


    private static int iData_pos = 0;

    private TextView tvResponse;
    private TextView tvProfileAd;
    private TextView tvProfileGender;
    private TextView tvProfileParty;
    private TextView tvProfileCounty;
    private TextView tvProfileEducation;
    private TextView tvProfileExperience;
    private ImageView imgProfile;
    private Spinner legislatorNameSpinner;


    List<TitleValueEntity> red_own = new ArrayList<TitleValueEntity>();
    List<TitleValueEntity> blue_ave = new ArrayList<TitleValueEntity>();
    List<List<TitleValueEntity>> data = new ArrayList<List<TitleValueEntity>>();


    private RESTMethods restMethods;

    private long totalSpendTime = 0;
    private String[] legislatorNameArray;
    private String[] legislatorProfileArray;
    private String[] legislatorAbsentArray;
    private boolean hasNextPage = true;

    private SpiderWebChart spiderWebChart;

    private Resources resources;
    String[] webChartTitle;


    // Key => legislator's name, Value => legislator's profile
    private Map<String, String[]> legislatorListWithProfile = new HashMap<String, String[]>();
    private Map<String, String[]> legislatorListWithAbsent = new HashMap<String, String[]>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        assert view != null;

        setupUiComponents(view);

		/* TODO ad selectable */
        restMethods = new RESTMethods();
        String getUrl = "https://twly.herokuapp.com/api/legislator_terms/?page=1&ad=8";
        restMethods.restGet(getUrl, FragmentProfile.this);



        setupOnclickListeners();
        return view;
    }

    @Override
    public void getDone(final String response, final long spendTime, int page) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject apiResponse = new JSONObject(response);
                    if (apiResponse.getString("next").equals("null")) {
                        hasNextPage = false;
                    }

                    JSONArray results = apiResponse.getJSONArray("results");
                    legislatorNameArray = new String[results.length()];
                    String[] legislatorProfileInfoApiKey =
                            resources.getStringArray(R.array.legislator_profile_info_api_key);
                    //Iris
                    String[] legislatorProfileRadarChartApiKey =
                            resources.getStringArray(R.array.legislator_profile_radar_chart_api_key);


                    for (int i = 0; i < results.length(); i++) {
                        // get legislator's name
                        JSONObject legislator = results.getJSONObject(i);
                        legislatorNameArray[i] = legislator.getString("name");
                        legislatorProfileArray = new String[PROFILE_INFO_COUNT];
                        legislatorAbsentArray = new String[ABSENT_COUNT];

                        // get legislator's profile
                        for (int j = 0; j < legislatorProfileArray.length; j++) {
                            switch (j) {
                                case 0:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_AD]);
                                    break;
                                case 1:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_GENDER]);
                                    break;
                                case 2:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_PARTY]);
                                    break;
                                case 3:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_COUNTY]);
                                    break;
                                case 4:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_EDUCATION]);
                                    break;
                                case 5:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_EXPERIENCE]);
                                    break;
                                case 6:
                                    legislatorProfileArray[j] =
                                            legislator.getString(legislatorProfileInfoApiKey[PROFILE_INFO_PHOTO]);
                                    break;
                            }
                        }

                        // get legislator's absent counts
                        for (int j = 0; j < legislatorAbsentArray.length; j++) {
                            switch (j) {
                                case 0:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[NOT_VOTE_COUNT]);
                                    break;
                                case 1:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[CONSCIENCE_VOTE_COUNT]);
                                    break;
                                case 2:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[PRIMARY_PROPOSER_COUNT]);
                                    break;
                                case 3:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[LY_ABSENT_COUNT]);
                                    break;
                                case 4:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[COMMITTEE_ABSENT_COUNT]);
                                    break;

                            }
                        }

                        legislatorListWithProfile.put(legislatorNameArray[i], legislatorProfileArray);
                        legislatorListWithAbsent.put(legislatorNameArray[i], legislatorAbsentArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        totalSpendTime += spendTime;
        int legislatorCount = legislatorListWithProfile.keySet().size();
        logger.debug("[Profile] getDone legislatorCount= " + legislatorCount);
        updateTextView(tvResponse, "Legislator count = " + legislatorCount, TvUpdateType.OVERWRITE);
        updateTextView(tvResponse, "Spend " + totalSpendTime / 1000 + "." + totalSpendTime % 1000 +
                "s", TvUpdateType.APPEND);

        Object[] NameObjArray = legislatorListWithProfile.keySet().toArray();
        legislatorNameArray = new String[legislatorListWithProfile.size()]; // XXX, new 12 times
        int nameArraySize = NameObjArray.length;
        for (int i = 0; i < nameArraySize; i++) {
            legislatorNameArray[i] = NameObjArray[i].toString();
        }

        // 這裡是更改立委的關鍵
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, legislatorNameArray);
        legislatorNameSpinner.setAdapter(arrayAdapter);

        // get rest profiles
        if (hasNextPage) {
            restMethods.restGet(
                    "https://twly.herokuapp.com/api/legislator_terms/?page=" + (page + 1) +
                            "&ad=8", FragmentProfile.this
            );
        } else {
            logger.debug("[Profile] getDone hasNextPage = false");
        }
    }


    public void setupOnclickListeners() {

        final int bundle_msg_id=((MainActivity)getActivity()).get_bundle_msg();

        legislatorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                       long l) {
                Toast.makeText(getActivity(),
                        "你選的是 " + legislatorNameArray[iData_pos] + bundle_msg_id, Toast.LENGTH_SHORT).show();



                if (legislatorListWithProfile.containsKey(legislatorNameArray[position])) {
                    updateTextView(tvProfileAd, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_AD], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileGender, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_GENDER], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileParty, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_PARTY], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileCounty, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_COUNTY], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileEducation, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_EDUCATION], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileExperience, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_EXPERIENCE], TvUpdateType.OVERWRITE);

                    updateSpiderWebChart(legislatorListWithAbsent.get(legislatorNameArray[position])[NOT_VOTE_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[CONSCIENCE_VOTE_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[PRIMARY_PROPOSER_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[LY_ABSENT_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[COMMITTEE_ABSENT_COUNT]);

                    GetImageFromUrl getImageFromUrl = new GetImageFromUrl();
                    getImageFromUrl.execute(legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_PHOTO]);
                } else {
                    logger.warn("[onItemSelected] legislator profile not found");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }




    private void updateTextView(TextView tv, String msg, TvUpdateType updateType) {
        switch (updateType) {
            case OVERWRITE:
                tv.setText(msg);
                break;
            case APPEND:
                CharSequence tempMsg = tv.getText();
                tv.setText(msg + "\n");
                if (tempMsg != null) {
                    tv.append(tempMsg);
                }
                break;
        }
    }

    private void updateSpiderWebChart(String nvc, String cvc, String pbc, String lac, String cac){

        // tvProfileAd.setText("hi " + msg);
        red_own.set(0,new TitleValueEntity(webChartTitle[0], Float.parseFloat(nvc)/10));  //沒投票次數 not_vote_count
        red_own.set(1,new TitleValueEntity(webChartTitle[1], Float.parseFloat(cvc)));  //脫黨投票次數 conscience_vote_count
        red_own.set(2,new TitleValueEntity(webChartTitle[2], Float.parseFloat(pbc)/10));  //主提案法案數 primary_biller_count
        red_own.set(3,new TitleValueEntity(webChartTitle[3], Float.parseFloat(lac)));  //全體院會缺席次數 ly_absent_count
        red_own.set(4,new TitleValueEntity(webChartTitle[4], Float.parseFloat(cac)));  //委員會缺席次數 committee_absent_count

        //data.add(red_own);
        data.set(0,red_own);

        addRadarChartData(data);
        spiderWebChart.setLatitudeNum(5);
        spiderWebChart.refreshDrawableState();
        spiderWebChart.invalidate();
        
         //spiderWebChart = (SpiderWebChart) view.findViewById(R.id.profile_radar_chart);

    }

    // Put data in SpiderWebChart.
    public void initSpiderWebChart() {

        webChartTitle =  resources.getStringArray(R.array.legislator_profile_radar_chart_title);

        // TODO create with class

        red_own.add(new TitleValueEntity(webChartTitle[0], 2));  //沒投票次數 not_vote_count
        red_own.add(new TitleValueEntity(webChartTitle[1], 4));  //脫黨投票次數 conscience_vote_count
        red_own.add(new TitleValueEntity(webChartTitle[2], 1));  //主提案法案數 primary_biller_count
        red_own.add(new TitleValueEntity(webChartTitle[3], 5));  //全體院會缺席次數 ly_absent_count
        red_own.add(new TitleValueEntity(webChartTitle[4], 8));  //委員會缺席次數 committee_absent_count


        blue_ave.add(new TitleValueEntity(webChartTitle[0], 3)); //先固定數字
        blue_ave.add(new TitleValueEntity(webChartTitle[1], 4));
        blue_ave.add(new TitleValueEntity(webChartTitle[2], 5));
        blue_ave.add(new TitleValueEntity(webChartTitle[3], 6));
        blue_ave.add(new TitleValueEntity(webChartTitle[4], 7));


        data.add(red_own);
        data.add(blue_ave);

        addRadarChartData(data);
        spiderWebChart.setLatitudeNum(5);//XXX method useless, check lib
    }




    // =============================================================================================

    //TODO check is data added before init chart
    private void addRadarChartData(List<List<TitleValueEntity>> newData) {
        List<List<TitleValueEntity>> radarChartData = new ArrayList<List<TitleValueEntity>>();
        for (List<TitleValueEntity> aData : newData) {
            radarChartData.add(aData);
        }
        spiderWebChart.setData(radarChartData);
    }

    private void setupUiComponents(View view) {
        TextView tvProfileAdTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_ad_title);
        TextView tvProfileGenderTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_gender_title);
        TextView tvProfilePartyTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_party_title);
        TextView tvProfileCountyTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_county_title);
        TextView tvProfileEducationTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_education_title);
        TextView tvProfileExperienceTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_experience_title);
        tvProfileAd = (TextView) view.findViewById(R.id.legislator_profile_info_ad);
        tvProfileGender = (TextView) view.findViewById(R.id.legislator_profile_info_gender);
        tvProfileParty = (TextView) view.findViewById(R.id.legislator_profile_info_party);
        tvProfileCounty = (TextView) view.findViewById(R.id.legislator_profile_info_county);
        tvProfileEducation = (TextView) view.findViewById(R.id.legislator_profile_info_education);
        tvProfileExperience = (TextView) view.findViewById(R.id.legislator_profile_info_experience);
        tvResponse = (TextView) view.findViewById(R.id.tv_response);
        imgProfile = (ImageView) view.findViewById(R.id.profile_img);
        legislatorNameSpinner = (Spinner) view.findViewById(R.id.spinner_legislator_name);
        spiderWebChart = (SpiderWebChart) view.findViewById(R.id.profile_radar_chart);
        initSpiderWebChart( );
        view.setBackgroundColor(Color.WHITE);

        // setup titles
        String[] legislatorProfileInfo = resources.getStringArray(R.array.legislator_profile_info);
        tvProfileAdTitle.setText(legislatorProfileInfo[PROFILE_INFO_AD]);
        tvProfileGenderTitle.setText(legislatorProfileInfo[PROFILE_INFO_GENDER]);
        tvProfilePartyTitle.setText(legislatorProfileInfo[PROFILE_INFO_PARTY]);
        tvProfileCountyTitle.setText(legislatorProfileInfo[PROFILE_INFO_COUNTY]);
        tvProfileEducationTitle.setText(legislatorProfileInfo[PROFILE_INFO_EDUCATION]);
        tvProfileExperienceTitle.setText(legislatorProfileInfo[PROFILE_INFO_EXPERIENCE]);
    }



    public enum TvUpdateType {
        APPEND,
        OVERWRITE
    }

    private class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imgProfile.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString) throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}
