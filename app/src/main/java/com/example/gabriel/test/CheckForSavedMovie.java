package com.example.gabriel.test;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CheckForSavedMovie extends AsyncTask<String, String, Void> {

    private File root;
    private boolean connected;

    public String json;
    public static String ERROR_MESSAGE = "NDA";

    public CheckForSavedMovie(File root, boolean connected){
        this.root = root;
        this.connected = connected;
    }

    private void LiberaEspaco(File root){
        if(root.listFiles().length == 0){
            ERROR_MESSAGE = "Não ha espaço suficiente para guardar essa pesquisa";
            return;
        }else{
            for(int i=root.listFiles().length; i>root.listFiles().length/2; i--)
                root.listFiles()[i].delete();
        }
    }

    private String GetJson(String name) {
        name = name.replace(" ","%20");
        name = name.replace("\'","%27");
        String url = "http://www.omdbapi.com/?t="+name;
        try {
            if(connected) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                HttpResponse response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();

                InputStream is = entity.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = rd.readLine();
                is.close();

                if (jsonText.equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}"))
                    return "";

                return jsonText;
            }
            return "No Internet";
        } catch(Exception e){
            e.printStackTrace();
            ERROR_MESSAGE = "Não foi possivel achar o filme";
            return "ERROR";
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String filmName = params[0];

        if(root.exists()) {
            for(int i=0; i<20; i++)
                System.out.println(root.getAbsolutePath());
        }

        File film = new File(root, filmName+".txt");

        try {
            if(!film.exists()) {
                json = GetJson(filmName);
                if(json.equals("")){
                    ERROR_MESSAGE = "Nome do filme errado ou filme inexistente";
                    return null;
                }else if(json.equals("ERROR")) {
                    return null;
                }else if(json.equals("No Internet")){
                    BufferedReader reader = new BufferedReader(new FileReader(film));
                    json = reader.readLine();
                    reader.close();
                }

                MainActivity.json = json;
                MainActivity.done = true;

                if(!json.equals("No Internet")) {
                    FileWriter writer = new FileWriter(film);
                    writer.append(json);
                    writer.flush();
                    writer.close();
                }
            }else{
                BufferedReader reader = new BufferedReader(new FileReader(film));
                json = reader.readLine();
                MainActivity.json = json;
                MainActivity.done = true;
                reader.close();
            }
        }catch(IOException e){
            json = GetJson(filmName);
            LiberaEspaco(root);
        }

        return null;
    }
}
