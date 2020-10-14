package com.example.myweatherapp;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{


private String[] mWeatherData;


private AdapterOnClickHandler mClickHandlerObj;

/*Interface to be used to handle clicks*/
public interface AdapterOnClickHandler{
    void onClick(String dayWeather);
}
/**/

    public RecyclerViewAdapter(AdapterOnClickHandler clickedHandled){
            mClickHandlerObj = clickedHandled;
    }



    public void setWeatherData(String [] data){
        mWeatherData=data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context=  parent.getContext();
        int layoutIdForItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForItem, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            String dataForDayi = mWeatherData[position];
           holder.mItemWeatherView.setText(dataForDayi);
    }

    @Override
    public int getItemCount() {
        if(mWeatherData==null){
            return 0;
        }
        return mWeatherData.length;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mItemWeatherView;
        public RecyclerViewHolder( View itemView) {
            super(itemView);
            mItemWeatherView = (TextView) itemView.findViewById(R.id.tv_recycler_item);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String dayWeather = mWeatherData[adapterPosition];
            mClickHandlerObj.onClick(dayWeather);
        }
    }
}
