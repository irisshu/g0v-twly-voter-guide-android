package g0v.ly.android.bill;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import g0v.ly.lylog.R;
import g0v.ly.android.data.list.DataLists;
import g0v.ly.android.rest.RESTMethods;

public class FragmentBillList extends Fragment implements RESTMethods.RestApiCallback {
	DataLists dataLists = new DataLists();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bill_list, container, false);
		AdapterBillList adapterBillList = new AdapterBillList(getActivity(), dataLists.getBillListGroupData(), dataLists.getBillListItemData());

		assert view != null;
		ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
		expandableListView.setAdapter(adapterBillList);

		return view;
	}

	@Override
	public void getDone(String response, long spendTime, int page) {

	}
}
