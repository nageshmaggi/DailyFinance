package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        holder.tvDate.setText(data.getDate());
        holder.tvTitle.setText(data.getVSNo());
        holder.tvTotalAmt.setText(data.getAmount());
        if(screen==1 || screen==3) {

            holder.tvNetAmt.setText(data.getNetAmount());
            holder.tvPerDayAmt.setText(data.getPerDayAmt());
            //New Change 12102019 in Query too
            if(screen!=3)
                if(data.getStatus().equals("0")){
                    holder.tvStatus.setText("Status: Active");
                    holder.tvStatus.setTextColor(cTxt.getResources().getColor(R.color.Green));
                }
                else{
                    holder.tvStatus.setText("Status: Closed");
                    holder.tvStatus.setTextColor(cTxt.getResources().getColor(R.color.Red));
                }
        }
        else

            holder.tvRemarks.setText(data.getRemarks());
        holder.tvMobNo.setText(data.getMobileNo());
        holder.tvRefNo.setText(data.getRefNo());
    }

    @Override
    public int getItemCount() {
        return _tableDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvNetAmt,tvPerDayAmt,tvRemarks,tvMobNo,tvRefNo,tvStatus,tvNetTil,tvPDTil,
                tvR2,tvR3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//new change 15102019
            tvR2 = (TextView) itemView.findViewById(R.id.tvRupee2);
            tvR3 = (TextView) itemView.findViewById(R.id.tvRupee3);
            tvNetTil = (TextView) itemView.findViewById(R.id.tvNetTil);
            tvPDTil = (TextView) itemView.findViewById(R.id.tvPerDayTil);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTotalAmt = (TextView) itemView.findViewById(R.id.tvTotalAmt);
            if(screen==1 || screen==3) {
                tvPDTil.setVisibility(View.VISIBLE);
                tvNetTil.setVisibility(View.VISIBLE);
                //new change 15102019
                tvR2.setVisibility(View.VISIBLE);
                tvR3.setVisibility(View.VISIBLE);
                tvNetAmt = (TextView) itemView.findViewById(R.id.tvNetAmt);
                tvPerDayAmt = (TextView) itemView.findViewById(R.id.tvPerDayAmt);
                tvStatus = (TextView) itemView.findViewById(R.id.tvStatusDF);
            }
            else{
                tvNetTil.setVisibility(View.INVISIBLE);
                tvPDTil.setVisibility(View.INVISIBLE);
                //new change 15102019
                tvR2.setVisibility(View.INVISIBLE);
                tvR3.setVisibility(View.INVISIBLE);
            }
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvMobNo = (TextView) itemView.findViewById(R.id.tvMobNo);
            tvRefNo = (TextView) itemView.findViewById(R.id.tvRefNo);
            tvMobNo.setTextIsSelectable(true);


        }
    }
}
