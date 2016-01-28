package com.example.gabriel.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FilmActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        TextView[] textViews = new TextView[6];
        textViews[0] = (TextView) findViewById(R.id.MovieTitle);
        textViews[1] = (TextView) findViewById(R.id.Year);
        textViews[2] = (TextView) findViewById(R.id.Director);
        textViews[3] = (TextView) findViewById(R.id.Actors);
        textViews[4] = (TextView) findViewById(R.id.Genre);
        textViews[5] = (TextView) findViewById(R.id.Plot);

        ImageView Poster = (ImageView)findViewById(R.id.Poster);
        RatingBar rating = (RatingBar)findViewById(R.id.RatingBar);

        //String json = "{\"Title\":\"The Lord of the Rings\",\"Year\":\"1978\",\"Rated\":\"PG\",\"Released\":\"15 Nov 1978\",\"Runtime\":\"132 min\",\"Genre\":\"Animation, Adventure, Fantasy\",\"Director\":\"Ralph Bakshi\",\"Writer\":\"Chris Conkling (screenplay), Peter S. Beagle (screenplay), J.R.R. Tolkien (novels)\",\"Actors\":\"Christopher Guard, William Squire, Michael Scholes, John Hurt\",\"Plot\":\"The Fellowship of the Ring embark on a journey to destroy the One Ring and end Sauron's reign over Middle-earth.\",\"Language\":\"English, Sindarin\",\"Country\":\"USA\",\"Awards\":\"Nominated for 1 Golden Globe. Another 1 win & 2 nominations.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTcxMzU2MDc5Ml5BMl5BanBnXkFtZTcwMjUxNDcyMg@@._V1_SX300.jpg\",\"Metascore\":\"N/A\",\"imdbRating\":\"6.1\",\"imdbVotes\":\"23,385\",\"imdbID\":\"tt0077869\",\"Type\":\"movie\",\"Response\":\"True\"}";

        while(!MainActivity.done){}

        String json = MainActivity.json;

        if (json != null) {
            GetMovieData gmd = new GetMovieData(textViews, Poster, rating);
            gmd.execute(json);
        } else {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Erro");
            alert.setMessage("Filme não encontrado");
            alert.show();
        }
    }

}
