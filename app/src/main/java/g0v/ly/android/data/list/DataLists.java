package g0v.ly.android.data.list;

import java.util.ArrayList;

public class DataLists {
	private String[] navigationDrawerList;
	private String[] profileRadarChartDataKeyList;
	private ArrayList<String> billListGroupData;
	private ArrayList<ArrayList<String>> billListItemData;
	private ArrayList<String> billListItemData1;
	private ArrayList<String> billListItemData2;
	private ArrayList<String> billListItemData3;

	public DataLists() {
        // XXX
		navigationDrawerList = new String[] {
				"委員",			// title 1
				"基本資料",
				"出席紀錄",
				"政治獻金",
				"議案",			// title 2
				"基本資料",
				"表決紀錄"
		};
		profileRadarChartDataKeyList = new String[] {
				"法條修正案",
				"附帶提案數",
				"投票缺席率",
				"脫黨投票率",
				"立法院院會缺席",
				"委員會開會缺席"
		};
		billListGroupData = new ArrayList<String>();
		billListItemData = new ArrayList<ArrayList<String>>();
		billListItemData1 = new ArrayList<String>();
		billListItemData2 = new ArrayList<String>();
		billListItemData3 = new ArrayList<String>();
	}

	public String[] getDrawerList() {
		return navigationDrawerList;
	}

	public String[] getProfileRadarChartDataKeyList() {
		return profileRadarChartDataKeyList;
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
