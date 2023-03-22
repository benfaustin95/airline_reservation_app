package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.AddAirline.makePopUp;
import static edu.pdx.cs410J.bena2.CardAdapter.setTextView;
import static edu.pdx.cs410J.bena2.MainActivity.AIRPORT;
import static edu.pdx.cs410J.bena2.MainActivity.DATA_ADDED;
import static edu.pdx.cs410J.bena2.MainActivity.FAILURE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class AddFlight extends AppCompatActivity {

    Airline toReturn = null;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialog2;

    TimePickerDialog timePickerDialog;
    TimePickerDialog timePickerDialog2;
    Button timeButtondep;
    Button timeButtonarr;
    Button dateButtondest;
    Button dateButtonarr;
    String date = null;
    int hour;
    int min;
    String period;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);


        datePickerDialog = initDatePicker(1);
        datePickerDialog2 = initDatePicker(2);
        dateButtondest = findViewById(R.id.destDateButton);
        dateButtonarr = findViewById(R.id.srcDateButton);
        dateButtondest.setText(date);
        dateButtonarr.setText(date);

        timeButtondep = findViewById(R.id.srcTimeButton);
        timeButtonarr = findViewById(R.id.destTimeButton);
        timeButtonarr.setText(makeCurrentTime());
        timeButtondep.setText(makeCurrentTime());
    }



    public void addFlight(View view){
        String aName = getStringInput(R.id.aNameControl);
        String fNumber = getStringInput(R.id.fNumberControl);
        String source = getStringInput(R.id.destControl2);
        String sourceDate = dateButtonarr.getText().toString();
        String sourceTime = timeButtondep.getText().toString();
        String dest = getStringInput(R.id.destControl);
        String destDate = dateButtondest.getText().toString();
        String destTime = timeButtonarr.getText().toString();

        try{
            Flight toAdd =  new Flight(fNumber, source, dest, Flight.validateDate(sourceDate+" "+sourceTime,0),
                Flight.validateDate(destDate+" "+destTime, 1));
            toReturn = new Airline(aName,toAdd);
            View old = findViewById(R.id.InputView);
            old.setVisibility(View.GONE);
            setSuccessScreen(toAdd);
        }catch (IllegalArgumentException ex){
            makePopUp(view, ex.getMessage(), Snackbar.LENGTH_SHORT);
        }
    }

    private void setSuccessScreen(Flight toAdd) {
        View success = findViewById(R.id.SuccessView);
        CardAdapter.setTextView(toReturn.getName(), findViewById(R.id.aNameSuccessControl));
        CardAdapter.setTextView(String.valueOf(toAdd.getNumber()), findViewById(R.id.FlightNumberSuccessControl));
        CardAdapter.setTextView(toAdd.getFullSource(),findViewById(R.id.SourceSuccessControl));
        CardAdapter.setTextView(toAdd.getDepartureString(),findViewById(R.id.DepartureSuccessControl));
        CardAdapter.setTextView(toAdd.getFullDestination(),findViewById(R.id.DestinationSuccessControl));
        CardAdapter.setTextView(toAdd.getArrivalString(),findViewById(R.id.ArrivalSuccessControl));
        success.setVisibility(View.VISIBLE);
    }


    public void returnToMain(){
        if(toReturn == null)
            setResult(MainActivity.FAILURE);
        else{
            Intent intent = new Intent();
            intent.putExtra(MainActivity.AIRPORT,toReturn);
            setResult(MainActivity.DATA_ADDED, intent);
        }
        finish();
    }

    public void helpMe(View view) { startActivity(new Intent(this, ReadMe.class));
    }
    public void returnToMain(View view){
        returnToMain();
    }

    private String getStringInput(int id){
        Object text = findViewById(id);
        if(text.getClass() == Button.class)
            return ((Button)text).getText().toString();
        return ((EditText)text).getText().toString();
    }


    private String makeCurrentTime(){
            Date temp = new Date();
            String am = "AM";

            int min = temp.getMinutes();
            int hour = temp.getHours();

            if(hour>=12) am = "PM";
            if(hour>12) hour = hour%13+1;
            if(min <10)
                return hour+":"+"0"+min+" "+am;
            return hour+":"+min+" "+am;

    }

    private String makeTime() {
        String minString;
        if(min<10) minString = "0"+min+" "+period;
        else minString = ""+min+" "+period;

        return hour+":"+minString;
    }

    public void openTimeArrPicker(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {


                hour = i;
                if(i>=12) period = "PM";
                else period = "AM";
                if(hour == 0) hour = 12;
                if(hour>12) hour = (i%13)+1;
                min = i1;

                timeButtonarr.setText(makeTime());
            }
        };

        TimePickerDialog toReturn = new TimePickerDialog(this, timeSetListener,hour, min, false);
        toReturn.setTitle("Select Departure Time");
        toReturn.show();
    }

    public void openTimeDepPicker(View view) {

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {


                hour = i;
                if(i>=12) period = "PM";
                else period = "AM";
                if(hour == 0) hour = 12;
                if(hour>12) hour = (i%13)+1;
                min = i1;

                timeButtondep.setText(makeTime());
            }
        };

        TimePickerDialog toReturn = new TimePickerDialog(this, timeSetListener,hour, min, false);
        toReturn.setTitle("Select Departure Time");
        toReturn.show();
    }

    //Date Logic
    private DatePickerDialog initDatePicker(int type) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1+=1;
                String date = makeDateString(i2,i1,i);
                if(type == 1)
                    dateButtonarr.setText(date);
                else
                    dateButtondest.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();

        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH);
        int y = cal.get(Calendar.YEAR);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        date = makeDateString(d, m+1, y);

        return new DatePickerDialog(this, style, dateSetListener, y, m, d);
    }

    private String makeDateString(int i2, int i1, int i) {
        return i1+"/"+i2+"/"+i;
    }

    public void openDateArrPicker(View view) {

        datePickerDialog.show();
    }

    public void openDateDepPicker(View view) {

        datePickerDialog2.show();
    }
}