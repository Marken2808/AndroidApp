package com.example.threecards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ci1, ci2, ci3;
    private Button btnResult, btnAgain;
    private TextView txtResult;
    private String[] cardTypes = {"spades","clubs","diamonds","hearts"};
    private int[] cardNums = {1,2,3,4,5,6,7,8,9,10,11,12,13};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        ci1 = findViewById(R.id.cardImg1);
        ci2 = findViewById(R.id.cardImg2);
        ci3 = findViewById(R.id.cardImg3);
        btnResult = findViewById(R.id.buttonResult);
        btnAgain = findViewById(R.id.buttonAgain);
        txtResult = findViewById(R.id.textResult);

        View.OnClickListener tappingCard = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int rdTypes = randomGen(cardTypes.length);
                int rdNums = randomGen(cardNums.length);

                        String cardValue = cardTypes[rdTypes]+"_"+cardNums[rdNums];

                showToast(cardValue.toString());

            }
        };

        ci1.setOnClickListener(tappingCard);
        ci2.setOnClickListener(tappingCard);
        ci3.setOnClickListener(tappingCard);


    }

    public int randomGen(int length){
        Random rd = new Random();
        return rd.nextInt(length);
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}