package ClientService.Entities;

import java.util.ArrayList;

public class GroupInfo {

    private String name;
    private ArrayList<String> list = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getChildtList() {
        return list;
    }

    public void setChildList(ArrayList<String> childList) {
        this.list = childList;
    }

}
