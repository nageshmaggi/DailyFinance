package m_fusilsolutions.com.dailyfinance.Adapters;

import android.app.AlertDialog;
import android.content.Context;
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
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.R;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    Context cTxt;
    List<DailyFinanceData> _collectionList;
    AlertDialog ad;
    boolean isEdit = false;
    InputUtils _inputXele;
    ExecuteDataBase _exeDB;

    public CollectionAdapter(Context cTxt, List<DailyFinanceData> collectionList, AlertDialog ad, boolean isEdit){
        this.cTxt = cTxt;
        this._collectionList = collectionList;
        this.ad = ad;
        this.isEdit = isEdit;
    }

    @NonNull
    @Override
    public CollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(cTxt).inflate(R.layout.report_row_layout,parent,false);
        return new CollectionAdapter.MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DailyFinanceData data = _collectionList.get(position);
        holder.tvName.setText(data.getName());
        holder.tvMobNo.setText(data.getMobileNo());
        holder.tvRefNo.setText(data.getRefNo());
        holder.tvTitle.setText(data.getVSNo());
        holder.tvDate.setText(data.getDate());
        holder.tvTotalAmt.setText("TotalAmt : ₹ " + data.getAmount());
        holder.tvRemarks.setText(data.getRemarks());
    }


    @Override
    public int getItemCount() {
        return _collectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvRemarks,tvMobNo,tvRefNo;
        ImageView img_delete;
        LinearLayout llMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTotalAmt = (TextView) itemView.findViewById(R.id.tvTotalAmt);
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvMobNo = (TextView) itemView.findViewById(R.id.tvMobNo);
            tvRefNo = (TextView) itemView.findViewById(R.id.tvRefNo);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            if(isEdit){
                img_delete.setVisibility(View.GONE);
            }else{
                img_delete.setVisibility(View.VISIBLE);
            }
            llMain.setOnClickListener(this);
            img_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            DailyFinanceData data = _collectionList.get(position);
            ((Collection_Activity)cTxt).SetRecyclerViewItem(data);
            ad.dismiss();
        }
    }
}
