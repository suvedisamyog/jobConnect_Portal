package com.example.jobconnect.ADAPTERS;


// JobInfo_Adp.java
import android.content.ClipData;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.example.jobconnect.Modules.JobKeyValue;
import com.example.jobconnect.R;

import java.util.List;

public class JobInfo_Adp extends RecyclerView.Adapter<JobInfo_Adp.ViewHolder> {
    private List<JobKeyValue> itemList;

    public JobInfo_Adp(List<JobKeyValue> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_info, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobKeyValue item = itemList.get(position);
        holder.jobTitleTextView.setText(item.getJobTitle());
        holder.valueTextView.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitleTextView;
        public TextView valueTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
        }
    }
}
