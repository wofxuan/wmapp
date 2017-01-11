package com.mx.android.wmapp.viewweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.mx.wmapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-22.
 */
public class WebListAdspter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public WebListAdspter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WebItem webItem = null;
        if (convertView == null) {
            webItem = new WebItem();
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.weblistitem, null);
            webItem.delBtn = (ImageButton) convertView.findViewById(R.id.web_item_del);
            webItem.urlTitle = (TextView) convertView.findViewById(R.id.web_item_url);

            webItem.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((IWebViewEvent) context).onDelPage(position);
                }
            });

            webItem.urlTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((IWebViewEvent) context).onShowPage(position);
                }
            });
            convertView.setTag(webItem);
        } else {
            webItem = (WebItem) convertView.getTag();
        }
        //绑定数据
        webItem.delBtn.setBackgroundResource(R.mipmap.ic_delweb);
        webItem.urlTitle.setText((String) data.get(position).get("url"));
        return convertView;
    }

    /**
     * 组件集合，对应list.xml中的控件
     *
     * @author Administrator
     */
    public final class WebItem {
        public ImageButton delBtn;
        public TextView urlTitle;
    }
}
