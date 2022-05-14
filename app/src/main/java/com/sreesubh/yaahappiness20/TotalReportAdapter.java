package com.sreesubh.yaahappiness20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TotalReportAdapter extends RecyclerView.Adapter<TotalReportAdapter.ViewHolder> {
    List<TotalReportModelView> list;

    public TotalReportAdapter(List<TotalReportModelView> list) {
        this.list = list;
    }

    @NonNull
    
    @Override
    public TotalReportAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  TotalReportAdapter.ViewHolder holder, int position) {
        String date = list.get(position).getDate();
        String day = list.get(position).getDay();
        double rate = list.get(position).getScore();
        holder.setData(rate,date,day);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_show,day_show,rate_show;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            rate_show = itemView.findViewById(R.id.rate_show);
            day_show = itemView.findViewById(R.id.day_show);
            date_show = itemView.findViewById(R.id.date_show);
        }
        public void setData(double sc,String... data)
        {
            rate_show.setText(sc+"");
            day_show.setText(data[1]);
            date_show.setText(data[0]);
        }
    }
}
