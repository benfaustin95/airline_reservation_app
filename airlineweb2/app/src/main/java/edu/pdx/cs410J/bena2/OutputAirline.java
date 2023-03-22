package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.DisplayFlights.AIRLINE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.StackView;
import android.widget.Toast;

import java.util.List;

public class OutputAirline extends AppCompatActivity {
    ListView prettyPrint;
    Airline toPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_airline2);


        toPrint = getIntent().getSerializableExtra(DisplayFlights.AIRLINE, Airline.class);

        if(toPrint == null)
        {
            Toast.makeText(this, "No Airline available to print", Toast.LENGTH_SHORT).show();
            finish();
        }

        prettyPrint = findViewById(R.id.airline_view);

        CardAdapter print = new CardAdapter(toPrint.getFlights(), R.layout.flight_card,
                OutputAirline.this);
        prettyPrint.setAdapter(print);
    }


    public void returnToDisplayFlights(View view) {
        finish();
    }
}