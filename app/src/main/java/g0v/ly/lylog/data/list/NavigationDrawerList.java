package g0v.ly.lylog.data.list;

public class NavigationDrawerList {
	private String[] 	drawerList;

	public NavigationDrawerList() {
		drawerList = new String[] {
			"委員",
				"基本資料",
				"出席紀錄",
				"政治獻金",
			"議案",
				"基本資料",
				"表決紀錄",
		};
	}

	public String[] getDrawerList() {
		return drawerList;
	}
}
