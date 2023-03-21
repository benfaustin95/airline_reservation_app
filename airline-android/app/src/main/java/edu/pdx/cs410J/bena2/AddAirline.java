package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.MainActivity.AIRPORT;
import static edu.pdx.cs410J.bena2.MainActivity.DATA_ADDED;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddAirline extends AppCompatActivity {
    String name = null;
    HashMap<String, Airline> airport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_airline);

        Intent received = getIntent();
        airport = (HashMap<String, Airline>) received.getSerializableExtra(AIRPORT);
        if(airport == null)
            airport = new HashMap<>();
    }

    public void addAirline(View view){
        try {
            EditText airlineName = findViewById(R.id.aName);
            String aName = airlineName.getText().toString();
            if (airport.containsKey(aName))
                throw new IllegalArgumentException("Airline: "+aName+" already exists");
            else {
                airport.put(aName, new Airline(aName));
                Toast.makeText(this, "Airline: "+aName+" has been added", Toast.LENGTH_SHORT).show();
                airlineName.getText().clear();
            }
        } catch(IllegalArgumentException ex){
            Toast.makeText(this,ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void returnToMain(View view){
        Intent toReturn = new Intent();
        toReturn.putExtra(AIRPORT, airport);
        setResult(DATA_ADDED, toReturn);
        finish();
    }
}