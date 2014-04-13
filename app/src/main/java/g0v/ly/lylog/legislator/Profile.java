package g0v.ly.lylog.legislator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import g0v.ly.lylog.R;

public class Profile extends Fragment {

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		assert view != null;

		Toast.makeText(view.getContext(), "這是一個Toast......", Toast.LENGTH_LONG).show();

        return view;
    }
}
