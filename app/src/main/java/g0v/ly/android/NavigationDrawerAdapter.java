package g0v.ly.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavigationDrawerAdapter extends BaseAdapter {
    //private static final Logger logger = LoggerFactory.getLogger(NavigationDrawerAdapter.class);

    private LayoutInflater inflater;
    private String[] drawerList;

    public NavigationDrawerAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
        drawerList = activity.getResources().getStringArray(R.array.navigation_drawer_list);
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        RowHolder rowHolder;

        if (row == null) {
            rowHolder = new RowHolder(position);

            // Apply different xml
            // XXX
            if (position == 0 || position == 4) {
                row = inflater.inflate(R.layout.row_navigation_title, viewGroup, false);
                assert row != null;
                rowHolder.textViewMain = (TextView) row.findViewById(R.id.tv_title);

                // set title clickable = false
                assert rowHolder.textViewMain != null;
                rowHolder.textViewMain.setEnabled(false);
                rowHolder.textViewMain.setOnClickListener(null);
                row.setClickable(true);
            } else {
                row = inflater.inflate(R.layout.row_navigation_normal, viewGroup, false);
                assert row != null;
                rowHolder.textViewMain = (TextView) row.findViewById(R.id.tv_normal);
                row.setClickable(false);
            }
            row.setTag(rowHolder);
        } else {
            rowHolder = (RowHolder) row.getTag();
        }

        rowHolder.textViewMain.setText(drawerList[position]);
        return row;
    }

    private class RowHolder {
        int position;
        TextView textViewMain;

        RowHolder(int position) {
            this.position = position;
        }

        // Handle row click event in NavigationDrawerFragment
    }
}
