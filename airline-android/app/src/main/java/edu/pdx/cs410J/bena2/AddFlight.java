package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.CardAdapter.setTextView;
import static edu.pdx.cs410J.bena2.MainActivity.AIRPORT;
import static edu.pdx.cs410J.bena2.MainActivity.DATA_ADDED;
import static edu.pdx.cs410J.bena2.MainActivity.FAILURE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddFlight extends AppCompatActivity {

    Airline toReturn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
    }


    public void addFlight(View view){
        String aName = getStringInput(R.id.aNameControl);
        String fNumber = getStringInput(R.id.fNumberControl);
        String source = getStringInput(R.id.destControl2);
        String sourceDate = getStringInput(R.id.sourceDateControl);
        String sourceTime = getStringInput(R.id.sourceTimeControl);
        String dest = getStringInput(R.id.destControl);
        String destDate = getStringInput(R.id.destDateControl);
        String destTime = getStringInput(R.id.destTimeControl);

        try{
            Flight toAdd =  new Flight(fNumber, source, dest, Flight.validateDateAndTime(sourceDate, sourceTime,0),
                Flight.validateDateAndTime(destDate, destTime, 1));
            toReturn = new Airline(aName,toAdd);
            View old = findViewById(R.id.InputView);
            old.setVisibility(View.GONE);
            setSuccessScreen(toAdd);
        }catch (IllegalArgumentException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSuccessScreen(Flight toAdd) {
        View success = findViewById(R.id.SuccessView);
        setTextView(toReturn.getName(), findViewById(R.id.aNameSuccessControl));
        setTextView(String.valueOf(toAdd.getNumber()), findViewById(R.id.FlightNumberSuccessControl));
        setTextView(toAdd.getFullSource(),findViewById(R.id.SourceSuccessControl));
        setTextView(toAdd.getDepartureString(),findViewById(R.id.DepartureSuccessControl));
        setTextView(toAdd.getFullDestination(),findViewById(R.id.DestinationSuccessControl));
        setTextView(toAdd.getArrivalString(),findViewById(R.id.ArrivalSuccessControl));
        success.setVisibility(View.VISIBLE);
    }


    public void returnToMain(){
        if(toReturn == null)
            setResult(FAILURE);
        else{
            Intent intent = new Intent();
            intent.putExtra(AIRPORT,toReturn);
            setResult(DATA_ADDED, intent);
        }
        finish();
    }

    public void returnToMain(View view){
        returnToMain();
    }

    private String getStringInput(int id){
        EditText text = findViewById(id);
        return text.getText().toString();
    }
}