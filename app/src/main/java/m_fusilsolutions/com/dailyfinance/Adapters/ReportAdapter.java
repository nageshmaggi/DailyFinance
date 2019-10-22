package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Collection_Activity;
import m_fusilsolutions.com.dailyfinance.DailyFinanceReport_Activity;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;
import m_fusilsolutions.com.dailyfinance.R;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    Context cTxt;
    List<ReportData> _tableDataList;
    int screen = 0;

    public ReportAdapter(Context cTxt, List<ReportData> tableDataList,int screen){
        this.cTxt = cTxt;
        this._tableDataList = tableDataList;
        this.screen = screen;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(cTxt).inflate(R.layout.report_row_layout,parent,false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ReportData data = _tableDataList.get(position);
        holder.tvName.setText(data.getName());
        holder.tvTitle.setText(data.getVSNo());
        holder.tvTotalAmt.setText(data.getAmount());
        if(screen==1 || screen==3) {
            holder.tvDate.setText(data.getDate());
            holder.tvNetAmt.setText(data.getNetAmount());
            holder.tvPerDayAmt.setText(data.getPerDayAmt());
//            if(screen!=3)
                if(data.getStatus().equals("0")){
                    holder.tvStatus.setText("Status: Active");
                    holder.tvStatus.setTextColor(cTxt.getResources().getColor(R.color.Green));
                    holder.ivCollBtn.setVisibility(View.VISIBLE);
                }
                else{
                    holder.tvStatus.setText("Status: Closed");
                    holder.tvStatus.setTextColor(cTxt.getResources().getColor(R.color.Red));
                }
        }
        else {
            holder.tvDate.setText("P DT : " + data.getDate());
            holder.tvColDate.setText("C DT : " + data.getColDate());//21102019
        }
            holder.tvRemarks.setText(data.getRemarks());
        holder.tvMobNo.setText(data.getMobileNo());
        holder.tvRefNo.setText(data.getRefNo());
    }

    @Override
    public int getItemCount() {
        return _tableDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout;
        ImageView ivCollBtn;//21102019
        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvNetAmt,tvPerDayAmt,tvRemarks,tvMobNo,tvRefNo,tvStatus,tvNetTil,tvPDTil,
                tvR2,tvR3,tvColDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvR2 = (TextView) itemView.findViewById(R.id.tvRupee2);
            tvR3 = (TextView) itemView.findViewById(R.id.tvRupee3);
            tvNetTil = (TextView) itemView.findViewById(R.id.tvNetTil);
            tvPDTil = (TextView) itemView.findViewById(R.id.tvPerDayTil);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTotalAmt = (TextView) itemView.findViewById(R.id.tvTotalAmt);
            tvColDate = (TextView) itemView.findViewById(R.id.tvColDate);//21102019
            if(screen==1 || screen==3) {
                ivCollBtn = (ImageView) itemView.findViewById(R.id.imgvCollectionBtn);//21102019
                layout = (LinearLayout) itemView.findViewById(R.id.llParentLayoutReportsCV);
                tvPDTil.setVisibility(View.VISIBLE);
                tvNetTil.setVisibility(View.VISIBLE);
                tvR2.setVisibility(View.VISIBLE);
                tvR3.setVisibility(View.VISIBLE);
                tvNetAmt = (TextView) itemView.findViewById(R.id.tvNetAmt);
                tvPerDayAmt = (TextView) itemView.findViewById(R.id.tvPerDayAmt);
                tvStatus = (TextView) itemView.findViewById(R.id.tvStatusDF);
                layout.setOnClickListener(this);
                ivCollBtn.setOnClickListener(this);
            }
            else{
                tvColDate.setVisibility(View.VISIBLE);
                tvNetTil.setVisibility(View.INVISIBLE);
                tvPDTil.setVisibility(View.INVISIBLE);
                tvR2.setVisibility(View.INVISIBLE);
                tvR3.setVisibility(View.INVISIBLE);
            }
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvMobNo = (TextView) itemView.findViewById(R.id.tvMobNo);
            tvRefNo = (TextView) itemView.findViewById(R.id.tvRefNo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                tvMobNo.setTextIsSelectable(true);
            }


        }
//22102019
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ReportData data = _tableDataList.get(position);
            if(v.getId() == R.id.llParentLayoutReportsCV) {
                ((DailyFinanceReport_Activity) cTxt).SetRecyclerViewItem(data);
            }
            else if(v.getId() == R.id.imgvCollectionBtn){
                Intent in = new Intent(cTxt, Collection_Activity.class);
                in.putExtra("MobileNo",data.getMobileNo());
                cTxt.startActivity(in);
            }
        }
    }
}
