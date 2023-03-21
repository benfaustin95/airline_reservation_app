package edu.pdx.cs410J.bena2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static final String AIRPORT ="airport";
    static final int DATA_ADDED = 200 ;
    static final int GET_AIRLINE = 42;
    static final int GET_FLIGHT = 43 ;
    static final int FAILURE = 404;
    static  final int DISPLAY_AIRPORT = 44;
    static final int DISPLAY_AIRLINE = 45;
    HashMap<String, Airline> airport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        airport = new HashMap<>();
    }


    public void launchAddAirline(View view){
        Intent toSend = new Intent(this, AddAirline.class);
        toSend.putExtra(AIRPORT,this.airport);
        startActivityForResult( toSend, GET_AIRLINE);
    }


    @Override
    public void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);

        if(data == null) return;

        if(result == DATA_ADDED && request == GET_AIRLINE)
            mergeAirport(data);
        else if(result == DATA_ADDED && request == GET_FLIGHT)
            addAirline(data);

    }

    protected void addAirline(Intent data) {
        Object temp = data.getSerializableExtra(AIRPORT);
        try{
            Airline toAdd = (Airline) temp;
            if(!airport.containsKey(toAdd.getName()))
                airport.put(toAdd.getName(), toAdd);
            else
                airport.get(toAdd.getName()).addFlights(toAdd.getFlights());
        }catch (ClassCastException ex)
        {
            Toast.makeText(this, "Error saving Flight and/or Airline",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void mergeAirport(Intent data) {
        Object temp = data.getSerializableExtra(AIRPORT);
        try {
            airport = (HashMap<String, Airline>) temp;
        }catch(ClassCastException ex)
        {
            Toast.makeText(this, "Error saving Airline", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchAddFlight(View view){
        startActivityForResult(new Intent(this, AddFlight.class), GET_FLIGHT);
    }

    public void launchDisplayFlights(View view){
        Intent toSend = new Intent(this, DisplayFlights.class);
        toSend.putExtra(AIRPORT, this.airport);
        startActivity(toSend);
    }
}