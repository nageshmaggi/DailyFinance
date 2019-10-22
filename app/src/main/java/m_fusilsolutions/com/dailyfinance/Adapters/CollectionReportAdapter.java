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

public class CollectionReportAdapter extends RecyclerView.Adapter<CollectionReportAdapter.MyViewHolder> {
    Context cTxt;
    List<ReportData> _tableDataList;
    int screen = 0;

    public CollectionReportAdapter(Context cTxt, List<ReportData> tableDataList, int screen){
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
        holder.tvDate.setText("P DT : "+data.getDate());
        holder.tvTitle.setText(data.getVSNo());
        holder.tvTotalAmt.setText(data.getAmount());
        holder.tvRemarks.setText(data.getRemarks());
        holder.tvMobNo.setText(data.getMobileNo());
        holder.tvRefNo.setText(data.getRefNo());
        holder.tvColDate.setText("C DT : "+data.getColDate());
    }

    @Override
    public int getItemCount() {
        return _tableDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvRemarks,tvMobNo,tvRefNo,tvNetTil,tvPDTil,
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
            tvColDate = (TextView) itemView.findViewById(R.id.tvColDate);
            tvColDate.setVisibility(View.VISIBLE);//22102019
                tvNetTil.setVisibility(View.INVISIBLE);
                tvPDTil.setVisibility(View.INVISIBLE);
                tvR2.setVisibility(View.INVISIBLE);
                tvR3.setVisibility(View.INVISIBLE);
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvMobNo = (TextView) itemView.findViewById(R.id.tvMobNo);
            tvRefNo = (TextView) itemView.findViewById(R.id.tvRefNo);
            tvMobNo.setTextIsSelectable(true);
        }
    }
}
