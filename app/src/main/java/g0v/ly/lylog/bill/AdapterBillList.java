package g0v.ly.lylog.bill;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

public class AdapterBillList implements ExpandableListAdapter {
	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public int getChildrenCount(int i) {
		return 0;
	}

	@Override
	public Object getGroup(int i) {
		return null;
	}

	@Override
	public Object getChild(int i, int i2) {
		return null;
	}

	@Override
	public long getGroupId(int i) {
		return 0;
	}

	@Override
	public long getChildId(int i, int i2) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
		return null;
	}

	@Override
	public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
		return null;
	}

	@Override
	public boolean isChildSelectable(int i, int i2) {
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void onGroupExpanded(int i) {

	}

	@Override
	public void onGroupCollapsed(int i) {

	}

	@Override
	public long getCombinedChildId(long l, long l2) {
		return 0;
	}

	@Override
	public long getCombinedGroupId(long l) {
		return 0;
	}
}
