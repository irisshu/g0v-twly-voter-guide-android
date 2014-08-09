package g0v.ly.android.navigate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import g0v.ly.android.R;


public class TestFragment extends Fragment implements ViewPagerFragment.ViewPagerInnerFragment {

    private TextView title;
    private int index;

    public TestFragment (int index) {
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        title = (TextView) view.findViewById(R.id.title);

        return view;
    }

    @Override
    public void enter() {
        title.setText("I'm num " + index + " fragment");
    }
}
