package com.example.quotationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView txt;
    ArrayList<String> listQuotation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        txt = findViewById(R.id.txtResult);
        btn = findViewById(R.id.btnResult);
        listQuotation.add("welcome to my world");
        listQuotation.add("hello my name is marken");
        listQuotation.add("good morning have a nice day");
        listQuotation.add("see you later");
    }

    public void showQuotation(View v) {

        if(v.getId() == R.id.btnResult){
            Random rd =  new Random();

            switch (rd.nextInt(listQuotation.size())){
                case 0:
                    txt.setGravity(Gravity.CENTER);
                    break;
                case 1:
                    txt.setGravity(Gravity.LEFT);
                    break;
                case 2:
                    txt.setGravity(Gravity.RIGHT);
                    break;
                case 3:
                    txt.setGravity(Gravity.BOTTOM);
                    break;
            }
            txt.setText(listQuotation.get(rd.nextInt(listQuotation.size())).toString() + "");
        }
    }
}