package com.example.gabriel.test;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetMovieData extends AsyncTask<String, Void, Bitmap>{

    private TextView[] textViews;
    private ImageView Poster;
    private RatingBar rating;

    private Map<String, String> map = new HashMap<String, String>();
    private ArrayList<String> JsonResult = new ArrayList<>();

    public GetMovieData(TextView[] textViews, ImageView Poster, RatingBar ratingBar){
        this.textViews = textViews;
        this.Poster = Poster;
        rating = ratingBar;
    }

    @Override
    protected Bitmap doInBackground(String... json) {
        Bitmap image = null;
        try {
            ReadJson(json[0]);

            if(map.size() > 2) {
                if (!map.get("Poster").equals("N/A")) {
                    URL url = new URL(map.get("Poster"));
                    InputStream stream = url.openStream();
                    image = BitmapFactory.decodeStream(stream);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return image;
    }

    protected void onPostExecute(Bitmap result){
        textViews[0].setText(map.get("Title"));//Titulo
        textViews[1].setText(map.get("Released"));//Lancamento
        textViews[2].setText(map.get("Director"));//Diretor
        textViews[3].setText(map.get("Actors"));//Atores
        textViews[4].setText(map.get("Genre"));//Genero
        textViews[5].setText(map.get("Plot"));//Plot

        if(result != null)
            Poster.setImageBitmap(result);

        String number = map.get("imdbRating");
        int stars = Math.round(Float.parseFloat(number));
        rating.setNumStars(stars);
    }

    private void ReadJson(String json){
        String[] objetos = json.split("\"");
        String linha = "";

        for(int i=0; i<objetos.length; i++){
            if(!objetos[i].equals("{") && !objetos[i].equals("}")){
                if(!objetos[i].equals(","))
                    linha += objetos[i];
                else{
                    JsonResult.add(linha);
                    linha = "";
                }
            }
        }

        if(JsonResult.size() <= 2)
            return;

        for(String s:JsonResult){
            String key = s.substring(0, s.indexOf(":"));
            String value = s.substring(s.indexOf(":")+1);
            map.put(key, value);
        };
    }
}
