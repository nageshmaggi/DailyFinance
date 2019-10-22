package m_fusilsolutions.com.dailyfinance.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.DailyFinance_Activity;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.R;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.MyViewHolder> {
    Context cTxt;
    List<DailyFinanceData> _collectionList;
    AlertDialog ad;
    boolean isEdit = false;
    InputUtils _inputXele;
    ExecuteDataBase _exeDB;

    public FinanceAdapter(Context cTxt, List<DailyFinanceData> collectionList, AlertDialog ad, boolean isEdit){
        this.cTxt = cTxt;
        this._collectionList = collectionList;
        this.ad = ad;
        this.isEdit = isEdit;
        _inputXele = new InputUtils(this.cTxt);
        _exeDB = new ExecuteDataBase(this.cTxt);
    }

    @NonNull
    @Override
    public FinanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(cTxt).inflate(R.layout.collection_row_layout,parent,false);
        return new FinanceAdapter.MyViewHolder(layoutView);
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
        holder.tvNetAmt.setText("NetAmt: ₹ " + data.getNetAmount());
        holder.tvPerDayAmt.setText("PerDayAmt : ₹ " + data.getPerDayAmt());
        holder.tvRemarks.setText(data.getRemarks());
    }


    @Override
    public int getItemCount() {
        return _collectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName,tvDate,tvTitle,tvTotalAmt,tvNetAmt,tvPerDayAmt,tvRemarks,tvMobNo,tvRefNo;
        ImageView img_delete;
        LinearLayout llMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTotalAmt = (TextView) itemView.findViewById(R.id.tvTotalAmt);
            tvNetAmt = (TextView) itemView.findViewById(R.id.tvNetAmt);
            tvPerDayAmt = (TextView) itemView.findViewById(R.id.tvPerDayAmt);
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
            int id = v.getId();
            int position = getAdapterPosition();
            DailyFinanceData data = _collectionList.get(position);
            if(id == R.id.llMain) {
                if (isEdit) {
                    ((DailyFinance_Activity) cTxt).SetRecyclerViewItem(data);
                    ad.dismiss();
                }
            }else if(id == R.id.img_delete){
                if(data.getCollectionCount() == 0){
                    ShowConfirmDialog(position,data);
                }else if(data.getCollectionCount() > 0){
                    new CustomToast(cTxt).ShowToast("Collection Done, You Cannot Perform Delete",true);
                }
            }
        }
    }

    private void ShowConfirmDialog(final int position, DailyFinanceData data) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                cTxt);
        alertDialogBuilder.setTitle("Are you sure want to delete?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String xele = _inputXele.getDFDeleteXele(data.getTransId());
                        _collectionList.remove(position);
                        _exeDB.ExecuteResult(SPName.USP_MA_DF_OtherFinanceData.toString(),xele, TransType.DeleteDFData.toString(),"4", Constants.HTTP_URL);
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
