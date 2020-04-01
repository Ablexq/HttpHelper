package com.example.httphelplib.host;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httphelplib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author:DingDeGao
 * time:2019-11-01-10:37
 * function: CaptureContentAdapter
 */
public class HostAdapter extends RecyclerView.Adapter<HostAdapter.VH> {

    private List<UrlEntity> list = new ArrayList<>();
    private Context context;

    public HostAdapter(Context context) {
        this.context = context;
    }

    void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setData(List<UrlEntity> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HostAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_host,
                viewGroup, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HostAdapter.VH viewHolder, final int position) {
        final UrlEntity entity = list.get(position);
        viewHolder.itemHostTv1.setText(entity.getName());
        //https://www.baidu.com
        viewHolder.itemHostTv2.setText(entity.getScheme() + "://" + entity.getHost() + ":" + entity.getPort());
        viewHolder.itemHostRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.onChooseClick(position);
            }
        });

        viewHolder.itemHostIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.onDeleteClick(position);
            }
        });
        boolean selected = entity.isSelected();
        if (selected) {
            viewHolder.itemHostCb.setChecked(true);
        } else {
            viewHolder.itemHostCb.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        private RelativeLayout itemHostRoot;
        private CheckBox itemHostCb;
        private TextView itemHostTv1;
        private TextView itemHostTv2;
        private ImageView itemHostIv1;

        VH(@NonNull View itemView) {
            super(itemView);
            itemHostRoot = itemView.findViewById(R.id.item_host_root);
            itemHostCb = itemView.findViewById(R.id.item_host_cb);
            itemHostTv1 = itemView.findViewById(R.id.item_host_tv1);
            itemHostTv2 = itemView.findViewById(R.id.item_host_tv2);
            itemHostIv1 = itemView.findViewById(R.id.iv_host_iv1);
        }
    }

    public interface OnClickItemListener {
        void onChooseClick(int position);

        void onDeleteClick(int position);
    }

    private OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }
}

