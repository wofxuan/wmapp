package com.mx.android.wmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.entity.OptData;

/**
 * Created by Administrator on 2016-06-07.
 */

public class OptAdapter extends RecyclerView.Adapter<OptAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext = null;
    private RecyclerViewItemClick mOnItemClickListener;

    public OptAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return OptData.OptDataS.length;
    }

    public OptData getItem(int position) {
        return OptData.OptDataS[position];
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.data_list, parent,
                false));
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return OptData.OptDataS[position].sid;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClick mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 绑定ViewHolder的数据。
     *
     * @param itemViewHolder
     * @param position       数据源list的下标
     */
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.sId.setText(Long.toString(getItem(position).sid));
        itemViewHolder.mtitle.setText(getItem(position).title);
        itemViewHolder.mimg.setImageResource(getItem(position).imgId);
        if (mOnItemClickListener != null) {
            /**
             * 这里加了判断，itemViewHolder.itemView.hasOnClickListeners()
             * 目的是减少对象的创建，如果已经为view设置了click监听事件,就不用重复设置了
             * 不然每次调用onBindViewHolder方法，都会创建两个监听事件对象，增加了内存的开销
             */
            if (!itemViewHolder.itemView.hasOnClickListeners()) {
                Log.e("ListAdapter", "setOnClickListener");
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = itemViewHolder.getAdapterPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = itemViewHolder.getAdapterPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }

    }

    /**
     * 处理item的点击事件和长按事件
     */

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView sId;
        private TextView mtitle;
        private ImageView mimg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            sId = (TextView) itemView.findViewById(R.id._id);
            mtitle = (TextView) itemView.findViewById(R.id._name);
            mimg = (ImageView) itemView.findViewById(R.id._img);
        }
    }
}
