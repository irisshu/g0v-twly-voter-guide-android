package g0v.ly.lylog.legislator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g0v.ly.lylog.R;
import g0v.ly.lylog.rest.RESTFunctionManager;
import g0v.ly.lylog.rest.RestApiCallback;

public class Profile extends Fragment implements RestApiCallback {

	TextView 	tvResponse;
	String[]	legislatorNameStringArray;
	Spinner		legislatorNameSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		assert view != null;

		Button 	btnGet 			= (Button) view.findViewById(R.id.btn_get);
		tvResponse 				= (TextView) view.findViewById(R.id.tv_response);
		legislatorNameSpinner 	= (Spinner) view.findViewById(R.id.spinner_legislator_name);

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
					legislatorNameStringArray = new String[results.length()];

					for (int i = 0 ; i < results.length() ; i++) {
						JSONObject object = results.getJSONObject(i);
						legislatorNameStringArray[i] = object.getString("name");
						updateTextView(object.getString("name"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateTextView("Spend " + spendTime/1000 + "." + spendTime%1000 + "s");
			}
		});
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, legislatorNameStringArray);
		legislatorNameSpinner.setAdapter(arrayAdapter);
		legislatorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				Toast.makeText(getActivity(), "你選的是" + legislatorNameStringArray[position], Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

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
