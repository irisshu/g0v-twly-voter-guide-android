package g0v.ly.android.legislator;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import g0v.ly.android.utility.androidcharts.PieChart;
import g0v.ly.android.utility.androidcharts.SpiderWebChart;
import g0v.ly.android.utility.androidcharts.TitleValueColorEntity;
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
    private static final int NOT_VOTE_PERCENTAGE = 0;
    private static final int CONSCIENCE_VOTE_PERCENTAGE = 1;
    private static final int PRIMARY_PROPOSER_COUNT = 2;
    private static final int LY_ABSENT_COUNT = 3;
    private static final int COMMITTEE_ABSENT_COUNT = 4;

    // PieChart number
    private static final int PIE_CHART_IN = 6;
    private static final int IN_INDIVIDUAL = 0;
    private static final int IN_PROFIT = 1;
    private static final int IN_PARTY = 2;
    private static final int IN_CIVIL = 3;
    private static final int IN_ANONYMOUS = 4;
    private static final int IN_OTHERS = 5;

    private static int stopFlag = 0;

    private TextView tvResponse;
    private TextView tvProfileName;
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

    List<TitleValueColorEntity> piechart_data_in = new ArrayList<TitleValueColorEntity>();
    List<TitleValueColorEntity> piechart_data_out = new ArrayList<TitleValueColorEntity>();

    private RESTMethods restMethods;

    private long totalSpendTime = 0;
    private String[] legislatorNameArray;
    private String[] legislatorProfileArray;
    private String[] legislatorAbsentArray;
    private String[] legislatorPoliJSONArray;
    private String[] legislatorPoliticalContributionsArray; //政治獻金
    private boolean hasNextPage = true;

    private SpiderWebChart spiderWebChart;
    private PieChart pieChartIn;
    private PieChart pieChartOut;


    private Resources resources;
    String[] webChartTitle;
    String[] pieChartTitle;
    int[] pieChartColor;


    // Key => legislator's name, Value => legislator's profile
    private Map<String, String[]> legislatorListWithProfile = new HashMap<String, String[]>();
    private Map<String, String[]> legislatorListWithAbsent = new HashMap<String, String[]>();
    private Map<String, String[]> legislatorListWithPoliticalContributions = new HashMap<String, String[]>();
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
        String getUrl = "http://vote.ly.g0v.tw/api/legislator_terms/?ad=8&county=%E5%8D%97%E6%8A%95%E7%B8%A3";


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
                    legislatorPoliJSONArray = new String[results.length()];

                    String[] legislatorProfileInfoApiKey =
                            resources.getStringArray(R.array.legislator_profile_info_api_key);
                    //Iris
                    String[] legislatorProfileRadarChartApiKey =
                            resources.getStringArray(R.array.legislator_profile_radar_chart_api_key);

                    String[] legislatorProfilePieChartApiKey =
                            resources.getStringArray(R.array.legislator_profile_pie_chart_api_key);


                    for (int i = 0; i < results.length(); i++) {
                        // get legislator's name
                        JSONObject legislator = results.getJSONObject(i);
                        legislatorNameArray[i] = legislator.getString("name");
                        legislatorProfileArray = new String[PROFILE_INFO_COUNT];
                        legislatorAbsentArray = new String[ABSENT_COUNT];

                        //TODO: PIE_CHART_IN 應該改成 PIE_CHART_IN + PIE_CHART_OUT
                        legislatorPoliticalContributionsArray = new String[PIE_CHART_IN];
                        JSONObject poli_in_out = null;

                        JSONArray POLI= legislator.getJSONArray("politicalcontributions");
                        if (POLI.length()== 0){
                            // This legislator does not have any data about political contributions.
                        }
                        else{
                            poli_in_out = POLI.getJSONObject(0);

                        }

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
                                            legislator.getString(legislatorProfileRadarChartApiKey[NOT_VOTE_PERCENTAGE]);
                                    break;
                                case 1:
                                    legislatorAbsentArray[j] =
                                            legislator.getString(legislatorProfileRadarChartApiKey[CONSCIENCE_VOTE_PERCENTAGE]);
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

                        // get legislator's political contributions.
                        for (int j = 0; j < legislatorPoliticalContributionsArray.length; j++) {
                            switch (j) {
                                case 0:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_INDIVIDUAL]) : null;
                                            // poli_in_out 有可能是null (有些人沒有政治獻金資料)
                                    break;
                                case 1:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_PROFIT]) : null;
                                    break;
                                case 2:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_PARTY]) : null;
                                    break;
                                case 3:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_CIVIL]) : null;
                                    break;
                                case 4:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_ANONYMOUS]) : null;
                                    break;
                                case 5:
                                    legislatorPoliticalContributionsArray[j] =
                                            poli_in_out != null ? poli_in_out.getString(legislatorProfilePieChartApiKey[IN_OTHERS]) : null;
                                    break;

                            }
                        }

                        legislatorListWithProfile.put(legislatorNameArray[i], legislatorProfileArray);
                        legislatorListWithAbsent.put(legislatorNameArray[i], legislatorAbsentArray);
                        legislatorListWithPoliticalContributions.put(legislatorNameArray[i], legislatorPoliticalContributionsArray);
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

        // 將立委資料放入 spinner
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, legislatorNameArray);


        // 只抓對應到的那頁
        if(stopFlag == 0){

            restMethods.restGet(
                    "http://vote.ly.g0v.tw/api/legislator_terms/?ad=8&county=%E5%8D%97%E6%8A%95%E7%B8%A3", FragmentProfile.this
            );
            legislatorNameSpinner.setAdapter(arrayAdapter);
            stopFlag = 1;
        }


        //原本全部的立委都抓下來
        // get rest profiles
