package jacksonmarkowski.launcher;

public class App {

    private String appName;
    private int appID;
    private int listPage;
    private int listXLoc;
    private int listYLoc;


    public App(String appName, int appID) {
        this.appName = appName;
        this.appID = appID;

    }

    public void setListLoc(int xLoc, int yLoc, int page) {
        listXLoc = xLoc;
        listYLoc = yLoc;
        listPage = page;
    }

    public String getName() {
        return appName;
    }

    public int getID() {
        return appID;
    }

    public int getListXLoc() {
        return listXLoc;
    }

    public int getListYLoc() {
        return listYLoc;
    }

    public int getListPage() {
        return listPage;
    }
}
