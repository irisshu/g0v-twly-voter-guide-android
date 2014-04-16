package g0v.ly.lylog.legislator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g0v.ly.lylog.R;
import g0v.ly.lylog.rest.RESTFunctionManager;
import g0v.ly.lylog.rest.RestApiCallback;

public class Profile extends Fragment implements RestApiCallback {

	TextView tvResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		assert view != null;

		Button 	btnGet 	= (Button) view.findViewById(R.id.btn_get);
		tvResponse 		= (TextView) view.findViewById(R.id.tv_response);

		final RESTFunctionManager restFunctionManager = new RESTFunctionManager();
		btnGet.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				restFunctionManager.restGet("https://twly.herokuapp.com/api/legislator/.json", Profile.this);
			}
		});
        return view;
    }

	// XXX
	@Override
	public void getDone(final String response, final long spendTime) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject apiResponse = new JSONObject(response);
					JSONArray results = apiResponse.getJSONArray("results");

					for (int i = 0 ; i < results.length() ; i++) {
						JSONObject object = results.getJSONObject(i);
						updateTextView(object.getString("name"));
					}
					/*
					List<DiaryStructure> diaryList = new ArrayList<DiaryStructure>();
					for (int j = 0; j < responseAry.length(); j++) {
						JSONObject obj = responseAry.getJSONObject(j);
						String time = obj.getString("diary_date_hour");
						String url = obj.getString("download_url");
						DiaryStructure diaryStructure = new DiaryStructure();
						diaryStructure.time = time;
						diaryStructure.url = url;
						diaryList.add(diaryStructure);
					}
					allDevicesDiaryLists.put(satDevicesProfiles.get(i).uid, diaryList);
					*/
				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateTextView("Spend " + spendTime/1000 + "." + spendTime%1000 + "s");
			}
		});
	}

	private void updateTextView(String msg) {
		CharSequence tempMsg = tvResponse.getText();
		tvResponse.setText(msg + "\n");
		if (tempMsg != null){
			tvResponse.append(tempMsg);
		}
	}
}
