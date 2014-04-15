package g0v.ly.lylog.legislator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import g0v.ly.lylog.R;
import g0v.ly.lylog.rest.RESTFunctionManager;
import g0v.ly.lylog.rest.RestApiCallback;

public class Profile extends Fragment implements RestApiCallback {

	TextView tvResponse;

	public Profile() {
    }

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
	public void getDone(final String response) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvResponse.setText(response);
			}
		});
	}
}
