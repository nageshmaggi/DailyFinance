package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.R;

/**
 * Created by Android on 22-10-2019.
 */

public class PendingsPopUpAdapter extends RecyclerView.Adapter<PendingsPopUpAdapter.MyViewHolder>
{
    List<DailyFinanceData> financeList;
    Context mCtx;
    Typeface typeface,typefaceBold,typefaceBoldItalic;

    public PendingsPopUpAdapter(Context mCtx, List<DailyFinanceData> financeList)
    {
        this.mCtx = mCtx;
        this.financeList = financeList;
        typeface = Typeface.createFromAsset(mCtx.getAssets(), "Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(mCtx.getAssets(), "CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(mCtx.getAssets(), "Caviar_Dreams_Bold.ttf");
    }

    @NonNull
    @Override
    public PendingsPopUpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(mCtx).inflate(R.layout.week_off_row_item,viewGroup,false);
        return new PendingsPopUpAdapter.MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingsPopUpAdapter.MyViewHolder holder, int position) {
        DailyFinanceData data = financeList.get(position);
        holder.tvAmount.setText(data.getAmount());
        holder.tvDate.setText(data.getDate());
    }



//    private void SetCollTotAmount()
//    {
//        Double totAmt = 0.0;
//        if(flag) {
//            double amt = Double.parseDouble(financeList.get(0).getAmount());
//            totAmt = financeList.size() * amt;
//            ((Collection_Activity)mCtx).tvWeekDayTotAmt.setText(String.valueOf(totAmt.intValue()));
//            ((Collection_Activity)mCtx).tvWeekTotCount.setText(String.valueOf(financeList.size()));
//        }else{
//            ((Collection_Activity)mCtx).tvWeekDayTotAmt.setText("");
//        }
//    }

    @Override
    public int getItemCount() {
        return financeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount,tvDate;
        CheckBox cbCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            cbCheck = (CheckBox) itemView.findViewById(R.id.cbCheck);
            cbCheck.setVisibility(View.GONE);
            tvAmount.setTypeface(typefaceBold);
            tvDate.setTypeface(typefaceBold);
        }
    }
}
