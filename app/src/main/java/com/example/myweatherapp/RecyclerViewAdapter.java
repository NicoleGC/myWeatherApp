package com.example.myweatherapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.myweatherapp.utilities.DateFormatter;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{



private Cursor mCursor;
private  Context mContext;

private AdapterOnClickHandler mClickHandlerObj;

/*Interface to be used to handle clicks*/
public interface AdapterOnClickHandler{
    void onClick(long dayWeather);
}
/**/

    public RecyclerViewAdapter(AdapterOnClickHandler clickedHandled, Context context){
            mClickHandlerObj = clickedHandled;
            mContext=context;
    }


   void swapCursor(Cursor newCursor){
       mCursor=newCursor;
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
          mCursor.moveToPosition(position);

          //weather info will be but in a weather summary string
        String weatherInfo;

        //extract the info from the cursor
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_DATE);
        String dateReadable = DateFormatter.getFriendlyDateString(mContext,dateInMillis,false);

        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_ID);
        //with the weather id, you can get the description
        String description= mCursor.getString(MainActivity.INDEX_DESCRIPTION);
        double highTemperature = mCursor.getDouble(MainActivity.INDEX_MAX_TEMP);
        double lowTemperature = mCursor.getDouble(MainActivity.INDEX_MIN_TEMP);

        //now put all the information together in one String
        String summary  = (dateReadable+ " - "+ description+" - "+ Math.round(highTemperature) + " / " + Math.round(lowTemperature));

        //display the summary
        holder.mItemWeatherView.setText(summary);






    }

    @Override
    public int getItemCount() {
        if(mCursor==null){
            return 0;
        }
        return mCursor.getCount();
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
            int adapterPos = getAdapterPosition();
            mCursor.moveToPosition(adapterPos);
            long date = mCursor.getLong(MainActivity.INDEX_DATE);
            mClickHandlerObj.onClick(date);
        }
    }
}
