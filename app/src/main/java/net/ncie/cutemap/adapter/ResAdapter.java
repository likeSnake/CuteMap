package net.ncie.cutemap.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.geojson.Point;
import com.tencent.mmkv.MMKV;

import net.ncie.cutemap.R;
import net.ncie.cutemap.act.MainAct;
import net.ncie.cutemap.bean.ResultsBean;


import java.util.List;

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.ViewHolder> {

    private List<ResultsBean> infos;
    private Context context;

    public ResAdapter(List<ResultsBean> list, Context context) {
        this.infos = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView aaa;
        private TextView bbb;
        private TextView ccc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aaa = itemView.findViewById(R.id.tv_name);
            bbb = itemView.findViewById(R.id.tv_address);
            ccc = itemView.findViewById(R.id.tv_distance);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultsBean destinationInfo = infos.get(position);
        String address = destinationInfo.getAddress();
        String distance = destinationInfo.getDistance();
        Point point = destinationInfo.getPoint();
        String name = destinationInfo.getName();
        holder.bbb.setText(address);
        holder.ccc.setText(distance);
        holder.aaa.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainAct.class);
                MMKV.defaultMMKV().encode("longitude",String.valueOf(point.longitude()));
                MMKV.defaultMMKV().encode("latitude",String.valueOf(point.latitude()));
             //   context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return infos.size();
    }


}
