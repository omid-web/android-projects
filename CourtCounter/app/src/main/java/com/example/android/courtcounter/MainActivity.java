package com.example.android.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.attr.button;

/**
 * This activity keeps track of 2 basketball teams
 */
public class MainActivity extends AppCompatActivity {

    int scoreTeamA = 0;
    int scoreTeamB = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonListener();
    }

    public void buttonListener() {
        Button teamApoints3 = (Button) findViewById(R.id.team_a_points_3);
        Button teamApoints2 = (Button) findViewById(R.id.team_a_points_2);
        Button teamAfreeThrow = (Button) findViewById(R.id.team_a_free_throw);

        teamApoints3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamA += 3;
                displayForTeamA(scoreTeamA);
            }
        });
        teamApoints2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamA += 2;
                displayForTeamA(scoreTeamA);
            }
        });
        teamAfreeThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamA += 1;
                displayForTeamA(scoreTeamA);
            }
        });

        Button teamBpoints3 = (Button) findViewById(R.id.team_b_points_3);
        Button teamBpoints2 = (Button) findViewById(R.id.team_b_points_2);
        Button teamBfreeThrow = (Button) findViewById(R.id.team_b_free_throw);

        teamBpoints3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamB += 3;
                displayForTeamB(scoreTeamB);
            }
        });
        teamBpoints2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamB += 2;
                displayForTeamB(scoreTeamB);
            }
        });
        teamBfreeThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamB += 1;
                displayForTeamB(scoreTeamB);
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreTeamA = 0;
                scoreTeamB = 0;
                displayForTeamA(scoreTeamA);
                displayForTeamB(scoreTeamB);
            }
        });
    }

    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }


    /**
     * inflates the menu, adds items to the action bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) return true;

        return super.onOptionsItemSelected(item);
    }
}
