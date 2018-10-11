package clientservice.factory;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;
import java.util.ArrayList;
import clientservice.entities.GroupInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.quitsmoke.william.quitsmokeappclient.R;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<GroupInfo> topLevelList;

    public ExpandableListAdapter(Context context, ArrayList<GroupInfo> topLevelList) {
        this.context = context;
        this.topLevelList = topLevelList;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        ArrayList<String> childList = topLevelList.get(groupPosition).getChildtList();
        return childList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        String detailInfo = getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        TextView childItem = (TextView)view.findViewById(R.id.childItem);
        childItem.setText(detailInfo);

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<String> productList = topLevelList.get(groupPosition).getChildtList();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return topLevelList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return topLevelList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        GroupInfo headerInfo = (GroupInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }

        TextView heading = (TextView)view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
