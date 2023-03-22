package edu.pdx.cs410J.bena2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReadMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);

        TextView out = findViewById(R.id.about_text_view);

        out.setText(R.string.READ_ME);
    }

    public void returnToMain(View view){
        finish();
    }
}