package ClientService.Factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.william.quitsmokeappclient.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;;import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import ClientService.Entities.GroupInfo;
import ClientService.Entities.SurveyResultEntity;
import ClientService.QuitSmokeClientUtils;
import ClientService.webservice.QuitSmokerReportWebservice;

public class SurveyResultFactorial  extends AsyncTask<Void, Void, Void>  {

    private Activity SurveyResultActivity;
    private int age;
    private String gender;
    private int smokeNo;
    private SurveyResultEntity surveyResultEntity;
    private double coldTurkeySucc;
    private double reduceSucc;
    private double coldTurkeyFail;
    private double reduceFail;
    private String strColdTurkeySucc;
    private String strReduceSucc;
    private String strColdTurkeyFail;
    private String strReduceyFail;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<GroupInfo> topLevelList = new ArrayList<GroupInfo>();


    public SurveyResultFactorial(Activity surveyResultActivity, int age, String gender, int smokeNo) {
        SurveyResultActivity = surveyResultActivity;
        this.age = age;
        this.gender = gender;
        this.smokeNo = smokeNo;

        strColdTurkeySucc = SurveyResultActivity.getResources().getString(R.string.succ_cold_turkey_behaviour);
        strReduceSucc = SurveyResultActivity.getResources().getString(R.string.succ_reduce_behaviour);
        strColdTurkeyFail = SurveyResultActivity.getResources().getString(R.string.fail_cold_turkey_behaviour);
        strReduceyFail = SurveyResultActivity.getResources().getString(R.string.fail_reduce_behaviour);
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "Survey result factory start.");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            surveyResultEntity = QuitSmokerReportWebservice.getSurveyResult(age, gender, smokeNo);

            // successfully done, go to manipulate UI logic
            for (SurveyResultEntity.ChanceAgeEntity entity : surveyResultEntity.getChanceAgeEntityList()) {
                if (strColdTurkeySucc.equals(entity.getBehaviour()) && entity.getAgeStart() <= age && entity.getAgeEnd() >= age)
                    coldTurkeySucc = entity.getProportion();
                else if(strReduceSucc.equals(entity.getBehaviour()) && entity.getAgeStart() <= age && entity.getAgeEnd() >= age)
                    reduceSucc = entity.getProportion();
                else if(strColdTurkeyFail.equals(entity.getBehaviour()) && entity.getAgeStart() <= age && entity.getAgeEnd() >= age)
                    coldTurkeyFail = entity.getProportion();
                else if(strReduceyFail.equals(entity.getBehaviour()) && entity.getAgeStart() <= age && entity.getAgeEnd() >= age)
                    reduceFail = entity.getProportion();

                // if all necessary values have been initialized, break loop
                if (coldTurkeySucc > 0 && reduceSucc > 0 && coldTurkeyFail > 0 && reduceFail > 0)
                    break;
            }

