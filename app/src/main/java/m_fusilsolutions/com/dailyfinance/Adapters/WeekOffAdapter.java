package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Collection_Activity;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.R;

/**
 * Created by Android on 16-10-2019.
 */

public class WeekOffAdapter extends RecyclerView.Adapter<WeekOffAdapter.MyViewHolder>
{
    List<DailyFinanceData> financeList;
    Context mCtx;
    boolean flag;
    Typeface typeface,typefaceBold,typefaceBoldItalic;

    public WeekOffAdapter(Context mCtx, List<DailyFinanceData> financeList, boolean flag)
    {
        this.mCtx = mCtx;
        this.financeList = financeList;
        this.flag = flag;
        typeface = Typeface.createFromAsset(mCtx.getAssets(), "Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(mCtx.getAssets(), "CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(mCtx.getAssets(), "Caviar_Dreams_Bold.ttf");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(mCtx).inflate(R.layout.week_off_row_item,viewGroup,false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DailyFinanceData data = financeList.get(position);
        holder.tvAmount.setText(data.getAmount());
        holder.tvDate.setText(data.getDate());
        if (!data.isWeekDaySelected()) {
            holder.cbCheck.setChecked(false);
            data.setWeekDaySelected(false);
        } else {
            holder.cbCheck.setChecked(true);
            data.setWeekDaySelected(true);
        }
    }

    public void selectAll() {
        if(!flag)
            flag = true;
        else
            flag = false;

        int count=0;
        for (DailyFinanceData data : financeList) {
            if (flag)
                if (count <= 100) {
                    data.setWeekDaySelected(true);
                } else {
                    data.setWeekDaySelected(false);
                }
            count++;
        }
        //SetCollTotAmount();
        notifyDataSetChanged();
    }

    private void SetCollTotAmount()
    {
        Double totAmt = 0.0;
        if(flag) {
            double amt = Double.parseDouble(financeList.get(0).getAmount());
            totAmt = financeList.size() * amt;
            ((Collection_Activity)mCtx).tvWeekDayTotAmt.setText(String.valueOf(totAmt.intValue()));
            ((Collection_Activity)mCtx).tvWeekTotCount.setText(String.valueOf(financeList.size()));
        }else{
            ((Collection_Activity)mCtx).tvWeekDayTotAmt.setText("");
        }
    }

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
            tvAmount.setTypeface(typefaceBold);
            tvDate.setTypeface(typefaceBold);
            cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        financeList.get(getAdapterPosition()).setWeekDaySelected(true);
                    }else{
                        financeList.get(getAdapterPosition()).setWeekDaySelected(false);
                    }
                }
            });
        }
    }
}
