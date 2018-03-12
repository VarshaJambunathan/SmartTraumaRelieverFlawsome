package com.example.jaisa.smarttraumareliever_flawsome.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jaisa.smarttraumareliever_flawsome.Beans.Complaint;
import com.example.jaisa.smarttraumareliever_flawsome.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SolvedCrimesAdapter extends RecyclerView.Adapter<SolvedCrimesAdapter.ViewHolder>{

    ArrayList<Complaint> mComplaintDetails;

    public SolvedCrimesAdapter(ArrayList<Complaint> mComplaintDetails) {
        this.mComplaintDetails = mComplaintDetails;
    }

    @Override
    public final SolvedCrimesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_crimes, parent, false);

        SolvedCrimesAdapter.ViewHolder vh = new SolvedCrimesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mSubject.setText(mComplaintDetails.get(position).getSubject());
        holder.mDesc.setText(mComplaintDetails.get(position).getDesciption());
        holder.mDate.setText(mComplaintDetails.get(position).getDate());
        holder.mTime.setText(mComplaintDetails.get(position).getTime());
        holder.mReportedTo.setText(mComplaintDetails.get(position).getReportedTo());

    }

    @Override
    public int getItemCount() {
        return mComplaintDetails.size() ;
        //return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mSubject, mDesc, mDate, mTime, mReportedTo;
        public ViewHolder(View itemView) {
            super(itemView);

            mSubject = (TextView) itemView.findViewById(R.id.subject);
            mDesc = (TextView) itemView.findViewById(R.id.description);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mReportedTo = (TextView) itemView.findViewById(R.id.reported_to);

            /*
            mSubject.setText("1");
            mDesc.setText("1");
            mDate.setText("1");
            mTime.setText("1");
            mReportedTo.setText("1");
            */
        }
    }
}