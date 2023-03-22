package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.MainActivity.AIRPORT;
import static edu.pdx.cs410J.bena2.MainActivity.DATA_ADDED;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class AddAirline extends AppCompatActivity {
    String name = null;
    HashMap<String, Airline> airport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_airline);

        Intent received = getIntent();
        airport = (HashMap<String, Airline>) received.getSerializableExtra(MainActivity.AIRPORT);
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
                airlineName.getText().clear();
                makePopUp(view, "Airline: "+aName+" has been added", Snackbar.LENGTH_SHORT);
            }
        } catch(IllegalArgumentException ex){
            makePopUp(view, ex.getMessage(), Snackbar.LENGTH_SHORT);
        }
    }

    public static void makePopUp(View view, String message, int length){
            Snackbar.make(view, message, length).show();
    }
    public void helpMe(View view) { startActivity(new Intent(this, ReadMe.class));
    }
    public void returnToMain(View view){
        Intent toReturn = new Intent();
        toReturn.putExtra(MainActivity.AIRPORT, airport);
        setResult(MainActivity.DATA_ADDED, toReturn);
        finish();
    }
}