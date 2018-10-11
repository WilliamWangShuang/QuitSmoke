package clientservice.factory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.quitsmoke.william.quitsmokeappclient.Fragments.MessageDialogFragment;
import com.quitsmoke.william.quitsmokeappclient.Interface.IUpdatePartnerAsyncResponse;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.entities.PlanEntity;
import clientservice.webservice.InteractWebservice;
import com.quitsmoke.william.quitsmokeappclient.R;

public class GetCurrentPlanFactorial extends AsyncTask<Void, Void, String> {
    private Activity smokerMainActivity;
    private FragmentManager fragmentManager;
    private String uid;
    private boolean isPartnerSet;
    private CircleProgressBar mCustomProgressBar;
    private PlanEntity currentPlan;
    private boolean isRequestFromSmokerFragement;
    private TextView tvMilestone;
    private TextView tvMoneySaved;
    public IUpdatePartnerAsyncResponse delegate = null;

    public GetCurrentPlanFactorial(Activity smokerMainActivity, FragmentManager fragmentManager, String uid, CircleProgressBar mCustomProgressBar, TextView tvMilestone, TextView tvMoneySaved, boolean isRequestFromSmokerFragement) {
        this.uid = uid;
        this.isRequestFromSmokerFragement = isRequestFromSmokerFragement;
        this.tvMilestone = tvMilestone;
        this.smokerMainActivity = smokerMainActivity;
        this.fragmentManager= fragmentManager;
        this.mCustomProgressBar = mCustomProgressBar;
        this.tvMoneySaved = tvMoneySaved;
    }

    @Override
    protected void onPreExecute() {
        Log.d("QuitSmokeDebug", "get current proceeding plan starts.");
    }

    @Override
    protected String doInBackground(Void... params) {
        int realAmount = 0;
        try {
            // do server side validation check if the smoker has set a supporter
            isPartnerSet = isRequestFromSmokerFragement ? InteractWebservice.isSupporterSet() : true;
            if (isPartnerSet) {
                // get current plan by smoker's uid
                currentPlan = InteractWebservice.getProceedingPlan(uid);
                Log.d("QuitSmokeDebug", "currentPlan is null:" + (currentPlan == null));
                if (currentPlan != null) {
                    // return current achieved successive day
                    realAmount = isRequestFromSmokerFragement ? currentPlan.getRealAmount() : currentPlan.getSuccessiveDay();
                    h.sendEmptyMessage(0);
                } else {
                    return QuitSmokeClientConstant.INDICATOR_NO_PLAN;
                }
            } else {
                // if current user not set supporter yet. Set result empty
                Log.d("QuitSmokeDebug", "Partner not set..");
                return QuitSmokeClientConstant.INDICATOR_N;
            }
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            h.sendEmptyMessage(2);
        }

        return "" + realAmount;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("QuitSmokeDebug", "current proceeding plan finish.");
        delegate.processFinish(result);
    }

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                String encouragement = currentPlan.getEncouragement();
                QuitSmokeClientUtils.setEncouragement(encouragement);
                mCustomProgressBar.setVisibility(View.VISIBLE);
                // calculate progress for progress bar
                int realAmount = currentPlan.getRealAmount();
                int targetAmount = currentPlan.getTargetAmount();
                final String progressBarClickMsg = String.format(smokerMainActivity.getResources().getString(R.string.progress_bar_click_message), currentPlan.getTargetAmount(), currentPlan.getRealAmount());
                int progress = targetAmount == 0 ? 0 : (int)(realAmount * 100 / targetAmount);
                QuitSmokeClientUtils.simulateProgress(mCustomProgressBar, progress);
                // set onclick of progress bar to show the plan information, e.g.plan target number
                mCustomProgressBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("message", progressBarClickMsg);
                        messageDialogFragment.setArguments(bundle);
                        messageDialogFragment.show(fragmentManager, "showPrgressBarMsg");
                    }
                });
                // construct milestone text
                int targetMilestone = currentPlan.getMilestone();
                int currentSuccesiveDays = currentPlan.getSuccessiveDay();
                String milestone = (currentSuccesiveDays >= targetMilestone ? "Complete\n" : "") + currentSuccesiveDays + "/" + targetMilestone;
                tvMilestone.setText(milestone);
                // if is smoker view, set textview for money saved
                if (isRequestFromSmokerFragement) {
                    double moneySaved = (targetAmount - realAmount) * Math.round((double)QuitSmokeClientUtils.getPricePerPack() / 20.0 * 100.00) / 100.00;
                    tvMoneySaved.setText("$" + moneySaved);
                }
                QuitSmokeClientUtils.setReward(currentPlan.getReward());
                QuitSmokeClientUtils.setAlreadyTakenSmokeNo(currentPlan.getRealAmount());
            } else {
                Toast.makeText(smokerMainActivity, "Exception occurred when create plan. Try again. If not work, remove the shit app.", Toast.LENGTH_LONG).show();
            }
        }
    };
}