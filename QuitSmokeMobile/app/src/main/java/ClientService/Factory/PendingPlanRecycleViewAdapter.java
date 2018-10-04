package clientservice.factory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Interface.IPlanRecycleItemClick;
import com.example.william.quitsmokeappclient.R;
import java.util.ArrayList;
import clientservice.entities.PlanEntity;

public class PendingPlanRecycleViewAdapter extends RecyclerView.Adapter<PendingPlanRecycleViewAdapter.MyViewHolder> {
    private final ArrayList<PlanEntity> mDataset;
    private final IPlanRecycleItemClick listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView tvTargetAmount;
        private TextView tvStatus;
        private TextView tvCreateDate;
        private TextView tvRealAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTargetAmount = (TextView)itemView.findViewById(R.id.plan_item_target_amount);
            tvStatus = (TextView)itemView.findViewById(R.id.plan_item_status);
            tvCreateDate = (TextView)itemView.findViewById(R.id.plan_item_create_date);
            tvRealAmount = (TextView)itemView.findViewById(R.id.plan_real_amount);
        }

        public void bind(final PlanEntity item, final IPlanRecycleItemClick listener) {
            tvTargetAmount.setText("Target Amount of this plan:" + item.getTargetAmount());
            tvStatus.setText(item.getStatus());
            tvCreateDate.setText(item.getCreateDate());
            tvRealAmount.setText("Real Amount Taken By your Partner:" + item.getRealAmount());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PendingPlanRecycleViewAdapter(ArrayList<PlanEntity> mDataset, IPlanRecycleItemClick listener) {
        this.mDataset = mDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PendingPlanRecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list, parent,false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
