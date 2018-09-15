package com.example.william.quitsmokeappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import clientservice.entities.GroupInfo;
import clientservice.entities.MotivationAgeEntity;
import clientservice.entities.MotivationGenderEntity;
import clientservice.entities.SurveyResultEntity;
import clientservice.factory.ExpandableListAdapter;

public class TopMotivationActivity extends AppCompatActivity {

    private Button btnNext;
    SurveyResultEntity surveyResultEntity;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<>();
    private ArrayList<GroupInfo> topLevelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_motivation);

        // get data from source
        Bundle bundle = getIntent().getExtras();
        surveyResultEntity = bundle.getParcelable("responsResult");
        // get button from UI
        btnNext = (Button)findViewById(R.id.btn_top_motivation_next);

        // motivation logic
        List<String> myDataset = new ArrayList<>();
        for (int i = 0; i < surveyResultEntity.getMotivationAgeEntityList().size(); i++) {
            MotivationAgeEntity entity = surveyResultEntity.getMotivationAgeEntityList().get(i);
            myDataset.add(entity.getBehaviour() + "\t\t(" + entity.getProportion() + "%)");
        }
        // add data for displaying in expandable list view
        loadData(surveyResultEntity.getMotivationAgeEntityList(), surveyResultEntity.getMotivationGenderEntityList());
        simpleExpandableListView = (ExpandableListView)findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new ExpandableListAdapter(TopMotivationActivity.this, topLevelList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        //expandAll();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopMotivationActivity.this, SurveyActivity.class);
                startActivity(intent);
            }
        });
    }

    private int addMotivationNode(String topLevel, String product){
        int groupPosition = 0;
        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(topLevel);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(topLevel);
            subjects.put(topLevel, headerInfo);
            topLevelList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<String> childList = headerInfo.getChildtList();
        //size of the children list
        int listSize = childList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        String detailInfo = product;
        childList.add(detailInfo);
        headerInfo.setChildList(childList);

        //find the group position inside the list
        groupPosition = childList.indexOf(headerInfo);
        return groupPosition;
    }

    //load some initial data into out list
    private void loadData(List<MotivationAgeEntity> topSet, List<MotivationGenderEntity> childSet){
        for (MotivationAgeEntity group : topSet) {
            for (MotivationGenderEntity child: childSet) {
                if (group.getBehaviour().equals(child.getClassification()))
                    addMotivationNode(group.getBehaviour() + "(" + group.getProportion() + "%)",
                            child.getBehaviour() + "(" +child.getProportion() + "%)");
            }
        }
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }
}
