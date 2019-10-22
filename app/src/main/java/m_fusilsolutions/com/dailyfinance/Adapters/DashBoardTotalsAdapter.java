package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.DailyFinanceReport_Activity;
import m_fusilsolutions.com.dailyfinance.Models.DashBoardData;
import m_fusilsolutions.com.dailyfinance.R;

public class DashBoardTotalsAdapter extends RecyclerView.Adapter<DashBoardTotalsAdapter.MyViewHolder> {
    Context mCtx;
    List<DashBoardData> dashBoardList;

    public DashBoardTotalsAdapter(Context mCtx, List<DashBoardData> dashBoardList) {
        this.mCtx = mCtx;
        this.dashBoardList = dashBoardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.dashboard_recycler_top_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DashBoardData currentData = dashBoardList.get(position);
        holder.tvnameAll.setText(currentData.getName());
        holder.tvamtAll.setText(currentData.getAmount());
        holder.imgviewAll.setImageResource(currentData.getImage());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dashBoardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgviewAll;
        TextView tvnameAll, tvamtAll;
        Typeface typeface;
        Typeface typefaceBold;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgviewAll = (ImageView) itemView.findViewById(R.id.img_viewAll);
            tvnameAll = (TextView) itemView.findViewById(R.id.tv_nameAll);
            tvamtAll = (TextView) itemView.findViewById(R.id.tv_amtAll);
            layout = (LinearLayout) itemView.findViewById(R.id.llTopMainCVItem);
            typeface = Typeface.createFromAsset(mCtx.getAssets(), "Caviar-Dreams.ttf");
            typefaceBold = Typeface.createFromAsset(mCtx.getAssets(), "Caviar_Dreams_Bold.ttf");
            tvnameAll.setTypeface(typeface);
            tvamtAll.setTypeface(typefaceBold);
            layout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int positon = getAdapterPosition();
            DashBoardData data = dashBoardList.get(positon);//new change 18102019
            if (data.getName().equals("Total Finance")) {
                Intent in = new Intent(mCtx, DailyFinanceReport_Activity.class);
                in.putExtra("screen", 3);
                mCtx.startActivity(in);
            }
        }
    }
}
