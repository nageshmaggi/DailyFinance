package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Collection_Activity;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;
import m_fusilsolutions.com.dailyfinance.PendingCollectionsReport_Activity;
import m_fusilsolutions.com.dailyfinance.R;

public class PendingsReportAdapter extends RecyclerView.Adapter<PendingsReportAdapter.MyViewHolder> {
    Context cTxt;
    List<ReportData> _collectionList;
    int DF;

    public PendingsReportAdapter(Context cTxt, List<ReportData> collectionList,int DF){
        this.cTxt = cTxt;
        this._collectionList = collectionList;
        this.DF = DF;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(cTxt).inflate(R.layout.memberwise_report_layout,parent,false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReportData data = _collectionList.get(position);
        holder.tvName.setText(data.getName());
        holder.tvDate.setText(data.getDate());
        holder.tvTitle.setText(data.getVSNo());
        holder.tvTotalAmt.setText(data.getAmount());
        if(DF == 1){
            holder.tvNetAmt.setText(data.getNetAmount());
            holder.tvPerDayAmt.setText(data.getPerDayAmt());
            if(data.getStatus().equals("0")){
                holder.tvStaus.setText("Status: Active");
                holder.tvStaus.setTextColor(cTxt.getResources().getColor(R.color.Green));
                holder.ivCollection.setVisibility(View.VISIBLE);//22102019
            }
            else{
                holder.tvStaus.setText("Status: CLosed");
                holder.tvStaus.setTextColor(cTxt.getResources().getColor(R.color.Red));
            }

        }

        holder.tvRemarks.setText(data.getRemarks());
        holder.tvMobNo.setText(data.getMobileNo());
        holder.tvRefNo.setText(data.getRefNo());
    }


    @Override
    public int getItemCount() {
        return _collectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvNetAmt,tvPerDayAmt,tvRemarks,tvMobNo,tvRefNo,tvStaus,
                tvNetTil,tvPDtil,tvR2,tvR3;
        ImageView ivCollection;//22102019
        LinearLayout llMain;
        CardView cvMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCollection = (ImageView) itemView.findViewById(R.id.ivCollRd);//22102019
            tvR2 = (TextView) itemView.findViewById(R.id.tvR2M);
            tvR3= (TextView) itemView.findViewById(R.id.tvR3M);
            cvMain = (CardView) itemView.findViewById(R.id.cvMainCV);
            tvNetTil = (TextView) itemView.findViewById(R.id.tvNetTil);
            tvPDtil = (TextView) itemView.findViewById(R.id.tvPerDayTil);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTotalAmt = (TextView) itemView.findViewById(R.id.tvTotalAmt);
            tvNetAmt = (TextView) itemView.findViewById(R.id.tvNetAmt);
            tvPerDayAmt = (TextView) itemView.findViewById(R.id.tvPerDayAmt);
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvMobNo = (TextView) itemView.findViewById(R.id.tvMobNo);
            tvRefNo = (TextView) itemView.findViewById(R.id.tvRefNo);
            tvStaus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvMobNo.setTextIsSelectable(true);
            llMain.setOnClickListener(this);
            if(DF==1){
                ivCollection.setOnClickListener(this);
                tvPDtil.setVisibility(View.VISIBLE);
                tvNetTil.setVisibility(View.VISIBLE);
                tvR2.setVisibility(View.VISIBLE);
                tvR3.setVisibility(View.VISIBLE);
            }
            else
            {
                tvPDtil.setVisibility(View.INVISIBLE);
                tvNetTil.setVisibility(View.INVISIBLE);
                tvR2.setVisibility(View.INVISIBLE);
                tvR3.setVisibility(View.INVISIBLE);
            }
        }
//22102019
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ReportData data = _collectionList.get(position);
            if(v.getId() == R.id.llMain)
                ((PendingCollectionsReport_Activity)cTxt).SetRecyclerViewItem(data);
            else if(v.getId() == R.id.ivCollRd)
            {
                Intent in = new Intent(cTxt, Collection_Activity.class);
                in.putExtra("MobileNo",data.getMobileNo());
                cTxt.startActivity(in);
            }
        }
    }
}