            h.sendEmptyMessage(0);
        } catch (Exception ex) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(1);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("QuitSmokeDebug", "survey result finish.");
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

    //load some initial data into out list
    private void loadData(List<SurveyResultEntity.MotivationAgeEntity> topSet, List<SurveyResultEntity.MotivationGenderEntity> childSet){
        for (SurveyResultEntity.MotivationAgeEntity group : topSet) {
            for (SurveyResultEntity.MotivationGenderEntity child: childSet) {
                if (group.getBehaviour().equals(child.getClassification()))
                    addMotivationNode(group.getBehaviour() + "(" + group.getProportion() + "%)",
                            child.getBehaviour() + "(" +child.getProportion() + "%)");
            }
        }
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

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // handle UI render logic
                // mean
                TextView tvMeanSmoke = (TextView)SurveyResultActivity.findViewById(R.id.tvMeanSmoke);
                TextView tvChance = (TextView)SurveyResultActivity.findViewById(R.id.tvChanceQuitting);
                TextView tvChanceFail = (TextView)SurveyResultActivity.findViewById(R.id.tvChanceQuittingFail);
                if (smokeNo >= surveyResultEntity.getMyMeanGroupEntity().getMeanConsume()) {
                    tvMeanSmoke.setText(String.format(SurveyResultActivity.getResources().getString(R.string.mean_higher_than_avg), smokeNo - surveyResultEntity.getMyMeanGroupEntity().getMeanConsume()));
                } else {
                    tvMeanSmoke.setText(String.format(SurveyResultActivity.getResources().getString(R.string.mean_lower_than_avg), surveyResultEntity.getMyMeanGroupEntity().getMeanConsume() - smokeNo));
                }

                // success quit chance
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                String succRoundColdTurkey = df.format(reduceSucc/coldTurkeySucc);
                String textChanceAgeSucc = String.format(SurveyResultActivity.getResources().getString(R.string.succ_chance), succRoundColdTurkey, df.format(reduceSucc), df.format(coldTurkeySucc));
                tvChance.setText(textChanceAgeSucc);

                // fail quit chance
                String failRoundColdTurkey = df.format(coldTurkeyFail/reduceFail);
                String textChanceAgeFail = String.format(SurveyResultActivity.getResources().getString(R.string.fail_chance), failRoundColdTurkey, df.format(coldTurkeyFail), df.format(reduceFail));
                tvChanceFail.setText(textChanceAgeFail);

                // graph
                GraphView graph = (GraphView) SurveyResultActivity.findViewById(R.id.graph);

                // construct line chart data source
                List<SurveyResultEntity.ChanceAgeEntity> reduceSuccList = new ArrayList<>();
                List<SurveyResultEntity.ChanceAgeEntity> coldTurkeySuccList = new ArrayList<>();
                List<SurveyResultEntity.ChanceAgeEntity> reduceFailList = new ArrayList<>();
                List<SurveyResultEntity.ChanceAgeEntity> coldTurkeyFailList = new ArrayList<>();
                for (SurveyResultEntity.ChanceAgeEntity entity : surveyResultEntity.getChanceAgeEntityList()) {
                    if (entity.getBehaviour().equals(strReduceSucc))
                        reduceSuccList.add(entity);
                    else if (entity.getBehaviour().equals(strColdTurkeySucc))
                        coldTurkeySuccList.add(entity);
                    else if (entity.getBehaviour().equals(strReduceyFail))
                        reduceFailList.add(entity);
                    else if (entity.getBehaviour().equals(strColdTurkeyFail))
                        coldTurkeyFailList.add(entity);
                }
                Collections.sort(reduceSuccList);
                Collections.sort(coldTurkeySuccList);
                Collections.sort(reduceFailList);
                Collections.sort(coldTurkeyFailList);

                // convert chance age reduce amount success to DataPoint array
                DataPoint[] reduceAmountSuccPointArray = new DataPoint[reduceSuccList.size()];
                for (int i = 0; i < reduceSuccList.size(); i++) {
                    SurveyResultEntity.ChanceAgeEntity entity = reduceSuccList.get(i);
                    reduceAmountSuccPointArray[i] = new DataPoint(entity.getAgeStart() + 2, entity.getProportion());
                }
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(reduceAmountSuccPointArray);
                series2.setTitle("Success by reduce amount");
                series2.setColor(Color.YELLOW);
                series2.setDrawDataPoints(true);
                series2.setDataPointsRadius(10);
                series2.setThickness(8);
                // convert chance age cold turkey success to DataPoint array
                DataPoint[] coldTurkeySuccPointArray = new DataPoint[coldTurkeySuccList.size()];
                for (int i = 0; i < coldTurkeySuccList.size(); i++) {
                    SurveyResultEntity.ChanceAgeEntity entity = coldTurkeySuccList.get(i);
                    coldTurkeySuccPointArray[i] = new DataPoint(entity.getAgeStart() + 2, entity.getProportion());
                }
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(coldTurkeySuccPointArray);
                series3.setTitle("Success by cold turkey");
                series3.setColor(Color.GREEN);
                series3.setDrawDataPoints(true);
                series3.setDataPointsRadius(10);
                series3.setThickness(8);
                // convert chance age cold turkey fail to DataPoint array
                DataPoint[] coldTurkeyFailPointArray = new DataPoint[coldTurkeyFailList.size()];
                for (int i = 0; i < coldTurkeyFailList.size(); i++) {
                    SurveyResultEntity.ChanceAgeEntity entity = coldTurkeyFailList.get(i);
                    coldTurkeyFailPointArray[i] = new DataPoint(entity.getAgeStart() + 2, entity.getProportion());
                }
                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(coldTurkeyFailPointArray);
                series4.setTitle("Fail by cold Turkey");
                series4.setColor(Color.RED);
                series4.setDrawDataPoints(true);
                series4.setDataPointsRadius(10);
                series4.setThickness(8);
                // convert chance age reduce amount fail to DataPoint array
                DataPoint[] reduceAmountFailPointArray = new DataPoint[reduceFailList.size()];
                for (int i = 0; i < reduceFailList.size(); i++) {
                    SurveyResultEntity.ChanceAgeEntity entity = reduceFailList.get(i);
                    reduceAmountFailPointArray[i] = new DataPoint(entity.getAgeStart() + 2, entity.getProportion());
                }
                LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(reduceAmountFailPointArray);
                series5.setColor(Color.BLACK);
                series5.setDrawDataPoints(true);
                series5.setDataPointsRadius(10);
                series5.setThickness(8);
                series5.setTitle("Fail by reduce amount");

                series2.setOnDataPointTapListener(new OnDataPointTapListener() {
                     @Override
                     public void onTap(Series series, DataPointInterface dataPoint) {
                         Toast.makeText(SurveyResultActivity, "Proportion of succeed quit by reducing amount at age of " + (int)dataPoint.getX() + " is " + dataPoint.getY() + "%", Toast.LENGTH_LONG).show();
                     }
                 });

                series3.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(SurveyResultActivity, "Proportion of succeed quit by cold turkey at age of " + dataPoint.getX() + " is " + dataPoint.getY() + "%", Toast.LENGTH_LONG).show();
                    }
                });

                series4.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(SurveyResultActivity, "Proportion of fail quit by cold turkey at age of " + dataPoint.getX() + " is " + dataPoint.getY() + "%", Toast.LENGTH_LONG).show();
                    }
                });

                series5.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(SurveyResultActivity, "Proportion of fail quit by reducing amount at age of " + dataPoint.getX() + " is " + dataPoint.getY() + "%", Toast.LENGTH_LONG).show();
                    }
                });
                // set graph parameters
                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.DKGRAY);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Age");
                graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.DKGRAY);
                graph.getGridLabelRenderer().setVerticalAxisTitle("Proportion (%)");
                graph.setTitle("Chance of Quitting");
                graph.setTitleColor(Color.BLACK);
                graph.addSeries(series2);
                graph.addSeries(series3);
                graph.addSeries(series4);
                graph.addSeries(series5);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(100);
                graph.getViewport().setMinX(20);
                graph.getViewport().setMaxX(80);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getLegendRenderer().setVisible(true);
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);
                graph.getLegendRenderer().setBackgroundColor(Color.TRANSPARENT);
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                // motivation logic
                List<String> myDataset = new ArrayList<>();
                for (int i = 0; i < surveyResultEntity.getMotivationAgeEntityList().size(); i++) {
                    SurveyResultEntity.MotivationAgeEntity entity = surveyResultEntity.getMotivationAgeEntityList().get(i);
                    myDataset.add(entity.getBehaviour() + "\t\t(" + entity.getProportion() + "%)");
                }
                // add data for displaying in expandable list view
                loadData(surveyResultEntity.getMotivationAgeEntityList(), surveyResultEntity.getMotivationGenderEntityList());
                simpleExpandableListView = (ExpandableListView)SurveyResultActivity.findViewById(R.id.simpleExpandableListView);
                // create the adapter by passing your ArrayList data
                listAdapter = new ExpandableListAdapter(SurveyResultActivity, topLevelList);
                // attach the adapter to the expandable list view
                simpleExpandableListView.setAdapter(listAdapter);

                //expand all the Groups
                expandAll();

            } else if(msg.what == 1) {
                Toast.makeText(SurveyResultActivity, SurveyResultActivity.getResources().getString(R.string.register_throw_exception), Toast.LENGTH_LONG);
            } else {

            }

        }
    };
}
