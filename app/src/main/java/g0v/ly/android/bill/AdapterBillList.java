package g0v.ly.android.bill;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import g0v.ly.android.R;

public class AdapterBillList implements ExpandableListAdapter {

    private LayoutInflater inflater;
    private ArrayList<String> groupData;
    private ArrayList<ArrayList<String>> itemData;

    public AdapterBillList(Activity activity, ArrayList<String> groupData,
                           ArrayList<ArrayList<String>> itemData) {
        this.groupData = groupData;
        this.itemData = itemData;

        inflater = LayoutInflater.from(activity);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return itemData.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupData.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return itemData.get(i).get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {

        View row = view;
        GroupViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.bill_expandable_list_group, viewGroup, false);
            assert row != null;

            holder = new GroupViewHolder();
            holder.groupTitle = (TextView) row.findViewById(R.id.group_title);

            row.setTag(holder);
        } else {
            holder = (GroupViewHolder) row.getTag();
        }

        holder.groupTitle.setText("Title " + position);

        return row;

    }

    @Override
    public View getChildView(int position, int i2, boolean b, View view, ViewGroup viewGroup) {

        View row = view;
        ItemViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.bill_expandable_list_item, viewGroup, false);
            assert row != null;

            holder = new ItemViewHolder();
            holder.itemTitle = (TextView) row.findViewById(R.id.item_title);

            row.setTag(holder);
        } else {
            holder = (ItemViewHolder) row.getTag();
        }

        // 子項目
        //holder.itemTitle.setText("Item88888888888888888888000000000 " + position);
        holder.itemTitle.setText(R.string.action_example + position);
        //holder.itemTitle.setImeOptions(R.menu.constituency_menu3);

        return row;

    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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

    // View holder
    class GroupViewHolder {
        TextView groupTitle;
    }

    class ItemViewHolder {
        TextView itemTitle;
    }
}
