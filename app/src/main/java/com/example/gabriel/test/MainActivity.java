package com.example.gabriel.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static String json;
    public static boolean done;

    public void Pesquisar(View v){
        json = "";
        done = false;

        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Nome do filme Vazio");
        alert.setMessage("O nome do filme não pode estar Vazio");

        String txt = ((TextView)findViewById(R.id.searchField)).getText().toString();
        if(txt.equals("")) {
            alert.show();
            return;
        }

        File folder = getFilesDir();
        String docs = "MoviesFinder/Posters";
        File moviesFinderFolder = new File(folder, docs);
        if(!moviesFinderFolder.exists())
            moviesFinderFolder.mkdirs();

        CheckForSavedMovie csm = new CheckForSavedMovie(folder, hasInternetConnection());
        csm.execute(txt);

        if(!CheckForSavedMovie.ERROR_MESSAGE.equals("NDA")){
            alert.setTitle("ERRO");
            alert.setMessage(CheckForSavedMovie.ERROR_MESSAGE);
            alert.show();
            return;
        }

        Intent intent = new Intent(this, FilmActivity.class);
        startActivity(intent);
    }

    private boolean hasInternetConnection(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return (info != null && info.isConnected());
    }
}