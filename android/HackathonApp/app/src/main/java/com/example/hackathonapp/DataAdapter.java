package com.example.hackathonapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<Data> mDataList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView titleText;
        //        TextView majorText;
        TextView deadlineText;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            titleText=(TextView)view.findViewById(R.id.data_title);
//            majorText=(TextView)view.findViewById(R.id.data_major);
            deadlineText=(TextView)view.findViewById(R.id.data_deadline);
        }
    }
    public DataAdapter(List<Data> dataList){
        mDataList=dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext==null){
            mContext=viewGroup.getContext();
        }
        final View view= LayoutInflater.from(mContext).inflate(R.layout.data_item,
                viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url",mDataList.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data data=mDataList.get(i);
        viewHolder.titleText.setText(data.getTitle());
        viewHolder.deadlineText.setText(data.getDate());
//        viewHolder.majorText.setText(data.getCategory());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
