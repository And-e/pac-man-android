package com.johnson.andy.pacmangame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean playedYet = false;
    final static int UP = 1;
    final static int LEFT = 2;
    final static int RIGHT = 3;
    final static int DOWN = 4;
   // long lastUpdate = 0;
    int[][] boardLevel1;
    int ghostFourDirection = 1;
    int ghostOneDirection = 1;
    int ghostTwoDirection = 1;
    int ghostThreeDirection = 1;
    int currentDirection = 3;
    boolean stopped = true;
    final Handler clockHandler = new Handler();
    final int TIME_INTERVAL = 100;
    AudioManager am;
    int numGhosts;
    int addGhosts;
    MediaPlayer mp;
    MediaPlayer bg;
    String startStopText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = (AudioManager)getSystemService(AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2, 0);

        Button button = (Button)findViewById(R.id.startStopBtn);
        button.setText("Start");
        button.setOnClickListener(new View.OnClickListener() {

            Runnable clockTimer = new Runnable() {
                @Override
                public void run() {
                    if(!stopped)
                    {
                        GameBoard board = (GameBoard)findViewById(R.id.gameBoard);
                        boardLevel1 = board.getBoardLevelOne();

                        TextView level = (TextView)findViewById(R.id.level);

                        String levelText = "Level: " + board.getCurrentLevel();
                        level.setText(levelText);
                        TextView cakesLeft = (TextView)findViewById(R.id.cakesLeft);

                        String cakesText = "Cakes left: " + board.getCakeCount();
                        cakesLeft.setText(cakesText);
                        if(gameOver())
                        {

                            stopped = true;
                            Button button = (Button)findViewById(R.id.startStopBtn);
                            clockHandler.removeCallbacks(clockTimer);
                            if(board.getCurrentLevel() == 1)
                            {
                                intermission();
                                board.goLevelTwo();
                                button.setText("Start");
                            }
                            else
                            {
                                endGame();
                                button.setText("Winner!");
                            }



                        }
                        else if(board.hitGhost())
                        {
                            stopped = true;
                            clockHandler.removeCallbacks(clockTimer);
                            youLost();
                            board.restart();
                            Button button = (Button)findViewById(R.id.startStopBtn);
                            button.setText("Try again");
                        }
                        else
                        {
                            board.update(currentDirection);
                            clockHandler.postDelayed(this,100);

                        }


                    }
                    }


            };

            @Override
            public void onClick(View v) {
                if(!playedYet)
                {
                    playBackgroundMusic();
                    playedYet = true;
                }

                Button button = (Button)findViewById(R.id.startStopBtn);
                stopped = !stopped;
                if(!stopped){
                    button.setText("Pause");
                    clockTimer.run();
                    resumeBgMusic();
                }
                else
                {
                    button.setText("Start");
                    clockHandler.removeCallbacks(clockTimer);
                    pauseBgMusic();
                }
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                GameBoard board = (GameBoard)findViewById(R.id.gameBoard);
                board.cheat();
                return true;
            }
        });
    }

    public boolean gameOver()
    {
        GameBoard board = (GameBoard)findViewById(R.id.gameBoard);
        if(board.getCakeCount() == 0)
        {

            pauseBgMusic();
            if(mp != null) mp.release();
            mp = MediaPlayer.create(getApplicationContext(), R.raw.win);
            mp.start();
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void intermission()
    {
        Toast.makeText(this, "On to Level 2...", Toast.LENGTH_SHORT).show();
    }

    public void endGame()
    {
        Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Ashman Project, Fall 2016, Andrew W Johnson",
                    Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        else if(id == R.id.preferences)
        {
            stopped = true;

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void onLeftClick(View view)
    {
        currentDirection = LEFT;
    }

    public void onDownClick(View view)
    {
        currentDirection = DOWN;
    }

    public void onRightClick(View view)
    {
        currentDirection = RIGHT;
    }

    public void onUpClick(View view)
    {
        currentDirection = UP;
    }

    public int randomNumberBetween(int min, int max, int not)
    {
        {
            Random rand = new Random();
            int randomInt = 5;

                randomInt = rand.nextInt((max - min + 1)) + min;  // <--- this line from Malcolm


            return(randomInt);
        }
    }

    public void youLost()
    {
        if(mp != null) mp.release();
        pauseBgMusic();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.lose);
        mp.start();
        Toast.makeText(this, "Ouch! Better luck next time.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        numGhosts = Integer.parseInt(prefs.getString("number_ghosts", "2"));
        addGhosts = Integer.parseInt(prefs.getString("additional_ghosts", "2"));
        System.out.println(numGhosts + " " + addGhosts);
        GameBoard board = (GameBoard)findViewById(R.id.gameBoard);
        board.updateGhosts(numGhosts,addGhosts);
    }


    public void onStop()
    {
        super.onStop();
        stopped = true;
        Button button = (Button)findViewById(R.id.startStopBtn);
        button.setText("Start");
    }


    @Override
    public void onPause()
    {
        super.onPause();
        stopped = true;
        pauseBgMusic();
        Button button = (Button)findViewById(R.id.startStopBtn);
        button.setText("Start");
    }

    public void resumeBgMusic()
    {
        if(bg != null && !bg.isPlaying()) bg.start();
    }


    public void pauseBgMusic()
    {
        if(bg != null) bg.pause();
    }

    public void playBackgroundMusic()
    {
        if(bg != null) bg.release();
        bg = MediaPlayer.create(getApplicationContext(), R.raw.background);
        bg.setVolume(0.1f,0.1f);
        bg.start();
    }

}
