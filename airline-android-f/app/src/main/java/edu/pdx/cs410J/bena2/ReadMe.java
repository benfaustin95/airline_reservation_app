package edu.pdx.cs410J.bena2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadMe extends AppCompatActivity {

    StringBuilder rMe;
    TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);
        out = findViewById(R.id.about_text_view);
    }

    public void readMe(View view){

        String line;
        rMe = new StringBuilder();
        try(InputStream is = this.getResources().openRawResource(R.raw.read_me)){
            if(is == null)
                throw new IOException();
            BufferedReader sr = new BufferedReader(new InputStreamReader(is));
            while((line = sr.readLine())!=null){
                rMe.append(line).append("\n");
            }
        } catch (IOException e) {
            rMe.append("Read Me Not Available");
        }
        out.setText(rMe.toString());
    }


    public void ussage(View view){

        String line;
        rMe = new StringBuilder();
        try(InputStream is = this.getResources().openRawResource(R.raw.ussage)){
            if(is == null)
                throw new IOException();
            BufferedReader sr = new BufferedReader(new InputStreamReader(is));
            while((line = sr.readLine())!=null){
                rMe.append(line).append("\n");
            }
        } catch (IOException e) {
            rMe.append("Ussage Not Available");
        }
        out.setText(rMe.toString());
    }

    public void returnToMain(View view){
        finish();
    }
}