package g0v.ly.lylog.legislator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import g0v.ly.lylog.R;
import g0v.ly.lylog.rest.RESTFunctionManager;
import g0v.ly.lylog.rest.RestApiCallback;
import g0v.ly.lylog.utility.FontManager;
import g0v.ly.lylog.utility.androidcharts.SpiderWebChart;
import g0v.ly.lylog.utility.androidcharts.TitleValueEntity;

public class Profile extends Fragment implements RestApiCallback {

	private long				totalSpendTime			 = 0;
	private TextView 			tvResponse;
	private TextView			tvProfile;
	private Spinner				legislatorNameSpinner;
	private RESTFunctionManager restFunctionManager;
	private String[]			legislatorNameArray;
	private String[]			legislatorProfileArray;
	private boolean				hasNextPage 			= true;

	private Typeface			robotoLight;
	private Typeface			droidSansFallback;

	private SpiderWebChart		spiderWebChart;

	// Key => legislator's name, Value => legislator's profile
	private Map<String, String[]> legislatorListWithProfile = new HashMap<String, String[]>();

	public enum TvUpdateType {
		APPEND,
		OVERWRITE
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		assert view != null;

		tvResponse 				= (TextView) view.findViewById(R.id.tv_response);
		tvProfile				= (TextView) view.findViewById(R.id.tv_profile);
		legislatorNameSpinner 	= (Spinner) view.findViewById(R.id.spinner_legislator_name);

		spiderWebChart 			= (SpiderWebChart) view.findViewById(R.id.testChart);
		initSpiderWebChart();

		/* TODO ad selectable */
		restFunctionManager = new RESTFunctionManager();
		//https://twly.herokuapp.com/api/legislator_terms/?page=2&ad=8
		//restFunctionManager.restGet("https://twly.herokuapp.com/api/legislator/.json", Profile.this);
		String getUrl = "https://twly.herokuapp.com/api/legislator_terms/?page=1&ad=8";
		restFunctionManager.restGet(getUrl, Profile.this);
		setupOnclickListeners();

		// set fonts
		FontManager fontManager = FontManager.getInstance();
		fontManager.setContext(getActivity());
		robotoLight = fontManager.getRobotoLight();
		droidSansFallback = fontManager.getDroidSansFallback();
		tvResponse.setTypeface(robotoLight);
		tvProfile.setTypeface(droidSansFallback);

		return view;
	}

	@Override
	public void getDone(final String response, final long spendTime, int page) {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
			try {
				JSONObject 	apiResponse = new JSONObject(response);
				if (apiResponse.getString("next").equals("null")) {
					hasNextPage = false;
				}

				JSONArray 	results 	= apiResponse.getJSONArray("results");
				legislatorNameArray 	= new String[results.length()];

				for (int i = 0 ; i < results.length() ; i++) {
					// get legislator's name
					JSONObject legislator 	= results.getJSONObject(i);
					legislatorNameArray[i] 	= legislator.getString("name");
					legislatorProfileArray	= new String[6];

					// get legislator's profile
					for (int j = 0 ; j < 6 ; j++) {
						switch (j) {
							case 0:
								legislatorProfileArray[j] = legislator.getString("ad");
								break;
							case 1:
								legislatorProfileArray[j] = legislator.getString("gender");
								break;
							case 2:
								legislatorProfileArray[j] = legislator.getString("party");
								break;
							case 3:
								legislatorProfileArray[j] = legislator.getString("county");
								break;
							case 4:
								legislatorProfileArray[j] = legislator.getString("education");
								break;
							case 5:
								legislatorProfileArray[j] = legislator.getString("experience");
								break;
						}
					}
					legislatorListWithProfile.put(legislatorNameArray[i], legislatorProfileArray);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}
		});

		totalSpendTime += spendTime;
		int legislatorCount = legislatorListWithProfile.keySet().size();
		Log.d("[Profile]getDone", "legislatorCount= " + legislatorCount);
		updateTextView(tvResponse, "Legislator count = " + legislatorCount, TvUpdateType.OVERWRITE);
		updateTextView(tvResponse, "Spend " + totalSpendTime/1000 + "." + totalSpendTime%1000 + "s", TvUpdateType.APPEND);

		Object[] NameObjArray = legislatorListWithProfile.keySet().toArray();
		legislatorNameArray = new String[legislatorListWithProfile.size()]; // XXX, new 12 times
		int nameArraySize = NameObjArray.length;
		for (int i = 0 ; i < nameArraySize ; i++) {
			legislatorNameArray[i] = NameObjArray[i].toString();
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, legislatorNameArray);
		legislatorNameSpinner.setAdapter(arrayAdapter);

		// get rest profiles
		if (hasNextPage) {
			restFunctionManager.restGet("https://twly.herokuapp.com/api/legislator_terms/?page=" + (page+1) + "&ad=8", Profile.this);
		}
		else {
			Log.d("[Profile]getDone", "hasNextPage= " + false); // hasNextPage = false
		}
	}

	private void setupOnclickListeners() {

		legislatorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				Toast.makeText(getActivity(), "你選的是 " + legislatorNameArray[position], Toast.LENGTH_SHORT).show();
				if (legislatorListWithProfile.containsKey(legislatorNameArray[position])) {
					updateTextView(tvProfile, legislatorNameArray[position] + "\n"
							+ "屆期：" + legislatorListWithProfile.get(legislatorNameArray[position])[0] + "\n"
							+ "性別：" + legislatorListWithProfile.get(legislatorNameArray[position])[1] + "\n"
							+ "黨籍：" + legislatorListWithProfile.get(legislatorNameArray[position])[2] + "\n"
							+ "縣市：" + legislatorListWithProfile.get(legislatorNameArray[position])[3] + "\n"
							+ "學歷：" + legislatorListWithProfile.get(legislatorNameArray[position])[4] + "\n"
							+ "經歷：" + legislatorListWithProfile.get(legislatorNameArray[position])[5], TvUpdateType.OVERWRITE);
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
				if (tempMsg != null){
					tv.append(tempMsg);
				}
				break;
		}
	}

	// =============================================================================================

	private void initSpiderWebChart() {
		List<TitleValueEntity> data1 = new ArrayList<TitleValueEntity>();
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title1), 3));
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title2), 4));
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title3), 9));
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title4), 8));
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 10));
		data1.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 2));

		List<TitleValueEntity> data2 = new ArrayList<TitleValueEntity>();
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 3));
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 4));
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 5));
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 6));
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 7));
		data2.add(new TitleValueEntity(getResources().getString(
				R.string.spiderwebchart_title5), 1));


		List<List<TitleValueEntity>> data = new ArrayList<List<TitleValueEntity>>();
		data.add(data1);
		data.add(data2);

		spiderWebChart.setData(data);
		spiderWebChart.setLatitudeNum(4);//TODO method useless, check lib
	}
}
