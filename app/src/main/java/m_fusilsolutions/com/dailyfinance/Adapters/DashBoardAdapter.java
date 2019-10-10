package m_fusilsolutions.com.dailyfinance.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Models.DashBoardData;
import m_fusilsolutions.com.dailyfinance.R;

/**
 * Created by Android on 02-10-2019.
 */

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder>
{
    Context mCtx;
    List<DashBoardData> dashBoardList;

    public DashBoardAdapter(Context mCtx, List<DashBoardData> dashBoardList) {
        this.mCtx = mCtx;
        this.dashBoardList = dashBoardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.dashboard_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DashBoardData currentData = dashBoardList.get(position);
        holder.tvname.setText(currentData.getName());
        holder.tvamt.setText(currentData.getAmount());
        holder.imgview.setImageResource(currentData.getImage());
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgview;
        TextView tvname, tvamt;
        Typeface typeface;
        Typeface typefaceBold;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgview = (ImageView) itemView.findViewById(R.id.img_view);
            tvname = (TextView) itemView.findViewById(R.id.tv_name);
            tvamt = (TextView) itemView.findViewById(R.id.tv_amt);
            typeface = Typeface.createFromAsset(mCtx.getAssets(),"Caviar-Dreams.ttf");
            typefaceBold = Typeface.createFromAsset(mCtx.getAssets(),"Caviar_Dreams_Bold.ttf");
            tvname.setTypeface(typeface);
            tvamt.setTypeface(typefaceBold);
        }
    }
}
