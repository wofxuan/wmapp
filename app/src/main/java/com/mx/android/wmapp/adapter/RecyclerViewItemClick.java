package com.mx.android.wmapp.adapter;

import android.view.View;

/**
 * Created by Administrator on 2016-09-21.
 */
public interface RecyclerViewItemClick {
    public void onItemClick(View view, int position);

    public void onItemLongClick(View view, int position);
}
