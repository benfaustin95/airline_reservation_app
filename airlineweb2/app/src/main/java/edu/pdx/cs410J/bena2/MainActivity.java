package edu.pdx.cs410J.bena2;

import static edu.pdx.cs410J.bena2.AddAirline.makePopUp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        try{
                readFromStorage();
            }catch (IOException ex){
                if(!(ex.getClass() == FileNotFoundException.class))
                    makePopUp(findViewById(R.id.content),"While loading from storage: "+ex.getMessage(), Snackbar.LENGTH_SHORT);
            }
        this.setTitle("Airline Home");
    }

    private void readFromStorage() throws IOException{
        File toRead = getStorage();
        try(FileInputStream fi = new FileInputStream(toRead);
            ObjectInputStream os = new ObjectInputStream(fi)){
            while(true){
                Airline temp = (Airline) os.readObject();
                airport.put(temp.getName(), temp);
            }
        } catch (EOFException | ClassCastException | FileNotFoundException ignored){
        } catch(IOException | ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }


    public void launchAddAirline(View view){
        Intent toSend = new Intent(this, AddAirline.class);
        toSend.putExtra(AIRPORT,this.airport);
        startActivityForResult(toSend, GET_AIRLINE);
    }


    @Override
    public void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);

        if(data == null) return;

        if(result == DATA_ADDED && request == GET_AIRLINE)
            mergeAirport(data);
        else if(result == DATA_ADDED && request == GET_FLIGHT)
            addAirline(data);
        saveAirport();

    }

    private void saveAirport() {
        File toSave = getStorage();
        if(!airport.isEmpty()){
            try(FileOutputStream fo = new FileOutputStream(toSave);
                ObjectOutputStream os = new ObjectOutputStream(fo)){
                for(Airline airline: airport.values()){
                    os.writeObject(airline);
                }
            } catch (IOException e) {
            }
        }
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
            makePopUp(findViewById(R.id.content), "Error saving Flight and/or Airline", Snackbar.LENGTH_SHORT);
        }
    }

    protected void mergeAirport(Intent data) {
        Object temp = data.getSerializableExtra(AIRPORT);
        try {
            airport = (HashMap<String, Airline>) temp;
        }catch(ClassCastException ex)
        {
            makePopUp(findViewById(R.id.content), "Error saving Airline", Snackbar.LENGTH_SHORT);
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

    public void helpMe(View view) {
        startActivity(new Intent(this, ReadMe.class));
    }

    @Override
    public void onStop() {
        super.onStop();
        saveAirport();
    }

    protected File getStorage(){
        File temp = getFilesDir();
        return new File(temp, "airline.xml");
    }
}