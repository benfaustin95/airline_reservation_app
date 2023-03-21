package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.MainActivity.AIRPORT;
import static edu.pdx.cs410J.bena2.MainActivity.DISPLAY_AIRLINE;
import static edu.pdx.cs410J.bena2.MainActivity.DISPLAY_AIRPORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

public class DisplayFlights extends AppCompatActivity {

    static final String AIRLINE = "airline";
    HashMap<String, Airline> airport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_flights);

        Intent received = getIntent();
        airport = (HashMap<String, Airline>) received.getSerializableExtra(AIRPORT);
        if(airport == null)
            airport = new HashMap<>();
    }

    public void makeDisplay(View view){
        String aName = getStringInput(R.id.aNameControl2);
        String source = getStringInput(R.id.sourceControl2);
        String dest = getStringInput(R.id.destControl2);
        Airline toDisplay = null;
       try {
           switch (validateGetParameters(aName, source, dest)) {
               case 1:
                   writeFlights(aName, 1);
                   break;
               case 2:
                   writeFlights(aName, 2,source, dest);
                   break;
               case 4:
                   writeAllFlights();
               default: break;
           }
       }catch (ParserException | IllegalArgumentException ex){
           Toast.makeText(this,"Error: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }
    public void returnToMain(){
        finish();
    }

    protected int validateGetParameters(String airline, String src, String dst) throws ParserException {

        if(airline == null && src == null && dst == null) return 4;

        if(airline == null)
            throw new ParserException("No airline provided with source and destination inputs");

        if(src == null && dst == null) return 1;

        if(src == null || dst == null)
            throw new ParserException((src==null?"Destination":"Source")+" " +
                    "provided with no "+(src==null?"Source":"Destinantion"));

        try{
            Flight.validateLocation(src, 0);
            Flight.validateLocation(dst, 1);

        }catch (IllegalArgumentException ex){
            throw new ParserException(ex.getMessage());
        }

        return 2;
    }

    protected void writeAllFlights() throws IllegalArgumentException{
        if(airport.isEmpty())
            throw new IllegalArgumentException("No Airlines currently stored in program");
        Intent toSend = new Intent(this, OutputAirport.class);
        toSend.putExtra(AIRPORT, airport);
        startActivity(toSend);
    }

    protected void writeFlights(String airline, int i, String ... strings) throws IllegalArgumentException{
        Airline toDisplay = airport.get(airline);
        Intent intent = new Intent(this, OutputAirline.class);
        if(toDisplay == null)
            throw new IllegalArgumentException("Airline "+airline+" does not exist");
        if(i == 2)
            toDisplay = new Airline(toDisplay, strings[0], strings[1]);

        if(toDisplay.getFlights().isEmpty()){
            if(i == 2)
                throw new IllegalArgumentException(String.format("%s contains no direct flights " +
                                "between %s(%s) and %s(%s)",toDisplay.getName(), strings[0], AirportNames.getName(strings[0].toUpperCase()),
                        strings[1], AirportNames.getName(strings[1].toUpperCase())));
            else
                throw new IllegalArgumentException("Airline: "+airline+" contains no flights");
        }

        intent.putExtra(AIRLINE, toDisplay);
        startActivity(intent);
    }

    public void returnToDisplayFlights(View view){
        finish();
    }
    private String getStringInput(int id){
        EditText text = findViewById(id);
        String toReturn = text.getText().toString();
        if(toReturn.equals("")) return null;
        return toReturn;
    }
}