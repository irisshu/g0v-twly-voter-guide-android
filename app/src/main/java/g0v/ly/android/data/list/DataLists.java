package g0v.ly.android.data.list;

import java.util.ArrayList;

public class DataLists {
	private ArrayList<String> billListGroupData;
	private ArrayList<ArrayList<String>> billListItemData;
	private ArrayList<String> billListItemData1;
	private ArrayList<String> billListItemData2;
	private ArrayList<String> billListItemData3;

	public DataLists() {
		billListGroupData = new ArrayList<String>();
		billListItemData = new ArrayList<ArrayList<String>>();
		billListItemData1 = new ArrayList<String>();
		billListItemData2 = new ArrayList<String>();
		billListItemData3 = new ArrayList<String>();
	}

	public ArrayList<String> getBillListGroupData() {
		billListGroupData.add("group 1");
		billListGroupData.add("group 2");
		billListGroupData.add("group 3");

		return billListGroupData;
	}

	public ArrayList<ArrayList<String>> getBillListItemData() {
		billListItemData1.add("1-1");
		billListItemData1.add("1-2");
		billListItemData1.add("1-3");

		billListItemData2.add("2-1");
		billListItemData2.add("2-2");
		billListItemData2.add("2-3");

		billListItemData3.add("3-1");
		billListItemData3.add("3-2");
		billListItemData3.add("3-3");

		billListItemData.add(billListItemData1);
		billListItemData.add(billListItemData2);
		billListItemData.add(billListItemData3);
		return billListItemData;
	}
}
