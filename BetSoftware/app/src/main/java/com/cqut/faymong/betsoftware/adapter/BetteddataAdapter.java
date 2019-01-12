package com.cqut.faymong.betsoftware.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.entity.CompetitionInfor;
import com.cqut.faymong.betsoftware.entity.shedule;
import com.cqut.faymong.betsoftware.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 2018/11/20.
 */

public class BetteddataAdapter  extends RecyclerView.Adapter<BetteddataAdapter.VH>{
    private static final String ARG_MSG = "arg_msg";
    private LayoutInflater mInflater;
    private Context mContext;

    private List<shedule> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public BetteddataAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<shedule> beans) {
        mItems.clear();
        mItems.addAll(beans);
        notifyDataSetChanged();
    }
    public void addMsg(shedule bean) {
        mItems.add(bean);
        notifyItemInserted(mItems.size() - 1);
    }

    public void refreshMsg(shedule bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;

        notifyItemChanged(index);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fourth_shedulelist, parent, false);
        final VH holder = new VH(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(holder.getAdapterPosition(), v, holder);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        shedule item = mItems.get(position);
        holder.date.setText(item.shedule_date);
        holder.time.setText(item.shedule_time);
        holder.vs.setText(item.shedule_vs);




    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }



    class VH extends RecyclerView.ViewHolder {

        private TextView time,date,vs;

        public VH(View itemView) {
            super(itemView);

            time = (TextView)itemView.findViewById(R.id.shedule_time);
            date = (TextView)itemView.findViewById(R.id.shedule_date);
            vs = (TextView)itemView.findViewById(R.id.vs);


        }
    }
}
