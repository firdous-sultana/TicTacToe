package com.example.android.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //member variables
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true; //we start with player 1 --> X

    private int roundCount;

    private int player1Point;
    private int player2Point;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting references to our text view
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        //assigning our 'buttons' array with references to all the Buttons
        //using nested loop instead of doing it one-by-one
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                //assigning our buttons
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //checking if the button that was clicked doesn't contain an empty string
        if(!((Button) v).getText().toString().equals("")){
            return;
        }
        if(player1Turn){
            ((Button) v).setText("X");
        }
        else{
            ((Button) v).setText("O");
        }
        //incrementing the roundcount so we know that a round is over
        roundCount++;

        if(checkForWin()){
            if(player1Turn){
                player1Wins();
            }
            else{
                player2Wins();
            }
        }
        else if (roundCount == 9){
            draw();
        }
        else{
            //switching the turns
            player1Turn = !player1Turn;
        }
    }

    //checking for winner
    private boolean checkForWin(){
        String[][] field = new String[3][3];
        //getting the button text into our string array
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        //going through all our rows in the String array
        for(int i = 0; i<3; i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                return true;
            }
        }

        //going through the column instead of the rows
        for(int i = 0; i<3; i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
        }

        //checking for diagonal
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            return true;
        }

        //checking for another diagonal
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            return true;
        }
        return false;
    }

    private void player1Wins(){
        player1Point++;
        Toast.makeText(this, "Player 1 won!", Toast.LENGTH_SHORT).show();

        //updating our text views so that they show updated amount of player points
        updatePointsText();

        //reset our board to start a new round
        resetBoard();
    }
    private void player2Wins(){
        player2Point++;
        Toast.makeText(this, "Player 2 won!", Toast.LENGTH_SHORT).show();

        updatePointsText();
        resetBoard();
    }
    private void draw(){
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText(){
        //taking the textview references and updating them
        textViewPlayer1.setText("Player 1: " + player1Point);
        textViewPlayer2.setText("Player 2: " + player2Point);
    }
    private void resetBoard(){
        //reset all our buttons to empty string
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame(){
        //setting all the points to 0 and resetting the board
        player1Point = 0;
        player2Point = 0;
        updatePointsText();
        resetBoard();
    }

    //handeling the rotation of device:


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Point", player1Point);
        outState.putInt("player2Point", player2Point);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Point = savedInstanceState.getInt("player1Point");
        player2Point = savedInstanceState.getInt("player2Point");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}