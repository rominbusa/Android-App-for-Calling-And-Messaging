package com.example.callingandmessaging;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class BuildURL {

    URL url = null;

    String callto,whattodo,name,audioStr;

    //build url to send request to wit api
    public String buildUrl(String text_of_audio) {
        String wit = "https://api.wit.ai/message";
        String google = "https://www.google.com/";
        Uri sendRequest_wit = Uri.parse(wit).buildUpon()
                .appendQueryParameter("q", text_of_audio).build();


        try {
            url = new URL(sendRequest_wit.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("url",url.toString());
      //  TextView showurl = (TextView) findViewById(R.id.showUrl);
       // showurl.setText(url.toString());

        try {
            audioStr = new WitQueryTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return audioStr;

        //TextView showName = (TextView) findViewById(R.id.showName);
        //showName.setText(callto);

    }

    public class WitQueryTask extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls){
            URL searchUrl = urls[0];
            String witSerachResult = null;
            try{
                witSerachResult=getResponseFromHttpUrl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }

            String s= witSerachResult;
          //  TextView showurl=(TextView) findViewById(R.id.showUrl);


            JSONArray t = null;
            if(s!=null && !s.equals("")){
                try {
                    JSONObject contact = new JSONObject(s);
                    JSONObject entity = contact.getJSONObject("entities");
                  try{
                      if(entity.getJSONArray("call") != null) {
                        JSONArray doit = entity.getJSONArray("call");
                        JSONObject what = doit.getJSONObject(0);
                        whattodo = what.getString("value");
                    }
                  }catch (JSONException e){
                      Log.d("no value for call","user doesn't want to call");
                  }

                  try {
                      if (entity.getJSONArray("message") != null) {
                          JSONArray messageArr = entity.getJSONArray("message");
                          JSONObject messageObj = messageArr.getJSONObject(0);
                          whattodo = messageObj.getString("value");
                      }
                  }catch (JSONException e){
                      Log.d("no value for message","user doesn't want to message");
                  }
                    JSONArray Name = entity.getJSONArray("contact");
                    JSONObject uname = Name.getJSONObject(0);
                    name = uname.getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            //    showurl.setText(whattodo+" "+name);
            }

            return whattodo+name;
        }
    }

    //set up to send request to wit api
    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.addRequestProperty("Authorization","Bearer JVNDXWXVDDQN2IJUJJY7NQXCPS23M7DX");
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
