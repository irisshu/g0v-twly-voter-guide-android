package g0v.ly.android.bill;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;

import g0v.ly.android.R;
import g0v.ly.android.rest.RESTMethods;

public class FragmentBillList extends Fragment implements RESTMethods.RestApiCallback {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bill_list, container, false);
        Resources resources = getResources();

        ArrayList<String> billTitles = new ArrayList<String>(Arrays.asList(resources.getStringArray(R.array.bill_titles)));
        ArrayList<ArrayList<String>> billSubtitles = new ArrayList<ArrayList<String>>();
        billSubtitles.add(0, new ArrayList<String>(Arrays.asList(resources.getStringArray(R.array.bill_subtitles_1))));
        billSubtitles.add(1, new ArrayList<String>(Arrays.asList(resources.getStringArray(R.array.bill_subtitles_2))));
        billSubtitles.add(2, new ArrayList<String>(Arrays.asList(resources.getStringArray(R.array.bill_subtitles_3))));

        AdapterBillList adapterBillList = new AdapterBillList(getActivity(), billTitles, billSubtitles);

		assert view != null;
		ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
		expandableListView.setAdapter(adapterBillList);

		return view;
	}

	@Override
	public void getDone(String response, long spendTime, int page) {

	}
}
