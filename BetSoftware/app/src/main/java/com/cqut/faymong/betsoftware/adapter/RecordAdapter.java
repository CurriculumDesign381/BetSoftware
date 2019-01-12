package com.cqut.faymong.betsoftware.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.entity.Record;
import com.cqut.faymong.betsoftware.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter   extends RecyclerView.Adapter<RecordAdapter.VH>{
    private LayoutInflater mInflater;
    private List<Record> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public RecordAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<Record> beans) {
        mItems.clear();
        mItems.addAll(beans);
        notifyDataSetChanged();
    }

    public void refreshMsg(Chat bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;

        notifyItemChanged(index);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.soccer_record, parent, false);
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
        Record item = mItems.get(position);

        holder.tvName.setText(item.name);
        holder.tvMsg.setText(item.message);
        holder.tvTime.setText(item.score);
        holder.hometeam.setText(item.hometeam);
        holder.awayteam.setText(item.awayteam);
        holder.betaccount.setText(item.betaccount);
        holder.domain.setText(item.domain);
       /* @Override
        public void onClick(View arg0) {
            // 点击事件
            Toast.makeText(mContext, postion + "", 1000).show();
        }*/



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public Record getMsg(int position) {
        return mItems.get(position);
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvName, tvMsg, tvTime,awayteam,hometeam,domain,betaccount;

        public VH(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            awayteam = (TextView)itemView.findViewById(R.id.awayteam);
            hometeam = (TextView)itemView.findViewById(R.id.hometeam);
            domain = (TextView)itemView.findViewById(R.id.record_domain);
            betaccount = (TextView)itemView.findViewById(R.id.record_betaccount);

        }
    }
}
