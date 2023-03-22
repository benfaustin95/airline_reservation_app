package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.DisplayFlights.AIRLINE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.StackView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class OutputAirline extends AppCompatActivity {
    ListView prettyPrint;
    Airline airline;
    Collection<Airline> airport = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_airline2);
        prettyPrint = findViewById(R.id.airline_view);
        Intent intent = getIntent();
        List<Flight> toPrint;
        Integer type = intent.getSerializableExtra(DisplayFlights.REQUEST, Integer.class);

        if(type.equals(12)) {
            airline = getIntent().getSerializableExtra(DisplayFlights.AIRLINE, Airline.class);
            toPrint = (List<Flight>) airline.getFlights();
        }
        else {
            airport = ((HashMap<String,Airline>) getIntent().getSerializableExtra(MainActivity.AIRPORT)).values();
            toPrint = createFlightBundle(airport);
        }

        if(toPrint == null  && airport == null)
        {
            Toast.makeText(this, "No Airline available to print", Toast.LENGTH_SHORT).show();
            finish();
        }

        CardAdapter print = new CardAdapter(toPrint, R.layout.flight_card,
                OutputAirline.this);
        prettyPrint.setAdapter(print);

    }

    private List<Flight> createFlightBundle(Collection<Airline> airport) {
        List<Flight> toReturn = new ArrayList<>();
        for(Airline airline: airport){
            for(Flight flight: airline.getFlights()){
               toReturn.add(new FlightBundle(airline.getName(), flight));
            }
        }
        return toReturn;
    }


    public void returnToDisplayFlights(View view) {
        finish();
    }

    protected class FlightBundle extends Flight{
        String airlineName;

        public FlightBundle(String airlineName, Flight flight){
            super(flight);
            this.airlineName = airlineName;
        }

        public String getAirlineName() {
            return airlineName;
        }
    }
}