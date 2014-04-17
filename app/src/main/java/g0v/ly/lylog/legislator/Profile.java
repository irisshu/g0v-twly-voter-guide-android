package g0v.ly.lylog.legislator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

import g0v.ly.lylog.R;
import g0v.ly.lylog.rest.RESTFunctionManager;
import g0v.ly.lylog.rest.RestApiCallback;

public class Profile extends Fragment implements RestApiCallback {

	TextView 	tvResponse;
	TextView	tvProfile;
	String[]	legislatorNameArray;
	Spinner		legislatorNameSpinner;
	String[]	legislatorProfileArray;

	// Key => legislator's name, Value => legislator's profile
	Map<String, String[]> legislatorListWithProfile = new HashMap<String, String[]>();

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

		RESTFunctionManager restFunctionManager = new RESTFunctionManager();
		restFunctionManager.restGet("https://twly.herokuapp.com/api/legislator/.json", Profile.this);
		setupOnclickListeners();

        return view;
    }

	// [Callback] Received response from REST GET. [start]
	@Override
	public void getDone(final String response, final long spendTime) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject 	apiResponse = new JSONObject(response);
					JSONArray 	results 	= apiResponse.getJSONArray("results");
					legislatorNameArray 	= new String[results.length()];

					for (int i = 0 ; i < results.length() ; i++) {
						// get legislator's name
						JSONObject legislator 	= results.getJSONObject(i);
						legislatorNameArray[i] 	= legislator.getString("name");
						legislatorProfileArray	= new String[4];

						// get legislator's profile
						JSONArray 	eachTerms 		= legislator.getJSONArray("each_terms");
						JSONObject 	eachTermsObj 	= eachTerms.getJSONObject(0);
						for (int j = 0 ; j < 4 ; j++) {
							switch (j) {
								case 0:
									legislatorProfileArray[j] = eachTermsObj.getString("gender");
									break;
								case 1:
									legislatorProfileArray[j] = eachTermsObj.getString("party");
									break;
								case 2:
									legislatorProfileArray[j] = eachTermsObj.getString("county");
									break;
								case 3:
									legislatorProfileArray[j] = eachTermsObj.getString("experience");
									break;
							}
						}
						legislatorListWithProfile.put(legislatorNameArray[i], legislatorProfileArray);
					}
					updateTextView(tvResponse, "Legislator count = " + legislatorListWithProfile.keySet().size(), TvUpdateType.OVERWRITE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateTextView(tvResponse, "Spend " + spendTime/1000 + "." + spendTime%1000 + "s", TvUpdateType.APPEND);
			}
		});
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, legislatorNameArray);
		legislatorNameSpinner.setAdapter(arrayAdapter);
	}// [Callback] Received response from REST GET. [end]

	private void setupOnclickListeners() {

		// Setup spinner's onclick listener. [start]
		legislatorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				Toast.makeText(getActivity(), "你選的是 " + legislatorNameArray[position], Toast.LENGTH_SHORT).show();
				if (legislatorListWithProfile.containsKey(legislatorNameArray[position])) {
					updateTextView(tvProfile, legislatorNameArray[position] + "\n"
							+ "性別：" + legislatorListWithProfile.get(legislatorNameArray[position])[0] + "\n"
							+ "黨籍：" + legislatorListWithProfile.get(legislatorNameArray[position])[1] + "\n"
							+ "縣市：" + legislatorListWithProfile.get(legislatorNameArray[position])[2] + "\n"
							+ "經歷：" + legislatorListWithProfile.get(legislatorNameArray[position])[3], TvUpdateType.OVERWRITE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});// Setup spinner's onclick listener. [end]
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
}
