package com.transport.xianxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.transport.xianxian.R;
import com.transport.xianxian.model.JiFenLieBiaoModel;

import java.util.List;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2016/7/6.
 * Email：1125213018@qq.com
 * description：pop adapter
 */
public class JiFenLieBiao_GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<JiFenLieBiaoModel.GoodsDataBean> list;
    private int selectIndex = 0;

    public JiFenLieBiao_GridViewAdapter(Context context, List<JiFenLieBiaoModel.GoodsDataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int selectItem) {
        this.selectIndex = selectItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jifenliebiao_gridview, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.textView4);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*holder.textView1.setText(list.get(position));
        if (selectIndex == position) {
            holder.textView1.setSelected(true);
            holder.textView1.setPressed(true);
            holder.textView1.setTextColor(context.getResources().getColor(R.color.white));
            holder.textView1.setBackgroundResource(R.drawable.yuanxing_hongse);
        } else {
            holder.textView1.setSelected(false);
            holder.textView1.setPressed(false);
            holder.textView1.setTextColor(context.getResources().getColor(R.color.red));
            holder.textView1.setBackgroundResource(R.drawable.yuanxingbiankuang_hongse);
        }*/
        holder.textView1.setText(list.get(position).getTitle());
        holder.textView2.setText(list.get(position).getSub_title());
        holder.textView3.setText("剩余" + list.get(position).getStore() + "件");
        holder.textView4.setText(list.get(position).getScore() + "积分");
        if (!list.get(position).getImage().equals(""))
            Glide.with(convertView)
                    .load(IMGHOST + list.get(position).getImage())
//                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                    .into(holder.imageView);//加载图片

        return convertView;
    }

    private static class ViewHolder {
        TextView textView1, textView2, textView3, textView4;
        ImageView imageView;
    }
}
