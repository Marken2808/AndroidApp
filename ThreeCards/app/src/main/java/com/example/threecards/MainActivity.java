package com.example.threecards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView ci1, ci2, ci3;
    private Button btnAgain;
    private TextView txtResult;
    private String[] cardTypes = {"spades","clubs","diamonds","hearts"};
    private int[] cardNums = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    private ArrayList<String> openedCards;

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
        btnAgain = findViewById(R.id.buttonAgain);
        txtResult = findViewById(R.id.textResult);
        openedCards = new ArrayList<>();

        View.OnClickListener tappingCard = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                ImageView imgView = (ImageView) v;

                int rdTypes = randomGen(cardTypes.length);
                int rdNums = randomGen(cardNums.length);
                String cardValue = cardTypes[rdTypes]+"_"+cardNums[rdNums];


                while(openedCards.contains(cardValue)){
                    rdTypes = randomGen(cardTypes.length);
                    rdNums = randomGen(cardNums.length);
                    cardValue = cardTypes[rdTypes]+"_"+cardNums[rdNums];

                }
                openedCards.add(cardValue);
                int resID = getResources().getIdentifier(cardValue, "drawable", getPackageName());
                imgView.setImageResource(resID);

                imgView.setClickable(false);
                txtResult.setText(showResult());

            }
        };

        ci1.setOnClickListener(tappingCard);
        ci2.setOnClickListener(tappingCard);
        ci3.setOnClickListener(tappingCard);

    }

    public int getScore(String cardName){
        String[] parts = cardName.split("_");
        int value = Integer.parseInt(parts[1]);
        if(value>=10){
            value=0;
        }
        return value;
    }
    public String showResult() {
        if(openedCards.size() == 3){
            int score=0;
            Iterator<String> itr = openedCards.iterator();
            while(itr.hasNext()){
                score+=getScore(itr.next());
            }
            return "Score: "+score%10;
        }
        return "More!";
    }

    public void playAgain(View v){
        ci1.setImageResource(R.drawable.empty_card);
        ci1.setClickable(true);
        ci2.setImageResource(R.drawable.empty_card);
        ci2.setClickable(true);
        ci3.setImageResource(R.drawable.empty_card);
        ci3.setClickable(true);
        txtResult.setText("Score: 0");
        openedCards.clear();
    }

    public int randomGen(int length){
        Random rd = new Random();
        return rd.nextInt(length);
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}