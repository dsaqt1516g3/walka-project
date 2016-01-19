package edu.upc.eetac.dsa.walka.client.entity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.upc.eetac.dsa.walka.R;

/**
 * Created by Marta_ on 06/01/2016.
 */
public class EventCollectionAdapter extends BaseAdapter{

    private EventCollection events;
    private LayoutInflater layoutInflater;
    private final static String TAG =EventCollectionAdapter.class.toString();

    public EventCollectionAdapter(Context context, EventCollection eventCollection){
        layoutInflater = LayoutInflater.from(context);
        this.events = eventCollection;
    }

    @Override
    public int getCount() {
        return events.getEvents().size();
    }

    @Override
    public Object getItem(int position) {
        return events.getEvents().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "entro en el getView");
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_events, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String ds = null;
        String de = null;
        String hs = null;
        String he = null;

        String Subject = events.getEvents().get(position).getTitle();
        String color = events.getEvents().get(position).getColor();
        if (color != null)
        {
            convertView.setBackgroundColor(Color.parseColor(color));
        }
        Log.d(TAG, Subject);
        String date1 = events.getEvents().get(position).getStart();
            String[] parts = date1.split(" ");
            ds = parts[0];
            hs = parts[1];
            Log.d(TAG, "if con espacio: "+ds + hs);

        String date2 = events.getEvents().get(position).getEnd();

            String[] parts2 = date2.split(" ");
            de = parts2[0];
            he = parts2[1];
            Log.d(TAG, "if con espacio: "+de + he);


        viewHolder.textViewSubject.setText(Subject);
        viewHolder.textViewDateStart.setText(ds);
        viewHolder.textViewHourStart.setText(hs);
        viewHolder.textViewDateEnd.setText(de);
        viewHolder.textViewHourEnd.setText(he);
        return convertView;
    }

    class ViewHolder{
        TextView textViewSubject;
        TextView textViewDateStart;
        TextView textViewDateEnd;
        TextView textViewHourStart;
        TextView textViewHourEnd;

        ViewHolder(View row){
            this.textViewSubject = (TextView) row.findViewById(R.id.textViewSubject);
            this.textViewDateStart = (TextView) row.findViewById(R.id.start_date);
            this.textViewDateEnd = (TextView) row.findViewById(R.id.end_date);
            this.textViewHourStart = (TextView) row.findViewById(R.id.start_hour);
            this.textViewHourEnd = (TextView) row.findViewById(R.id.end_hour);
        }
    }
}
