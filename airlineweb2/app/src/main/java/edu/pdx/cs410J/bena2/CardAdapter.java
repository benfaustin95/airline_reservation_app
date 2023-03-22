package edu.pdx.cs410J.bena2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;

public class CardAdapter extends ArrayAdapter {
    List<Flight> toPrint;
    Context context;
    int item;

    public CardAdapter(Collection<Flight> toPrint, int resource, Context context) {
        super(context, resource, (List) toPrint);
        this.toPrint = (List) toPrint;
        this.item = resource;
        this.context = context;
    }

    @Override
    public int getCount(){
        return toPrint.size();
    }

    @Override
    public Object getItem(int index){
        return toPrint.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    item, parent, false);
        }

        Flight flight = toPrint.get(position);

        setTextView(String.valueOf(flight.getNumber()), convertView.findViewById(R.id.FNumber));
        setTextView(flight.getFullSource(), convertView.findViewById(R.id.Source));
        setTextView(flight.getDepartureString(), convertView.findViewById(R.id.Departure));
        setTextView(flight.getFullDestination(), convertView.findViewById(R.id.Destination));
        setTextView(flight.getArrivalString(), convertView.findViewById(R.id.Arrival));

        return convertView;
    }

    protected static void setTextView(String string, TextView viewById) {
        viewById.setText(string);
    }


}