//        if (hasNextPage) {
//            restMethods.restGet(
//                    "https://twly.herokuapp.com/api/legislator_terms/?page=" + (page + 1) +
//                            "&ad=8", FragmentProfile.this
//            );
//        } else {
//
//            // 全部下載完後，再顯示出來
//            legislatorNameSpinner.setAdapter(arrayAdapter);
//            logger.debug("[Profile] getDone hasNextPage = false");
//        }


    }




    public void setupOnclickListeners() {

        // Get the "pos" number from MainActivity
        final int bundle_msg_id=((MainActivity)getActivity()).pos;

        legislatorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                       long l) {

                position = bundle_msg_id; // 傳 "選區編號" 資料
                position = 1;
                Toast.makeText(getActivity(),
                        "你選的是 " + legislatorNameArray[position] + position, Toast.LENGTH_SHORT).show();

                if (legislatorListWithProfile.containsKey(legislatorNameArray[position])) {
                    updateTextView(tvProfileName,legislatorNameArray[position], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileAd, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_AD], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileGender, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_GENDER], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileParty, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_PARTY], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileCounty, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_COUNTY], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileEducation, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_EDUCATION], TvUpdateType.OVERWRITE);
                    updateTextView(tvProfileExperience, legislatorListWithProfile.get(legislatorNameArray[position])[PROFILE_INFO_EXPERIENCE], TvUpdateType.OVERWRITE);

                    updateSpiderWebChart(legislatorListWithAbsent.get(legislatorNameArray[position])[NOT_VOTE_PERCENTAGE],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[CONSCIENCE_VOTE_PERCENTAGE],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[PRIMARY_PROPOSER_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[LY_ABSENT_COUNT],
                            legislatorListWithAbsent.get(legislatorNameArray[position])[COMMITTEE_ABSENT_COUNT]);

                    updatePieChart(legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_INDIVIDUAL],
                            legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_PROFIT],
                            legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_PARTY],
                            legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_CIVIL],
                            legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_ANONYMOUS],
                            legislatorListWithPoliticalContributions.get(legislatorNameArray[position])[IN_OTHERS]
                    );

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

    // Put data in SpiderWebChart.
    public void initSpiderWebChart() {

        webChartTitle =  resources.getStringArray(R.array.legislator_profile_radar_chart_title);

        // TODO create with class

        red_own.add(new TitleValueEntity(webChartTitle[0], 2));  //投票率 (100-not_vote_percentage)/10，因為雷達圖最大是10單位
        red_own.add(new TitleValueEntity(webChartTitle[1], 4));  //脫黨投票率  conscience_vote_percentage/10
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

    private void updateSpiderWebChart(String nvp, String cvp, String pbc, String lac, String cac){

        //TODO: 去要真實資料的最大值，設範圍

        // 暫時讓指標不要超出圖表範圍
        float Fpbc = Float.parseFloat(pbc);
        if(Fpbc > 10)Fpbc = 10;

        red_own.set(0,new TitleValueEntity(webChartTitle[0], (100-Float.parseFloat(nvp)) /10 ));    //投票率 (100-not_vote_percentage)/10
        red_own.set(1,new TitleValueEntity(webChartTitle[1], Float.parseFloat(cvp)/10 ));           //脫黨投票率 conscience_vote_percentage/10
        red_own.set(2,new TitleValueEntity(webChartTitle[2], Fpbc));                    //主提案法案數 primary_biller_count
        red_own.set(3,new TitleValueEntity(webChartTitle[3], Float.parseFloat(lac)));   //全體院會缺席次數 ly_absent_count
        red_own.set(4,new TitleValueEntity(webChartTitle[4], Float.parseFloat(cac)));   //委員會缺席次數 committee_absent_count

        data.set(0,red_own);

        addRadarChartData(data);
        spiderWebChart.setLatitudeNum(5);
        spiderWebChart.refreshDrawableState();
        spiderWebChart.invalidate(); // 強制更新圖表
    }

    private void initPieChart() {
        pieChartColor = resources.getIntArray(R.array.legislator_profile_pie_chart_color);

        pieChartTitle = resources.getStringArray(R.array.legislator_profile_pie_chart_title_in);
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[0], 2, pieChartColor[0])); //個人捐贈 in_individual
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[1], 3, pieChartColor[1])); //營利事業捐贈 in_profit
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[2], 5, pieChartColor[2])); //政黨捐贈 in_party
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[3], 4, pieChartColor[3])); //人民團體捐贈 in_civil
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[4], 2, pieChartColor[4])); //匿名捐贈 in_anonymous
        piechart_data_in.add(new TitleValueColorEntity(pieChartTitle[5], 2, pieChartColor[5])); //其他 in_others
        pieChartIn.setData(piechart_data_in);

        piechart_data_out = new ArrayList<TitleValueColorEntity>();
        pieChartTitle = resources.getStringArray(R.array.legislator_profile_pie_chart_title_out);
        piechart_data_out.add(new TitleValueColorEntity(pieChartTitle[0], 4, pieChartColor[0]));
        piechart_data_out.add(new TitleValueColorEntity(pieChartTitle[1], 3, pieChartColor[1]));
        piechart_data_out.add(new TitleValueColorEntity(pieChartTitle[2], 4, pieChartColor[2]));
        piechart_data_out.add(new TitleValueColorEntity(pieChartTitle[3], 2, pieChartColor[3]));
        piechart_data_out.add(new TitleValueColorEntity(pieChartTitle[4], 2, pieChartColor[4]));
        pieChartOut.setData(piechart_data_out);
    }

    private void updatePieChart(String in_individual, String in_profit, String in_party, String in_civil, String in_anonymous, String in_others) {
        pieChartColor = resources.getIntArray(R.array.legislator_profile_pie_chart_color);

        pieChartTitle = resources.getStringArray(R.array.legislator_profile_pie_chart_title_in);
        piechart_data_in.set(0,new TitleValueColorEntity(pieChartTitle[0], Float.parseFloat(in_individual),pieChartColor[0])); //個人捐贈 in_individual
        piechart_data_in.set(1,new TitleValueColorEntity(pieChartTitle[1], Float.parseFloat(in_profit),pieChartColor[1])); //營利事業捐贈 in_profit
        piechart_data_in.set(2,new TitleValueColorEntity(pieChartTitle[2], Float.parseFloat(in_civil),pieChartColor[2])); //政黨捐贈 in_party
        piechart_data_in.set(3,new TitleValueColorEntity(pieChartTitle[3], Float.parseFloat(in_party),pieChartColor[3])); //人民團體捐贈 in_civil
        piechart_data_in.set(4,new TitleValueColorEntity(pieChartTitle[4], Float.parseFloat(in_anonymous),pieChartColor[4])); //匿名捐贈 in_anonymous
        piechart_data_in.set(5,new TitleValueColorEntity(pieChartTitle[5], Float.parseFloat(in_others),pieChartColor[5])); //其他 in_others
        pieChartIn.setData(piechart_data_in);
        pieChartIn.invalidate();

        //TODO pieChartOut

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
        TextView tvProfileNameTitle =
                (TextView) view.findViewById(R.id.legislator_profile_info_name_title);
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

        tvProfileName = (TextView) view.findViewById(R.id.legislator_profile_info_name);
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
        pieChartIn = (PieChart) view.findViewById(R.id.profile_pie_chart_in);
        pieChartOut = (PieChart) view.findViewById(R.id.profile_pie_chart_out);

        initSpiderWebChart( );
        initPieChart();
        view.setBackgroundColor(Color.WHITE);

        // setup titles
        String[] legislatorProfileInfo = resources.getStringArray(R.array.legislator_profile_info);
        tvProfileNameTitle.setText("姓名："); //暫時使用
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
