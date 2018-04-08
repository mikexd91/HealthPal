package com.mikexd.healthpal.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.randomcolor.RandomColor;
import com.mikexd.healthpal.Activity.DetailedSickActivity;
import com.mikexd.healthpal.Data.Sickness;
import com.mikexd.healthpal.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xunda on 7/4/18.
 */

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {

    private Context context;
    private ArrayList<Sickness> data;

    public MasonryAdapter(Context context, ArrayList<Sickness> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cell, parent, false);
        MasonryView masonryView = new MasonryView(layoutView);

        return masonryView;
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        holder.setCell(data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class MasonryView extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        CardView cardView;
        Sickness sick;

        public MasonryView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

        public void setCell(Sickness sick){
            this.sick = sick;
            textView.setText(sick.getIssue());
            randomColor();
        }

        public void randomColor(){
            RandomColor randomColor = new RandomColor();
//            int color = randomColor.randomColor();
            int[] color = randomColor.random(RandomColor.Color.PINK, 18);
            int[] colorz = randomColor.random(RandomColor.Color.ORANGE, 18);
            int[] both = (int[]) ArrayUtils.addAll(color, colorz);
            Random rand = new Random();
            int  n = rand.nextInt(35) + 0;
            cardView.setCardBackgroundColor(both[n]);
        }

        @Override
        public void onClick(View v) {
            Log.d("mula", "onClick " + getPosition() + " " + textView.getText().toString());
            Intent i = new Intent(context, DetailedSickActivity.class);
            i.putExtra("id", sick.getID());
            i.putExtra("sick",sick.getIssue());
            context.startActivity(i);
        }
    }
}
