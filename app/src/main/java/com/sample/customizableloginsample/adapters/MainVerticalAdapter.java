package com.sample.customizableloginsample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.models.Sport;

import java.util.ArrayList;

public class MainVerticalAdapter extends RecyclerView.Adapter<MainVerticalAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Sport> _sportsList;
    private float rowwidth;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rootLayout;
        public ImageView thumbnail;


        public MyViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.rootLinearLayout);
            thumbnail = view.findViewById(R.id.imgbanner);

            // llmain = view.findViewById(R.id.llmain);*/
        }
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public LinearLayout rootLayout;
        public ImageView thumbnail;


        public MyViewHolder1(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.rootLinearLayout);
            // thumbnail = view.findViewById(R.id.imgbanner);
            // llmain = view.findViewById(R.id.llmain);*/
        }
    }


    public MainVerticalAdapter(Context context, float getwidthonepercent) {
        this.context = context;

        rowwidth = getwidthonepercent;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mainrecycler_row1, parent, false);

            return new MyViewHolder(itemView);
        } else if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mainrecycler_row2, parent, false);

            return new MyViewHolder(itemView);
        }
        else if(viewType==2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mainrecycler_row3, parent, false);

            return new MyViewHolder(itemView);
        }



        else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mainrecycler_row1, parent, false);

            return new MyViewHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else if (position == 2) {
            return 2;
        }
        return 0;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        //final Sport sport = _sportsList.get(position);
        if (holder.thumbnail != null) {
            holder.thumbnail.getLayoutParams().height = (int) rowwidth;
        }
        /*if(holder.rootLayout!=null){
            holder.rootLayout.getLayoutParams().height=(int)rowwidth;
        }*/
        //  holder.rootLayout.getLayoutParams().height = (int)rowwidth;
        // holder.thumbnail.getLayoutParams().height = (int)rowwidth;
/*
        holder.llmain.getLayoutParams().width = (int) rowwidth;

        Glide.with(context)
                .load(sport.getImgURL())

                .into(holder.thumbnail);*/
        // holder.name.setText(sport.getSportName().toString());
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
