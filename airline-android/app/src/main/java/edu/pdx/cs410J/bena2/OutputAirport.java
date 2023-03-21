package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.DisplayFlights.AIRLINE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class OutputAirport extends AppCompatActivity {

    Airline toPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_airport);

    }

}